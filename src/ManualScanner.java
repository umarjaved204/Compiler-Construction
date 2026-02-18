import java.io.*;
import java.util.*;

public class ManualScanner {
    private String input;
    private int pos;
    private int line;
    private int column;
    private int tokenStartColumn;
    private List<Token> tokens;
    private ErrorHandler errorHandler;
    private SymbolTable symbolTable;
    
    // Statistics
    private Map<TokenType, Integer> tokenCounts;
    private int commentCount;
    
    // Keywords set for quick lookup
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "start", "finish", "loop", "condition", "declare", "output", 
        "input", "function", "return", "break", "continue", "else"
    ));

    public ManualScanner(String input) {
        this.input = input;
        this.pos = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
        this.errorHandler = new ErrorHandler();
        this.symbolTable = new SymbolTable();
        this.tokenCounts = new HashMap<>();
        this.commentCount = 0;
    }

    public List<Token> scan() {
        while (pos < input.length()) {
            tokenStartColumn = column;
            
            // Priority order for pattern matching
            if (tryMultiLineComment()) continue;
            if (trySingleLineComment()) continue;
            if (tryMultiCharOperator()) continue;
            if (tryKeyword()) continue;
            if (tryBooleanLiteral()) continue;
            if (tryIdentifier()) continue;
            if (tryFloatLiteral()) continue;
            if (tryIntegerLiteral()) continue;
            if (tryStringLiteral()) continue;
            if (tryCharLiteral()) continue;
            if (trySingleCharOperator()) continue;
            if (tryPunctuator()) continue;
            if (tryWhitespace()) continue;
            
            // If nothing matched, it's an error
            char ch = peek();
            errorHandler.reportInvalidCharacter(line, column, ch);
            consume();
        }
        
        return tokens;
    }

    private boolean tryMultiLineComment() {
        if (peek() == '#' && peekAhead(1) == '*') {
            int startLine = line;
            int startCol = column;
            StringBuilder comment = new StringBuilder();
            comment.append(consume()); // #
            comment.append(consume()); // *
            
            while (pos < input.length()) {
                if (peek() == '*' && peekAhead(1) == '#') {
                    comment.append(consume()); // *
                    comment.append(consume()); // #
                    commentCount++;
                    return true;
                }
                comment.append(consume());
            }
            
            // Unclosed comment
            errorHandler.reportUnclosedComment(startLine, startCol);
            return true;
        }
        return false;
    }

    private boolean trySingleLineComment() {
        if (peek() == '#' && peekAhead(1) == '#') {
            consume(); // #
            consume(); // #
            
            while (pos < input.length() && peek() != '\n') {
                consume();
            }
            commentCount++;
            return true;
        }
        return false;
    }

    private boolean tryMultiCharOperator() {
        String twoChar = peekString(2);
        
        // Check two-character operators
        if (twoChar.equals("**") || twoChar.equals("==") || twoChar.equals("!=") ||
            twoChar.equals("<=") || twoChar.equals(">=") || twoChar.equals("&&") ||
            twoChar.equals("||") || twoChar.equals("++") || twoChar.equals("--") ||
            twoChar.equals("+=") || twoChar.equals("-=") || twoChar.equals("*=") ||
            twoChar.equals("/=")) {
            
            String lexeme = consume() + "" + consume();
            TokenType type = getOperatorType(lexeme);
            addToken(type, lexeme);
            return true;
        }
        
        return false;
    }

    private boolean tryKeyword() {
        // Check if current position starts a keyword
        for (String keyword : KEYWORDS) {
            if (matchesKeyword(keyword)) {
                String lexeme = "";
                for (int i = 0; i < keyword.length(); i++) {
                    lexeme += consume();
                }
                addToken(TokenType.KEYWORD, lexeme);
                return true;
            }
        }
        return false;
    }
    
    private boolean matchesKeyword(String keyword) {
        // Check if we have enough characters
        if (pos + keyword.length() > input.length()) {
            return false;
        }
        
        // Check if the keyword matches
        for (int i = 0; i < keyword.length(); i++) {
            if (input.charAt(pos + i) != keyword.charAt(i)) {
                return false;
            }
        }
        
        // Make sure it's not part of a longer identifier
        // (keyword must be followed by non-alphanumeric character)
        if (pos + keyword.length() < input.length()) {
            char nextChar = input.charAt(pos + keyword.length());
            if (isAlphaNumeric(nextChar)) {
                return false;
            }
        }
        
        return true;
    }

    private boolean tryIdentifier() {
        if (!isUpperLetter(peek())) {
            return false;
        }
        
        StringBuilder sb = new StringBuilder();
        int startLine = line;
        int startCol = column;
        
        // First character must be uppercase letter
        sb.append(consume());
        
        // Followed by lowercase letters, digits, or underscores
        while (pos < input.length() && (isLowerLetter(peek()) || isDigit(peek()) || peek() == '_')) {
            sb.append(consume());
        }
        
        String lexeme = sb.toString();
        
        // Check if it's too long
        if (lexeme.length() > 31) {
            errorHandler.reportInvalidIdentifier(startLine, startCol, lexeme, 
                "Identifier exceeds maximum length of 31 characters");
            return true;
        }
        
        addToken(TokenType.IDENTIFIER, lexeme);
        symbolTable.addIdentifier(lexeme, startLine, startCol);
        
        return true;
    }

    private boolean tryBooleanLiteral() {
        String four = peekString(4);
        String five = peekString(5);
        
        if (five.equals("false") && !isAlphaNumeric(peekAhead(5))) {
            String lexeme = "";
            for (int i = 0; i < 5; i++) lexeme += consume();
            addToken(TokenType.BOOLEAN_LITERAL, lexeme);
            return true;
        }
        
        if (four.equals("true") && !isAlphaNumeric(peekAhead(4))) {
            String lexeme = "";
            for (int i = 0; i < 4; i++) lexeme += consume();
            addToken(TokenType.BOOLEAN_LITERAL, lexeme);
            return true;
        }
        
        return false;
    }

    private boolean tryFloatLiteral() {
        int savePos = pos;
        int saveLine = line;
        int saveCol = column;
        
        StringBuilder sb = new StringBuilder();
        
        // Optional sign
        if (peek() == '+' || peek() == '-') {
            sb.append(consume());
        }
        
        // Must have at least one digit before decimal point
        if (!isDigit(peek())) {
            pos = savePos;
            line = saveLine;
            column = saveCol;
            return false;
        }
        
        while (isDigit(peek())) {
            sb.append(consume());
        }
        
        // Must have decimal point
        if (peek() != '.') {
            pos = savePos;
            line = saveLine;
            column = saveCol;
            return false;
        }
        
        sb.append(consume()); // consume '.'
        
        // Must have 1-6 digits after decimal point
        int decimalDigits = 0;
        int decStartPos = pos;
        while (isDigit(peek())) {
            sb.append(consume());
            decimalDigits++;
        }
        
        if (decimalDigits == 0 || decimalDigits > 6) {
            String lexeme = sb.toString();
            if (decimalDigits > 6) {
                // Consume extra digits
                while (isDigit(peek())) {
                    lexeme += consume();
                }
            }
            errorHandler.reportMalformedLiteral(saveLine, saveCol, lexeme, 
                "Floating-point literal must have 1-6 digits after decimal point");
            return true;
        }
        
        // Optional exponent
        if (peek() == 'e' || peek() == 'E') {
            sb.append(consume());
            
            // Optional sign in exponent
            if (peek() == '+' || peek() == '-') {
                sb.append(consume());
            }
            
            // Must have at least one digit in exponent
            if (!isDigit(peek())) {
                errorHandler.reportMalformedLiteral(saveLine, saveCol, sb.toString(), 
                    "Exponent must have at least one digit");
                return true;
            }
            
            while (isDigit(peek())) {
                sb.append(consume());
            }
        }
        
        addToken(TokenType.FLOAT_LITERAL, sb.toString());
        return true;
    }

    private boolean tryIntegerLiteral() {
        // Look ahead to avoid matching float literals
        int savePos = pos;
        int saveLine = line;
        int saveCol = column;
        
        StringBuilder sb = new StringBuilder();
        
        // Optional sign
        if (peek() == '+' || peek() == '-') {
            sb.append(consume());
        }
        
        // Must have at least one digit
        if (!isDigit(peek())) {
            pos = savePos;
            line = saveLine;
            column = saveCol;
            return false;
        }
        
        while (isDigit(peek())) {
            sb.append(consume());
        }
        
        // Check if it's actually a float (has decimal point)
        if (peek() == '.') {
            pos = savePos;
            line = saveLine;
            column = saveCol;
            return false;
        }
        
        addToken(TokenType.INTEGER_LITERAL, sb.toString());
        return true;
    }

    private boolean tryStringLiteral() {
        if (peek() != '"') {
            return false;
        }
        
        int startLine = line;
        int startCol = column;
        StringBuilder sb = new StringBuilder();
        sb.append(consume()); // opening "
        
        while (pos < input.length() && peek() != '"' && peek() != '\n') {
            if (peek() == '\\') {
                sb.append(consume()); // backslash
                if (pos < input.length()) {
                    char next = peek();
                    if (next == '"' || next == '\\' || next == 'n' || 
                        next == 't' || next == 'r') {
                        sb.append(consume());
                    } else {
                        // Invalid escape sequence
                        sb.append(consume());
                    }
                }
            } else {
                sb.append(consume());
            }
        }
        
        if (pos >= input.length() || peek() != '"') {
            errorHandler.reportUnterminatedString(startLine, startCol, sb.toString());
            return true;
        }
        
        sb.append(consume()); // closing "
        addToken(TokenType.STRING_LITERAL, sb.toString());
        return true;
    }

    private boolean tryCharLiteral() {
        if (peek() != '\'') {
            return false;
        }
        
        int startLine = line;
        int startCol = column;
        StringBuilder sb = new StringBuilder();
        sb.append(consume()); // opening '
        
        if (pos >= input.length()) {
            errorHandler.reportUnterminatedChar(startLine, startCol, sb.toString());
            return true;
        }
        
        if (peek() == '\\') {
            sb.append(consume()); // backslash
            if (pos < input.length()) {
                char next = peek();
                if (next == '\'' || next == '\\' || next == 'n' || 
                    next == 't' || next == 'r') {
                    sb.append(consume());
                } else {
                    sb.append(consume());
                }
            }
        } else if (peek() != '\'' && peek() != '\n') {
            sb.append(consume());
        }
        
        if (pos >= input.length() || peek() != '\'') {
            errorHandler.reportUnterminatedChar(startLine, startCol, sb.toString());
            return true;
        }
        
        sb.append(consume()); // closing '
        addToken(TokenType.CHAR_LITERAL, sb.toString());
        return true;
    }

    private boolean trySingleCharOperator() {
        char ch = peek();
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || 
            ch == '%' || ch == '=' || ch == '<' || ch == '>' || ch == '!') {
            String lexeme = String.valueOf(consume());
            TokenType type = getOperatorType(lexeme);
            addToken(type, lexeme);
            return true;
        }
        return false;
    }

    private boolean tryPunctuator() {
        char ch = peek();
        if (ch == '(' || ch == ')' || ch == '{' || ch == '}' ||
            ch == '[' || ch == ']' || ch == ',' || ch == ';' || ch == ':') {
            String lexeme = String.valueOf(consume());
            addToken(TokenType.PUNCTUATOR, lexeme);
            return true;
        }
        return false;
    }

    private boolean tryWhitespace() {
        if (isWhitespace(peek())) {
            while (pos < input.length() && isWhitespace(peek())) {
                consume();
            }
            return true;
        }
        return false;
    }

    private TokenType getOperatorType(String op) {
        if (op.equals("+") || op.equals("-") || op.equals("*") || 
            op.equals("/") || op.equals("%") || op.equals("**")) {
            return TokenType.ARITHMETIC_OP;
        } else if (op.equals("==") || op.equals("!=") || op.equals("<") || 
                   op.equals(">") || op.equals("<=") || op.equals(">=")) {
            return TokenType.RELATIONAL_OP;
        } else if (op.equals("&&") || op.equals("||") || op.equals("!")) {
            return TokenType.LOGICAL_OP;
        } else if (op.equals("=") || op.equals("+=") || op.equals("-=") || 
                   op.equals("*=") || op.equals("/=")) {
            return TokenType.ASSIGNMENT_OP;
        } else if (op.equals("++")) {
            return TokenType.INCREMENT_OP;
        } else if (op.equals("--")) {
            return TokenType.DECREMENT_OP;
        }
        return TokenType.ERROR;
    }

    private void addToken(TokenType type, String lexeme) {
        Token token = new Token(type, lexeme, line, tokenStartColumn);
        tokens.add(token);
        tokenCounts.put(type, tokenCounts.getOrDefault(type, 0) + 1);
    }

    private char peek() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos);
    }

    private char peekAhead(int offset) {
        if (pos + offset >= input.length()) return '\0';
        return input.charAt(pos + offset);
    }

    private String peekString(int length) {
        int end = Math.min(pos + length, input.length());
        return input.substring(pos, end);
    }

    private char consume() {
        char ch = input.charAt(pos++);
        if (ch == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        return ch;
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private boolean isUpperLetter(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    private boolean isLowerLetter(char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    private boolean isLetter(char ch) {
        return isUpperLetter(ch) || isLowerLetter(ch);
    }

    private boolean isAlphaNumeric(char ch) {
        return isLetter(ch) || isDigit(ch) || ch == '_';
    }

    private boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public void printTokens() {
        System.out.println("\n=== Tokens ===");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public void printStatistics() {
        System.out.println("\n=== Statistics ===");
        System.out.println("Total tokens: " + tokens.size());
        System.out.println("Lines processed: " + line);
        System.out.println("Comments removed: " + commentCount);
        System.out.println("\nToken counts by type:");
        
        tokenCounts.entrySet().stream()
            .sorted(Map.Entry.<TokenType, Integer>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.println("  " + entry.getKey() + ": " + entry.getValue()));
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    // Main method for testing
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java ManualScanner <input-file>");
            return;
        }

        try {
            // Read input file
            String content = readFile(args[0]);
            
            // Create scanner and scan
            ManualScanner scanner = new ManualScanner(content);
            scanner.scan();
            
            // Print results
            scanner.printTokens();
            scanner.printStatistics();
            scanner.getSymbolTable().print();
            scanner.getErrorHandler().printErrorSummary();
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        }
        return content.toString();
    }
}

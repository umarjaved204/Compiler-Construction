import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private List<LexicalError> errors;
    private int errorCount;

    public ErrorHandler() {
        this.errors = new ArrayList<>();
        this.errorCount = 0;
    }

    public void reportError(ErrorType type, int line, int column, String lexeme, String reason) {
        LexicalError error = new LexicalError(type, line, column, lexeme, reason);
        errors.add(error);
        errorCount++;
        System.err.println(error);
    }

    public void reportInvalidCharacter(int line, int column, char ch) {
        reportError(ErrorType.INVALID_CHARACTER, line, column, 
                   String.valueOf(ch), 
                   "Invalid character '" + ch + "' not recognized");
    }

    public void reportMalformedLiteral(int line, int column, String lexeme, String reason) {
        reportError(ErrorType.MALFORMED_LITERAL, line, column, lexeme, reason);
    }

    public void reportInvalidIdentifier(int line, int column, String lexeme, String reason) {
        reportError(ErrorType.INVALID_IDENTIFIER, line, column, lexeme, reason);
    }

    public void reportUnclosedComment(int line, int column) {
        reportError(ErrorType.UNCLOSED_COMMENT, line, column, 
                   "#*", "Multi-line comment not closed");
    }

    public void reportUnterminatedString(int line, int column, String lexeme) {
        reportError(ErrorType.UNTERMINATED_STRING, line, column, 
                   lexeme, "String literal not terminated");
    }

    public void reportUnterminatedChar(int line, int column, String lexeme) {
        reportError(ErrorType.UNTERMINATED_CHAR, line, column, 
                   lexeme, "Character literal not terminated");
    }

    public boolean hasErrors() {
        return errorCount > 0;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public List<LexicalError> getErrors() {
        return new ArrayList<>(errors);
    }

    public void printErrorSummary() {
        if (errorCount == 0) {
            System.out.println("\n✓ No lexical errors found.");
            return;
        }
        
        System.out.println("\n=== Lexical Error Summary ===");
        System.out.println("Total errors: " + errorCount);
        System.out.println("\nErrors by type:");
        
        long invalidChars = errors.stream().filter(e -> e.type == ErrorType.INVALID_CHARACTER).count();
        long malformedLits = errors.stream().filter(e -> e.type == ErrorType.MALFORMED_LITERAL).count();
        long invalidIds = errors.stream().filter(e -> e.type == ErrorType.INVALID_IDENTIFIER).count();
        long unclosedComms = errors.stream().filter(e -> e.type == ErrorType.UNCLOSED_COMMENT).count();
        long untermStrs = errors.stream().filter(e -> e.type == ErrorType.UNTERMINATED_STRING).count();
        long untermChars = errors.stream().filter(e -> e.type == ErrorType.UNTERMINATED_CHAR).count();
        
        if (invalidChars > 0) System.out.println("  Invalid characters: " + invalidChars);
        if (malformedLits > 0) System.out.println("  Malformed literals: " + malformedLits);
        if (invalidIds > 0) System.out.println("  Invalid identifiers: " + invalidIds);
        if (unclosedComms > 0) System.out.println("  Unclosed comments: " + unclosedComms);
        if (untermStrs > 0) System.out.println("  Unterminated strings: " + untermStrs);
        if (untermChars > 0) System.out.println("  Unterminated chars: " + untermChars);
    }

    public void clear() {
        errors.clear();
        errorCount = 0;
    }

    // Inner class to represent a lexical error
    public static class LexicalError {
        public ErrorType type;
        public int line;
        public int column;
        public String lexeme;
        public String reason;

        public LexicalError(ErrorType type, int line, int column, String lexeme, String reason) {
            this.type = type;
            this.line = line;
            this.column = column;
            this.lexeme = lexeme;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return String.format("Lexical Error [%s] at Line: %d, Col: %d - Lexeme: \"%s\" - Reason: %s",
                               type, line, column, lexeme, reason);
        }
    }

    // Error types enumeration
    public enum ErrorType {
        INVALID_CHARACTER,
        MALFORMED_LITERAL,
        INVALID_IDENTIFIER,
        UNCLOSED_COMMENT,
        UNTERMINATED_STRING,
        UNTERMINATED_CHAR
    }
}

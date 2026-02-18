/* JFlex Scanner Specification */

import java.io.*;
import java.util.*;

%%

%class Yylex
%public
%line
%column
%unicode
%type void
%eofval{
    return;
%eofval}

%{
    private List<Token> tokens = new ArrayList<>();
    private ErrorHandler errorHandler = new ErrorHandler();
    private SymbolTable symbolTable = new SymbolTable();
    private Map<TokenType, Integer> tokenCounts = new HashMap<>();
    private int commentCount = 0;

    private void addToken(TokenType type, String lexeme) {
        Token token = new Token(type, lexeme, yyline + 1, yycolumn + 1);
        tokens.add(token);
        tokenCounts.put(type, tokenCounts.getOrDefault(type, 0) + 1);
        
        if (type == TokenType.IDENTIFIER) {
            symbolTable.addIdentifier(lexeme, yyline + 1, yycolumn + 1);
        }
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

    public void printTokens() {
        System.out.println("\n=== Tokens (JFlex) ===");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public void printStatistics() {
        System.out.println("\n=== Statistics (JFlex) ===");
        System.out.println("Total tokens: " + tokens.size());
        System.out.println("Lines processed: " + (yyline + 1));
        System.out.println("Comments removed: " + commentCount);
        System.out.println("\nToken counts by type:");
        
        tokenCounts.entrySet().stream()
            .sorted(Map.Entry.<TokenType, Integer>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.println("  " + entry.getKey() + ": " + entry.getValue()));
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Yylex <input-file>");
            return;
        }

        try {
            Yylex scanner = new Yylex(new FileReader(args[0]));
            scanner.yylex();
            
            scanner.printTokens();
            scanner.printStatistics();
            scanner.getSymbolTable().print();
            scanner.getErrorHandler().printErrorSummary();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
%}

/* Macro Definitions */
DIGIT           = [0-9]
UPPER           = [A-Z]
LOWER           = [a-z]
LETTER          = [A-Za-z]
WHITESPACE      = [ \t\r\n]

/* Identifiers */
IDENTIFIER      = {UPPER}[a-z0-9_]{0,30}

/* Literals */
INTEGER         = [+-]?{DIGIT}+
FLOAT           = [+-]?{DIGIT}+\.{DIGIT}{1,6}([eE][+-]?{DIGIT}+)?
STRING          = \"([^\"\\\n]|\\[\"\\\ntr])*\"
CHAR            = '([^'\\\n]|\\['\\\ntr])'

/* Operators - Simple character class only */
ARITH_OP        = [%]
REL_OP_1        = [<>]

/* Punctuators */
PUNCTUATOR      = [(){}\[\],;:]

/* Comments */
SINGLE_COMMENT  = ##[^\n]*
MULTI_COMMENT   = #\*([^*]|\*+[^*#])*\*+#

/* Error patterns */
INVALID_FLOAT   = [+-]?{DIGIT}+\.{DIGIT}{DIGIT}{DIGIT}{DIGIT}{DIGIT}{DIGIT}{DIGIT}{DIGIT}+([eE][+-]?{DIGIT}+)?
INVALID_ID      = {UPPER}[a-z0-9_]{31}[a-z0-9_]+
UNTERM_STRING   = \"([^\"\\\n]|\\[\"\\\ntr])*
UNTERM_CHAR     = \'([^\'\\\n]|\\[\'\\\ntr])
UNCLOSED_MULTI  = #\*([^*]|\*+[^*#])*

%%

/* Lexical Rules - Priority Order */

/* Multi-line comments (highest priority) */
{MULTI_COMMENT}     { commentCount++; }

/* Single-line comments */
{SINGLE_COMMENT}    { commentCount++; }

/* Multi-character operators - Explicit literals */
"**"                { addToken(TokenType.ARITHMETIC_OP, yytext()); }
"++"                { addToken(TokenType.INCREMENT_OP, yytext()); }
"--"                { addToken(TokenType.DECREMENT_OP, yytext()); }
"=="                { addToken(TokenType.RELATIONAL_OP, yytext()); }
"!="                { addToken(TokenType.RELATIONAL_OP, yytext()); }
"<="                { addToken(TokenType.RELATIONAL_OP, yytext()); }
">="                { addToken(TokenType.RELATIONAL_OP, yytext()); }
"&&"                { addToken(TokenType.LOGICAL_OP, yytext()); }
"||"                { addToken(TokenType.LOGICAL_OP, yytext()); }
"+="                { addToken(TokenType.ASSIGNMENT_OP, yytext()); }
"-="                { addToken(TokenType.ASSIGNMENT_OP, yytext()); }
"*="                { addToken(TokenType.ASSIGNMENT_OP, yytext()); }
"/="                { addToken(TokenType.ASSIGNMENT_OP, yytext()); }

/* Keywords - Explicit literals */
"start"             { addToken(TokenType.KEYWORD, yytext()); }
"finish"            { addToken(TokenType.KEYWORD, yytext()); }
"loop"              { addToken(TokenType.KEYWORD, yytext()); }
"condition"         { addToken(TokenType.KEYWORD, yytext()); }
"declare"           { addToken(TokenType.KEYWORD, yytext()); }
"output"            { addToken(TokenType.KEYWORD, yytext()); }
"input"             { addToken(TokenType.KEYWORD, yytext()); }
"function"          { addToken(TokenType.KEYWORD, yytext()); }
"return"            { addToken(TokenType.KEYWORD, yytext()); }
"break"             { addToken(TokenType.KEYWORD, yytext()); }
"continue"          { addToken(TokenType.KEYWORD, yytext()); }
"else"              { addToken(TokenType.KEYWORD, yytext()); }

/* Boolean literals - Explicit literals */
"true"              { addToken(TokenType.BOOLEAN_LITERAL, yytext()); }
"false"             { addToken(TokenType.BOOLEAN_LITERAL, yytext()); }

/* Identifiers */
{IDENTIFIER}        { addToken(TokenType.IDENTIFIER, yytext()); }

/* Floating-point literals */
{FLOAT}             { addToken(TokenType.FLOAT_LITERAL, yytext()); }

/* Integer literals */
{INTEGER}           { addToken(TokenType.INTEGER_LITERAL, yytext()); }

/* String literals */
{STRING}            { addToken(TokenType.STRING_LITERAL, yytext()); }

/* Character literals */
{CHAR}              { addToken(TokenType.CHAR_LITERAL, yytext()); }

/* Single-character operators - Explicit literals */
"+"                 { addToken(TokenType.ARITHMETIC_OP, yytext()); }
"-"                 { addToken(TokenType.ARITHMETIC_OP, yytext()); }
"*"                 { addToken(TokenType.ARITHMETIC_OP, yytext()); }
"/"                 { addToken(TokenType.ARITHMETIC_OP, yytext()); }
"!"                 { addToken(TokenType.LOGICAL_OP, yytext()); }
"="                 { addToken(TokenType.ASSIGNMENT_OP, yytext()); }
{ARITH_OP}          { addToken(TokenType.ARITHMETIC_OP, yytext()); }
{REL_OP_1}          { addToken(TokenType.RELATIONAL_OP, yytext()); }

/* Punctuators */
{PUNCTUATOR}        { addToken(TokenType.PUNCTUATOR, yytext()); }

/* Punctuators */
{PUNCTUATOR}        { addToken(TokenType.PUNCTUATOR, yytext()); }

/* Whitespace (skip) */
{WHITESPACE}        { /* skip */ }

/* Error patterns */
{INVALID_FLOAT}     { errorHandler.reportMalformedLiteral(yyline + 1, yycolumn + 1, yytext(), 
                        "More than 6 decimal digits"); }

{INVALID_ID}        { errorHandler.reportInvalidIdentifier(yyline + 1, yycolumn + 1, yytext(), 
                        "Identifier exceeds 31 characters"); }

{UNTERM_STRING}     { errorHandler.reportUnterminatedString(yyline + 1, yycolumn + 1, yytext()); }

{UNTERM_CHAR}       { errorHandler.reportUnterminatedChar(yyline + 1, yycolumn + 1, yytext()); }

{UNCLOSED_MULTI}    { errorHandler.reportUnclosedComment(yyline + 1, yycolumn + 1); }

/* Invalid characters */
.                   { errorHandler.reportInvalidCharacter(yyline + 1, yycolumn + 1, yytext().charAt(0)); }

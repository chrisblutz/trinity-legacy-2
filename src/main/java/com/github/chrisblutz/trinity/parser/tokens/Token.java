package com.github.chrisblutz.trinity.parser.tokens;

/**
 * @author Christopher Lutz
 */
public enum Token {
    
    // Miscellaneous
    UNIDENTIFIED_TOKEN("\0"),
    
    // Whitespace
    WS_SPACE(" "), WS_TAB("\t"),
    
    // Punctuation / Operators
    LEFT_PARENTHESIS("("), LEFT_SQUARE_BRACKET("["), LEFT_CURLY_BRACKET("{"),
    RIGHT_PARENTHESIS(")"), RIGHT_SQUARE_BRACKET("]"), RIGHT_CURLY_BRACKET("}"),
    SINGLE_QUOTATION("'"), DOUBLE_QUOTATION("\""),
    COLON(":"), SEMICOLON(";"),
    COMMA(","), QUESTION_MARK("?"),
    COMMENT_MARKER("#"),
    GLOBAL_VARIABLE_PREFIX("$"),
    
    DOT_OP("."), DOUBLE_DOT_OP(".."), TRIPLE_DOT_OP("..."),
    PLUS_OP("+"), MINUS_OP("-"), MULTIPLY_OP("*"), DIVIDE_OP("/"), MODULUS_OP("%"), EXPONENT_OP("**"),
    BITWISE_AND_OP("&"), BITWISE_OR_OP("|"), BITWISE_XOR_OP("^"), BITWISE_COMPLEMENT_OP("~"),
    LEFT_SHIFT_OP("<<"), RIGHT_SHIFT_OP(">>"), RIGHT_LOGICAL_SHIFT_OP(">>>"),
    LOGICAL_NEGATION_OP("!"), LOGICAL_AND_OP("&&"), LOGICAL_OR_OP("||"),
    EQUAL_TO_OP("=="), NOT_EQUAL_TO_OP("!="), COMPARISON_OP("<=>"),
    GREATER_THAN_OP(">"), GREATER_THAN_OR_EQUAL_TO_OP(">="), LESS_THAN_OP("<"), LESS_THAN_OR_EQUAL_TO_OP("<="),
    ASSIGNMENT_OP("="), NIL_ASSIGNMENT_OP("||="),
    PLUS_EQUAL_OP("+="), MINUS_EQUAL_OP("-="), MULTIPLY_EQUAL_OP("*="), DIVIDE_EQUAL_OP("/="), MODULUS_EQUAL_OP("%="), EXPONENT_EQUAL_OP("**="),
    BITWISE_AND_EQUAL_OP("&="), BITWISE_OR_EQUAL_OP("|="), BITWISE_XOR_EQUAL_OP("^="),
    LEFT_SHIFT_EQUAL_OP("<<="), RIGHT_SHIFT_EQUAL_OP(">>="), RIGHT_LOGICAL_SHIFT_EQUAL_OP(">>>="),
    
    // Strings
    TOKEN_STRING("\0"),
    LITERAL_STRING("\0"),
    NUMERIC_STRING("\0"),
    
    // Values
    NIL("nil"),
    TRUE("true"),
    FALSE("false"),
    
    // Constants
    __FILE__("__FILE__"),
    __LINE__("__LINE__"),
    
    // References
    SUPER("super"),
    THIS("this"),
    
    // Keywords
    MODULE("module", true), CLASS("class", true), INTERFACE("interface", true), DEF("def", true), DEF_NATIVE("def native"), VAR("var"), VAL("val"),
    IF("if", true), ELSIF("elsif", true, true), ELSE("else", true, true),
    FOR("for", true), WHILE("while", true), SWITCH("switch"), CASE("case"), DEFAULT("default"),
    TRY("try"), CATCH("catch"), FINALLY("finally"),
    DO("do"), END("end"),
    REQUIRE("require"), USING("using"), RETURN("return"),
    BREAK("break"), CONTINUE("continue"),
    NATIVE("native"), STATIC("static"), SECURE("secure"), FINAL("final"),
    PUBLIC("public", true), PROTECTED("protected", true), PRIVATE("private", true),
    
    // Combination Sequences
    BLOCK_CHECK("block?"), SCOPE_MODIFIER("\0", true),
    
    // Escape Sequences
    BACKSLASH("\\"), BACKSLASH_ESCAPE("\\\\", "\\"),
    NEWLINE_ESCAPE("\\n", "\n"), RETURN_ESCAPE("\\r", "\r"),
    FORM_FEED_ESCAPE("\\f", "\f"),
    BACKSPACE_ESCAPE("\\b", "\b"),
    TAB_ESCAPE("\\t", "\t"),
    SINGLE_QUOTE_ESCAPE("\\'", "\'"), DOUBLE_QUOTE_ESCAPE("\\\"", "\""),
    NIL_CHARACTER_ESCAPE("\\0", "\0"), UNICODE_ESCAPE("\\u", "\0");
    
    private String readable, literal;
    private boolean blocking, blockContinuing;
    
    Token(String readable) {
        
        this(readable, readable, false, false);
    }
    
    Token(String readable, boolean blocking) {
        
        this(readable, readable, blocking, false);
    }
    
    Token(String readable, boolean blocking, boolean blockContinuing) {
        
        this(readable, readable, blocking, blockContinuing);
    }
    
    Token(String readable, String literal) {
        
        this(readable, literal, false, false);
    }
    
    Token(String readable, String literal, boolean blocking, boolean blockContinuing) {
        
        this.readable = readable;
        this.literal = literal;
        this.blocking = blocking;
        this.blockContinuing = blockContinuing;
    }
    
    public String getReadable() {
        
        return readable;
    }
    
    public String getLiteral() {
        
        return literal;
    }
    
    public boolean isBlocking() {
        
        return blocking;
    }
    
    public boolean isBlockContinuing() {
        
        return blockContinuing;
    }
    
    public static Token forString(String readable) {
        
        for (Token t : values()) {
            
            if (t.getReadable().contentEquals(readable)) {
                
                return t;
            }
        }
        
        return UNIDENTIFIED_TOKEN;
    }
    
    public static boolean isToken(String readable) {
        
        return forString(readable) != UNIDENTIFIED_TOKEN;
    }
}

package com.github.chrisblutz.trinity;

import com.github.chrisblutz.trinity.interpreter.errors.TrinitySyntaxError;
import com.github.chrisblutz.trinity.parser.Parser;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.blocks.Statement;
import com.github.chrisblutz.trinity.parser.lines.Line;
import com.github.chrisblutz.trinity.parser.lines.LineSet;
import com.github.chrisblutz.trinity.parser.tokens.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author Christopher Lutz
 */
@DisplayName("Parser")
public class ParserTests {
    
    private static final File PARSER_TEST_HOME = new File(TestUtilities.TRINITY_TEST_HOME, "parser/");
    
    private static final String KEYWORDS_FILE = "Keywords";
    private static final String SYMBOLS_AND_OPERATORS_FILE = "SymbolsAndOperators";
    private static final String ESCAPE_INVALIDITY_FILE = "EscapeInvalidity";
    private static final String UNICODE_ESCAPES_FILE = "UnicodeEscapes";
    private static final String LITERAL_STRINGS_FILE = "LiteralStrings";
    private static final String CONSOLIDATION_FILE = "Consolidation";
    private static final String INDENTATION_ERRORS_FILE = "IndentationErrors";
    private static final String NUMERIC_STRINGS_FILE = "NumericStrings";
    private static final String SCOPE_MODIFIERS_FILE = "ScopeModifiers";
    private static final String BLOCKING_FILE = "Blocking";
    
    @Test
    @DisplayName("Keywords")
    public void testKeywords() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, KEYWORDS_FILE));
        LineSet set = parser.parseTokens();
        
        Token[] expected = new Token[]{
                Token.NIL,
                Token.TRUE,
                Token.FALSE,
                Token.__FILE__,
                Token.__LINE__,
                Token.SUPER,
                Token.THIS,
                Token.MODULE,
                Token.CLASS,
                Token.INTERFACE,
                Token.DEF,
                Token.DEF_NATIVE,
                Token.VAR,
                Token.VAL,
                Token.IF,
                Token.ELSIF,
                Token.ELSE,
                Token.TRY,
                Token.CATCH,
                Token.FINALLY,
                Token.DO,
                Token.END,
                Token.REQUIRE,
                Token.USING,
                Token.RETURN,
                Token.BREAK,
                Token.CONTINUE,
                Token.NATIVE,
                Token.STATIC,
                Token.SECURE,
                Token.FINAL,
                Token.BLOCK_CHECK
        };
        
        TestUtilities.checkSingleTokens(set, expected);
    }
    
    @Test
    @DisplayName("Symbols/Operators")
    public void testSymbolsAndOperators() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, SYMBOLS_AND_OPERATORS_FILE));
        LineSet set = parser.parseTokens();
        
        Token[] expected = new Token[]{
                Token.LEFT_PARENTHESIS,
                Token.RIGHT_PARENTHESIS,
                Token.LEFT_SQUARE_BRACKET,
                Token.RIGHT_SQUARE_BRACKET,
                Token.LEFT_CURLY_BRACKET,
                Token.RIGHT_CURLY_BRACKET,
                Token.COLON,
                Token.SEMICOLON,
                Token.COMMA,
                Token.QUESTION_MARK,
                Token.GLOBAL_VARIABLE_PREFIX,
                Token.DOT_OP,
                Token.DOUBLE_DOT_OP,
                Token.TRIPLE_DOT_OP,
                Token.PLUS_OP,
                Token.PLUS_EQUAL_OP,
                Token.MINUS_OP,
                Token.MINUS_EQUAL_OP,
                Token.MULTIPLY_OP,
                Token.MULTIPLY_EQUAL_OP,
                Token.DIVIDE_OP,
                Token.DIVIDE_EQUAL_OP,
                Token.MODULUS_OP,
                Token.MODULUS_EQUAL_OP,
                Token.LEFT_SHIFT_OP,
                Token.LEFT_SHIFT_EQUAL_OP,
                Token.RIGHT_SHIFT_OP,
                Token.RIGHT_SHIFT_EQUAL_OP,
                Token.RIGHT_LOGICAL_SHIFT_OP,
                Token.RIGHT_LOGICAL_SHIFT_EQUAL_OP,
                Token.BITWISE_AND_OP,
                Token.BITWISE_AND_EQUAL_OP,
                Token.BITWISE_OR_OP,
                Token.BITWISE_OR_EQUAL_OP,
                Token.BITWISE_XOR_OP,
                Token.BITWISE_XOR_EQUAL_OP,
                Token.BITWISE_COMPLEMENT_OP,
                Token.LOGICAL_NEGATION_OP,
                Token.LOGICAL_AND_OP,
                Token.LOGICAL_OR_OP,
                Token.EQUAL_TO_OP,
                Token.NOT_EQUAL_TO_OP,
                Token.COMPARISON_OP,
                Token.GREATER_THAN_OP,
                Token.GREATER_THAN_OR_EQUAL_TO_OP,
                Token.LESS_THAN_OP,
                Token.LESS_THAN_OR_EQUAL_TO_OP,
                Token.ASSIGNMENT_OP,
                Token.NIL_ASSIGNMENT_OP,
                Token.BACKSLASH,
                Token.BACKSLASH_ESCAPE,
                Token.NEWLINE_ESCAPE,
                Token.RETURN_ESCAPE,
                Token.FORM_FEED_ESCAPE,
                Token.BACKSPACE_ESCAPE,
                Token.TAB_ESCAPE,
                Token.SINGLE_QUOTE_ESCAPE,
                Token.DOUBLE_QUOTE_ESCAPE,
                Token.NIL_CHARACTER_ESCAPE
        };
        
        TestUtilities.checkSingleTokens(set, expected);
    }
    
    @Test
    @DisplayName("Escape Invalidity")
    public void testEscapeInvalidity() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, ESCAPE_INVALIDITY_FILE));
        LineSet set = parser.parseTokens();
        
        assertEquals(1, set.size());
        
        Line line = set.get(0);
        
        assertEquals(2, line.size());
        assertEquals(Token.BACKSLASH, line.get(0).getToken());
        assertEquals(Token.TOKEN_STRING, line.get(1).getToken());
        assertEquals("x", line.get(1).getContents());
    }
    
    @Test
    @DisplayName("Unicode Escapes")
    public void testUnicodeEscapes() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, UNICODE_ESCAPES_FILE));
        LineSet set = parser.parseTokens();
        
        String[] expected = new String[]{
                "\u00a9",
                "\u00a9",
                "\u00a90"
        };
        
        TestUtilities.checkTokenTypeAndContents(set, Token.LITERAL_STRING, expected);
    }
    
    @Test
    @DisplayName("Literal Strings")
    public void testLiteralStrings() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, LITERAL_STRINGS_FILE));
        LineSet set = parser.parseTokens();
        
        String[] expected = new String[]{
                "abcd",
                "abcd\\n",
                "abcd\u00a9",
                "module",
                "abcd",
                "abcd\n",
                "abcd\u00a9",
                "module"
        };
        
        TestUtilities.checkTokenTypeAndContents(set, Token.LITERAL_STRING, expected);
    }
    
    @Test
    @DisplayName("Consolidation")
    public void testConsolidation() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, CONSOLIDATION_FILE));
        LineSet set = parser.parseTokens();
        
        Token[][] expected = new Token[][]{
                new Token[]{Token.MODULE},
                new Token[]{Token.MODULE},
                new Token[]{Token.MODULE, Token.CLASS}
        };
        
        TestUtilities.checkMultipleTokens(set, expected);
    }
    
    @Test
    @DisplayName("Indentation Errors")
    public void testIndentationErrors() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, INDENTATION_ERRORS_FILE));
        
        PrintStream stdErr = System.err;
        PrintStream muffler = new PrintStream(new ByteArrayOutputStream());
        System.setErr(muffler);
        
        Executable executable = parser::parseTokens;
        assertThrows(TrinitySyntaxError.class, executable, "Parallel tabs and spaces did not trigger the expected error.");
        
        System.setErr(stdErr);
        muffler.close();
    }
    
    @Test
    @DisplayName("Numeric Strings")
    public void testNumericStrings() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, NUMERIC_STRINGS_FILE));
        LineSet set = parser.parseTokens();
        
        String[] expected = new String[]{
                "1234",
                "1234.5",
                "0b1010",
                "0b1010.1",
                "0o1234",
                "0o1234.5",
                "0x123f",
                "0x123d.4f"
        };
        
        TestUtilities.checkTokenTypeAndContents(set, Token.NUMERIC_STRING, expected);
    }
    
    @Test
    @DisplayName("Scope Modifiers")
    public void testScopeModifiers() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, SCOPE_MODIFIERS_FILE));
        LineSet set = parser.parseTokens();
        
        String[] expected = new String[]{
                "public",
                "module-protected",
                "protected",
                "private"
        };
        
        TestUtilities.checkTokenTypeAndContents(set, Token.SCOPE_MODIFIER, expected);
    }
    
    @Test
    @DisplayName("Blocking")
    public void testBlocking() {
        
        Parser parser = new Parser(TestUtilities.getTestFile(PARSER_TEST_HOME, BLOCKING_FILE));
        Block block = parser.parse();
        
        assertEquals(19, block.size());
        
        Class[] types = new Class[]{
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Block.class,
                Statement.class,
                Statement.class,
                Block.class
        };
        
        TestUtilities.checkTypes(block, types);
        
        Statement statement = (Statement) block.get(0);
        Block subBlock = (Block) block.get(1);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.IF, Token.TOKEN_STRING}, new String[]{null, "x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        
        statement = (Statement) block.get(2);
        subBlock = (Block) block.get(3);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.IF, Token.TOKEN_STRING}, new String[]{null, "x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        
        statement = (Statement) block.get(4);
        subBlock = (Block) block.get(5);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.ELSE}, new String[]{null});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"z"});
        
        statement = (Statement) block.get(6);
        subBlock = (Block) block.get(7);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.TOKEN_STRING}, new String[]{"x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        
        statement = (Statement) block.get(8);
        subBlock = (Block) block.get(9);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.TOKEN_STRING}, new String[]{"x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"z"});
        TestUtilities.checkBlockHeader(subBlock, new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        
        statement = (Statement) block.get(10);
        subBlock = (Block) block.get(11);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.TOKEN_STRING}, new String[]{"x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        
        statement = (Statement) block.get(12);
        subBlock = (Block) block.get(13);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.TOKEN_STRING}, new String[]{"x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"z"});
        TestUtilities.checkBlockHeader(subBlock, new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        
        statement = (Statement) block.get(14);
        subBlock = (Block) block.get(15);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.TOKEN_STRING}, new String[]{"x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"z"});
        TestUtilities.checkBlockHeader(subBlock, new Token[]{Token.TOKEN_STRING, Token.ASSIGNMENT_OP, Token.LEFT_PARENTHESIS, Token.NUMERIC_STRING, Token.BITWISE_OR_OP, Token.NUMERIC_STRING, Token.RIGHT_PARENTHESIS}, new String[]{"y", null, null, null, null, null, null});
        
        statement = (Statement) block.get(16);
        TestUtilities.checkStatement(statement, new Token[]{Token.LEFT_CURLY_BRACKET, Token.TOKEN_STRING, Token.COLON, Token.TOKEN_STRING, Token.RIGHT_CURLY_BRACKET}, new String[]{null, "x", null, "y", null});
        
        statement = (Statement) block.get(17);
        subBlock = (Block) block.get(18);
        
        TestUtilities.checkStatement(statement, new Token[]{Token.TOKEN_STRING}, new String[]{"x"});
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class, Block.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"y"});
        subBlock = (Block) subBlock.get(1);
        TestUtilities.checkTypes(subBlock, new Class[]{Statement.class});
        TestUtilities.checkStatement((Statement) subBlock.get(0), new Token[]{Token.TOKEN_STRING}, new String[]{"z"});
    }
}

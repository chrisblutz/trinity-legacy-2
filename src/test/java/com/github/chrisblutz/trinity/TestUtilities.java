package com.github.chrisblutz.trinity;

import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.blocks.Statement;
import com.github.chrisblutz.trinity.parser.lines.Line;
import com.github.chrisblutz.trinity.parser.lines.LineSet;
import com.github.chrisblutz.trinity.parser.sources.FileSourceEntry;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Christopher Lutz
 */
public class TestUtilities {
    
    public static final File TRINITY_TEST_HOME = new File("test/");
    private static final String TEST_PREFIX = "test_";
    
    public static FileSourceEntry getTestFile(File root, String name) {
        
        try {
            
            return new FileSourceEntry(new File(root, TEST_PREFIX + name + ".ty"));
            
        } catch (IOException e) {
            
            fail("Unable to load test file. (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")", e);
            return null;
        }
    }
    
    public static void checkSingleTokens(LineSet set, Token[] tokens) {
        
        assertEquals(tokens.length, set.size());
        
        for (int i = 0; i < set.size(); i++) {
            
            Line line = set.get(i);
            
            assertEquals(1, line.size());
            assertEquals(tokens[i], line.get(0).getToken());
        }
    }
    
    public static void checkMultipleTokens(LineSet set, Token[][] tokens) {
        
        assertEquals(tokens.length, set.size());
        
        for (int i = 0; i < set.size(); i++) {
            
            Line line = set.get(i);
            SourceToken[] actual = line.toArray(new SourceToken[line.size()]);
            Token[] actualTokens = new Token[actual.length];
            for (int j = 0; j < actual.length; j++) {
                
                actualTokens[j] = actual[j].getToken();
            }
            
            assertArrayEquals(tokens[i], actualTokens);
        }
    }
    
    public static void checkTokenTypeAndContents(LineSet set, Token token, String[] contents) {
        
        assertEquals(contents.length, set.size());
        
        for (int i = 0; i < set.size(); i++) {
            
            Line line = set.get(i);
            assertEquals(1, line.size());
            assertEquals(token, line.get(0).getToken());
            assertEquals(contents[i], line.get(0).getContents());
        }
    }
    
    public static void checkTypes(Block block, Class[] types) {
        
        for (int i = 0; i < block.size(); i++) {
            
            assertEquals(types[i], block.get(i).getClass());
        }
    }
    
    public static void checkStatement(Statement statement, Token[] tokens, String[] tokenStringContents) {
        
        assertEquals(tokens.length, statement.size());
        
        for (int i = 0; i < statement.size(); i++) {
            
            SourceToken token = statement.get(i);
            
            assertEquals(tokens[i], token.getToken());
            
            if (token.getToken() == Token.TOKEN_STRING) {
                
                assertEquals(tokenStringContents[i], token.getContents());
            }
        }
    }
    
    public static void checkBlockHeader(Block block, Token[] tokens, String[] tokenStringContents) {
        
        assertNotNull(block.getHeader());
        assertEquals(tokens.length, block.getHeader().size());
        
        List<SourceToken> header = block.getHeader();
        for (int i = 0; i < header.size(); i++) {
            
            SourceToken token = header.get(i);
            
            assertEquals(tokens[i], token.getToken());
            
            if (token.getToken() == Token.TOKEN_STRING) {
                
                assertEquals(tokenStringContents[i], token.getContents());
            }
        }
    }
}

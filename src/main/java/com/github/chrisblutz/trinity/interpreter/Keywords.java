package com.github.chrisblutz.trinity.interpreter;

import com.github.chrisblutz.trinity.interpreter.components.Keyword;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class Keywords {
    
    private static Map<Token, Keyword> keywords = new HashMap<>();
    
    public static void register(Token token, Keyword keyword) {
        
        keywords.put(token, keyword);
    }
    
    public static boolean isKeyword(Token token) {
        
        return keywords.containsKey(token);
    }
    
    public static Keyword getKeyword(Token token) {
        
        return keywords.get(token);
    }
}

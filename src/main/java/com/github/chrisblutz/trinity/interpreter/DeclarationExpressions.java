package com.github.chrisblutz.trinity.interpreter;

import com.github.chrisblutz.trinity.interpreter.components.DeclarationExpression;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class DeclarationExpressions {
    
    private static Map<Token, DeclarationExpression> expressions = new HashMap<>();
    
    public static void registerDeclarationExpression(Token token, DeclarationExpression expression) {
        
        expressions.put(token, expression);
    }
    
    public static boolean isDeclarationKeyword(Token token) {
        
        return expressions.containsKey(token);
    }
    
    public static DeclarationExpression getExpression(Token token) {
        
        return expressions.get(token);
    }
}

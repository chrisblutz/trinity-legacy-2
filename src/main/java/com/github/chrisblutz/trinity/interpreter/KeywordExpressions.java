package com.github.chrisblutz.trinity.interpreter;

import com.github.chrisblutz.trinity.interpreter.components.KeywordExpression;
import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * @author Christopher Lutz
 */
public class KeywordExpressions {
    
    private static Map<Token, KeywordExpression> expressions = new HashMap<>();
    
    private static Map<Block, Token> previousTokens = new WeakHashMap<>();
    private static Map<Block, InstructionSet> previousSets = new WeakHashMap<>();
    
    public static void registerKeywordExpression(Token token, KeywordExpression expression) {
        
        expressions.put(token, expression);
    }
    
    public static boolean isKeyword(Token token) {
        
        return expressions.containsKey(token);
    }
    
    public static KeywordExpression getExpression(Token token) {
        
        return expressions.get(token);
    }
    
    public static Token getPreviousToken(Block block) {
        
        return previousTokens.get(block);
    }
    
    public static InstructionSet getPreviousSet(Block block) {
        
        return previousSets.get(block);
    }
    
    public static void updatePrevious(Block block, Token token, InstructionSet set) {
        
        previousTokens.put(block, token);
        previousSets.put(block, set);
    }
}

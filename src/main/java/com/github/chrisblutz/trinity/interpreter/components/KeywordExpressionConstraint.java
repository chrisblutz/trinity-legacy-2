package com.github.chrisblutz.trinity.interpreter.components;

import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public abstract class KeywordExpressionConstraint {
    
    private Token[] tokens;
    private String message;
    
    protected KeywordExpressionConstraint(Token[] tokens, String message) {
    
        this.tokens = tokens;
        this.message = message;
    }
    
    public Token[] getTokens() {
        
        return tokens;
    }
    
    public String getMessage() {
        
        return message;
    }
    
    public abstract boolean check(InstructionSet set, InstructionSet previous);
}

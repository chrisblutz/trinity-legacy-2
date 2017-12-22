package com.github.chrisblutz.trinity.interpreter.errors;

/**
 * @author Christopher Lutz
 */
public class TrinitySyntaxError extends RuntimeException {
    
    public TrinitySyntaxError() {
        
        super("SyntaxError");
    }
}

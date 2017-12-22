package com.github.chrisblutz.trinity.interpreter.errors;

import com.github.chrisblutz.trinity.lang.TyObject;


/**
 * @author Christopher Lutz
 */
public class TrinityError extends RuntimeException {
    
    private TyObject errorObject;
    
    public TrinityError(TyObject errorObject) {
        
        super(errorObject.getObjectClass().getFullName());
        this.errorObject = errorObject;
    }
    
    public TyObject getErrorObject() {
        
        return errorObject;
    }
}

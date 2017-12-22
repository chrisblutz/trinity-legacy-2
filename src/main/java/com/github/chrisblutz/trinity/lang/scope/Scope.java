package com.github.chrisblutz.trinity.lang.scope;

import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;


/**
 * @author Christopher Lutz
 */
public enum Scope {
    
    PUBLIC("public"), PROTECTED("protected"), MODULE_PROTECTED("module-protected"), PRIVATE("private");
    
    private String str;
    
    Scope(String str) {
        
        this.str = str;
    }
    
    @Override
    public String toString() {
        
        return str;
    }
    
    public void reportAccessViolation(TyRuntime runtime) {
        
        Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Cannot access value of field marked '" + toString() + "' here.");
    }
    
    public void reportAssignmentViolation(TyRuntime runtime) {
        
        Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Cannot set value of field marked '" + toString() + "' here.");
    }
}

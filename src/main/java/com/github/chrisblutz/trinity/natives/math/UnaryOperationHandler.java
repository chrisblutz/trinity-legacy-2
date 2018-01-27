package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyString;


/**
 * @author Christopher Lutz
 */
public abstract class UnaryOperationHandler {
    
    public abstract TyString toString(TyObject operand);
    
    public abstract TyString toHexString(TyObject operand);
    
    public abstract TyObject bitwiseComplement(TyObject operand);
    
    public void throwOperationUnsupportedError(String operation, TyClass type) {
        
        Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Operation '" + operation + "' not supported on values of type '" + type.getFullName() + "'.");
    }
    
    public void throwDivideByZeroError() {
        
        Errors.throwError(Errors.Classes.ARITHMETIC_ERROR, "/ by 0.");
    }
}

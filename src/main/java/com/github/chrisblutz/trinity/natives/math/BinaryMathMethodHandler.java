package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;


/**
 * @author Christopher Lutz
 */
public abstract class BinaryMathMethodHandler {
    
    public abstract TyObject pow(TyObject operand, TyObject exponent);
    
    public abstract TyObject log(TyObject operand, TyObject base);
    
    public void throwOperationUnsupportedError(String operation, TyClass type) {
        
        Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Operation '" + operation + "' not supported on values of type '" + type.getFullName() + "'.");
    }
    
    public void throwOperationUnsupportedError(String operation, TyClass type1, TyClass type2) {
        
        Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Operation '" + operation + "' not supported on values of type '" + type1.getFullName() + "' and '" + type2.getFullName() + "'.");
    }
    
    public void throwDivideByZeroError() {
        
        Errors.throwError(Errors.Classes.ARITHMETIC_ERROR, "/ by 0.");
    }
}

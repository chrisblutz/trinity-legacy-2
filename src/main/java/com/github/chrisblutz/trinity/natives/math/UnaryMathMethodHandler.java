package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;


/**
 * @author Christopher Lutz
 */
public abstract class UnaryMathMethodHandler {
    
    public abstract TyObject abs(TyObject operand);
    
    public abstract TyObject sqrt(TyObject operand);
    
    public abstract TyObject cbrt(TyObject operand);
    
    public abstract TyObject sin(TyObject operand);
    
    public abstract TyObject cos(TyObject operand);
    
    public abstract TyObject tan(TyObject operand);
    
    public abstract TyObject arcsin(TyObject operand);
    
    public abstract TyObject arccos(TyObject operand);
    
    public abstract TyObject arctan(TyObject operand);
    
    public abstract TyObject ln(TyObject operand);
    
    public abstract TyObject round(TyObject operand);
    
    public abstract TyObject ceil(TyObject operand);
    
    public abstract TyObject floor(TyObject operand);
    
    public void throwOperationUnsupportedError(String operation, TyClass type) {
        
        Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Operation '" + operation + "' not supported on values of type '" + type.getFullName() + "'.");
    }
    
    public void throwDivideByZeroError() {
        
        Errors.throwError(Errors.Classes.ARITHMETIC_ERROR, "/ by 0.");
    }
}

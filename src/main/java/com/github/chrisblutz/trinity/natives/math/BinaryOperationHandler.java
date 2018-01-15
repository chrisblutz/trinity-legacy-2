package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;


/**
 * @author Christopher Lutz
 */
public abstract class BinaryOperationHandler {
    
    public abstract TyObject add(TyObject operand1, TyObject operand2);
    
    public abstract TyObject subtract(TyObject operand1, TyObject operand2);
    
    public abstract TyObject multiply(TyObject operand1, TyObject operand2);
    
    public abstract TyObject divide(TyObject operand1, TyObject operand2);
    
    public abstract TyObject modulus(TyObject operand1, TyObject operand2);
    
    public abstract TyObject shiftLeft(TyObject operand1, TyObject operand2);
    
    public abstract TyObject shiftRight(TyObject operand1, TyObject operand2);
    
    public abstract TyObject shiftLogicalRight(TyObject operand1, TyObject operand2);
    
    public abstract TyObject bitwiseOr(TyObject operand1, TyObject operand2);
    
    public abstract TyObject bitwiseAnd(TyObject operand1, TyObject operand2);
    
    public abstract TyObject bitwiseXor(TyObject operand1, TyObject operand2);
    
    public abstract TyBoolean equals(TyObject operand1, TyObject operand2);
    
    public abstract TyInt compare(TyObject operand1, TyObject operand2);
    
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

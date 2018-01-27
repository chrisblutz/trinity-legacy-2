package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyString;


/**
 * @author Christopher Lutz
 */
public class DefaultUnaryOperationHandler extends UnaryOperationHandler {
    
    private static DefaultUnaryOperationHandler instance = new DefaultUnaryOperationHandler();
    
    private DefaultUnaryOperationHandler() {
    
    }
    
    @Override
    public TyString toString(TyObject operand) {
        
        Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "String conversion not defined for numeric type " + operand.getObjectClass().getFullName() + ".");
        return new TyString("");
    }
    
    @Override
    public TyString toHexString(TyObject operand) {
        
        Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Hexadecimal string conversion not defined for numeric type " + operand.getObjectClass().getFullName() + ".");
        return new TyString("");
    }
    
    @Override
    public TyObject bitwiseComplement(TyObject operand) {
        
        throwOperationUnsupportedError("~", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    public static DefaultUnaryOperationHandler getInstance() {
        
        return instance;
    }
}

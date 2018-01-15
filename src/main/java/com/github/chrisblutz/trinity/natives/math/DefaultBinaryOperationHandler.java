package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;


/**
 * @author Christopher Lutz
 */
public class DefaultBinaryOperationHandler extends BinaryOperationHandler {
    
    private static DefaultBinaryOperationHandler instance = new DefaultBinaryOperationHandler();
    
    private DefaultBinaryOperationHandler() {
    
    }
    
    @Override
    public TyObject add(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("+", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject subtract(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("-", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject multiply(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("*", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject divide(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("/", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject modulus(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("%", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject shiftLeft(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("<<", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject shiftRight(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError(">>", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject shiftLogicalRight(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError(">>>", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject bitwiseOr(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("|", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject bitwiseAnd(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("&", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject bitwiseXor(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("^", operand1.getObjectClass(), operand2.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyBoolean equals(TyObject operand1, TyObject operand2) {
        
        return TyBoolean.FALSE;
    }
    
    @Override
    public TyInt compare(TyObject operand1, TyObject operand2) {
        
        throwOperationUnsupportedError("<=>", operand1.getObjectClass(), operand2.getObjectClass());
        return TrinityMath.ZERO;
    }
    
    public static DefaultBinaryOperationHandler getInstance() {
        
        return instance;
    }
}

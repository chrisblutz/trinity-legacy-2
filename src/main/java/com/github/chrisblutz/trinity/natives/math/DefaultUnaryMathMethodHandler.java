package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyObject;


/**
 * @author Christopher Lutz
 */
public class DefaultUnaryMathMethodHandler extends UnaryMathMethodHandler {
    
    private static DefaultUnaryMathMethodHandler instance = new DefaultUnaryMathMethodHandler();
    
    private DefaultUnaryMathMethodHandler() {
    
    }
    
    @Override
    public TyObject abs(TyObject operand) {
        
        throwOperationUnsupportedError("abs", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject sqrt(TyObject operand) {
        
        throwOperationUnsupportedError("sqrt", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject cbrt(TyObject operand) {
        
        throwOperationUnsupportedError("cbrt", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject sin(TyObject operand) {
        
        throwOperationUnsupportedError("sin", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject cos(TyObject operand) {
        
        throwOperationUnsupportedError("cos", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject tan(TyObject operand) {
        
        throwOperationUnsupportedError("tan", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject sinh(TyObject operand) {
        
        throwOperationUnsupportedError("sinh", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject cosh(TyObject operand) {
        
        throwOperationUnsupportedError("cosh", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject tanh(TyObject operand) {
        
        throwOperationUnsupportedError("tanh", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject arcsin(TyObject operand) {
        
        throwOperationUnsupportedError("arcsin", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject arccos(TyObject operand) {
        
        throwOperationUnsupportedError("arccos", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject arctan(TyObject operand) {
        
        throwOperationUnsupportedError("arctan", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject ln(TyObject operand) {
        
        throwOperationUnsupportedError("ln", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject round(TyObject operand) {
        
        throwOperationUnsupportedError("round", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject ceil(TyObject operand) {
        
        throwOperationUnsupportedError("ceil", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject floor(TyObject operand) {
        
        throwOperationUnsupportedError("floor", operand.getObjectClass());
        return TyObject.NIL;
    }
    
    public static DefaultUnaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

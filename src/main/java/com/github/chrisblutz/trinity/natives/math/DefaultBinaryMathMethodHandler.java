package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyObject;


/**
 * @author Christopher Lutz
 */
public class DefaultBinaryMathMethodHandler extends BinaryMathMethodHandler {
    
    private static DefaultBinaryMathMethodHandler instance = new DefaultBinaryMathMethodHandler();
    
    private DefaultBinaryMathMethodHandler() {
    
    }
    
    @Override
    public TyObject pow(TyObject operand, TyObject exponent) {
        
        throwOperationUnsupportedError("pow", operand.getObjectClass(), exponent.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject log(TyObject operand, TyObject base) {
        
        throwOperationUnsupportedError("log", operand.getObjectClass(), base.getObjectClass());
        return TyObject.NIL;
    }
    
    @Override
    public TyObject arctan2(TyObject x, TyObject y) {
        
        throwOperationUnsupportedError("arctan2", x.getObjectClass(), y.getObjectClass());
        return TyObject.NIL;
    }
    
    public static DefaultBinaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

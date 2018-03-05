package com.github.chrisblutz.trinity.natives.math.defaults.real;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.math.UnaryMathMethodHandler;


/**
 * @author Christopher Lutz
 */
public class RealTypesUnaryMathMethodHandler extends UnaryMathMethodHandler {
    
    private static RealTypesUnaryMathMethodHandler instance = new RealTypesUnaryMathMethodHandler();
    
    private RealTypesUnaryMathMethodHandler() {
    
    }
    
    @Override
    public TyObject abs(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.abs(toDouble(operand)));
    }
    
    @Override
    public TyObject sqrt(TyObject operand) {
        
        double opDouble = toDouble(operand);
        
        if (opDouble >= 0) {
            
            return NativeConversion.wrapNumber(Math.sqrt(opDouble));
            
        } else {
            
            Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Complex numbers are not currently supported.");
            return TyObject.NIL;
        }
    }
    
    @Override
    public TyObject cbrt(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.cbrt(toDouble(operand)));
    }
    
    @Override
    public TyObject sin(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.sin(toDouble(operand)));
    }
    
    @Override
    public TyObject cos(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.cos(toDouble(operand)));
    }
    
    @Override
    public TyObject tan(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.tan(toDouble(operand)));
    }
    
    @Override
    public TyObject sinh(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.sinh(toDouble(operand)));
    }
    
    @Override
    public TyObject cosh(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.cosh(toDouble(operand)));
    }
    
    @Override
    public TyObject tanh(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.tanh(toDouble(operand)));
    }
    
    @Override
    public TyObject arcsin(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.asin(toDouble(operand)));
    }
    
    @Override
    public TyObject arccos(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.acos(toDouble(operand)));
    }
    
    @Override
    public TyObject arctan(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.atan(toDouble(operand)));
    }
    
    @Override
    public TyObject ln(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.log(toDouble(operand)));
    }
    
    @Override
    public TyObject round(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.round(toDouble(operand)));
    }
    
    @Override
    public TyObject ceil(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.ceil(toDouble(operand)));
    }
    
    @Override
    public TyObject floor(TyObject operand) {
        
        return NativeConversion.wrapNumber(Math.floor(toDouble(operand)));
    }
    
    private double toDouble(TyObject object) {
        
        return NativeConversion.asNumber(object);
    }
    
    public static RealTypesUnaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

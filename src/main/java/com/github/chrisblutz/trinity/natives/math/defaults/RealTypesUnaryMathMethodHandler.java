package com.github.chrisblutz.trinity.natives.math.defaults;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
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
        
        return TrinityNatives.wrapNumber(Math.abs(toDouble(operand)));
    }
    
    @Override
    public TyObject sqrt(TyObject operand) {
        
        double opDouble = toDouble(operand);
        
        if (opDouble >= 0) {
            
            return TrinityNatives.wrapNumber(Math.sqrt(toDouble(operand)));
            
        } else {
            
            // TODO Complex numbers
            Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, "Complex numbers are not currently supported.");
            return TyObject.NIL;
        }
    }
    
    @Override
    public TyObject cbrt(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.cbrt(toDouble(operand)));
    }
    
    @Override
    public TyObject sin(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.sin(toDouble(operand)));
    }
    
    @Override
    public TyObject cos(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.cos(toDouble(operand)));
    }
    
    @Override
    public TyObject tan(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.tan(toDouble(operand)));
    }
    
    @Override
    public TyObject arcsin(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.asin(toDouble(operand)));
    }
    
    @Override
    public TyObject arccos(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.acos(toDouble(operand)));
    }
    
    @Override
    public TyObject arctan(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.atan(toDouble(operand)));
    }
    
    @Override
    public TyObject ln(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.log(toDouble(operand)));
    }
    
    @Override
    public TyObject round(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.round(toDouble(operand)));
    }
    
    @Override
    public TyObject ceil(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.ceil(toDouble(operand)));
    }
    
    @Override
    public TyObject floor(TyObject operand) {
        
        return TrinityNatives.wrapNumber(Math.floor(toDouble(operand)));
    }
    
    private double toDouble(TyObject object) {
        
        return TrinityNatives.asNumber(object);
    }
    
    public static RealTypesUnaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

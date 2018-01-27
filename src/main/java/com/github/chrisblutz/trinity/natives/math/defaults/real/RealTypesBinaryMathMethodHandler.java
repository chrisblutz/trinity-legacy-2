package com.github.chrisblutz.trinity.natives.math.defaults.real;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.natives.math.BinaryMathMethodHandler;
import com.github.chrisblutz.trinity.natives.math.TrinityMath;


/**
 * @author Christopher Lutz
 */
public class RealTypesBinaryMathMethodHandler extends BinaryMathMethodHandler {
    
    private static RealTypesBinaryMathMethodHandler instance = new RealTypesBinaryMathMethodHandler();
    
    private RealTypesBinaryMathMethodHandler() {
    
    }
    
    @Override
    public TyObject pow(TyObject operand, TyObject exponent) {
        
        return TrinityNatives.wrapNumber(Math.pow(toDouble(operand), toDouble(exponent)));
    }
    
    @Override
    public TyObject log(TyObject operand, TyObject base) {
        
        return TrinityMath.divide(TrinityMath.ln(operand), TrinityMath.ln(base));
    }
    
    @Override
    public TyObject arctan2(TyObject y, TyObject x) {
        
        return TrinityNatives.wrapNumber(Math.atan2(toDouble(y), toDouble(x)));
    }
    
    private double toDouble(TyObject object) {
        
        return TrinityNatives.asNumber(object);
    }
    
    public static RealTypesBinaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

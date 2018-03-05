package com.github.chrisblutz.trinity.natives.math.defaults.real;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeMath;
import com.github.chrisblutz.trinity.natives.math.BinaryMathMethodHandler;


/**
 * @author Christopher Lutz
 */
public class RealTypesBinaryMathMethodHandler extends BinaryMathMethodHandler {
    
    private static RealTypesBinaryMathMethodHandler instance = new RealTypesBinaryMathMethodHandler();
    
    private RealTypesBinaryMathMethodHandler() {
    
    }
    
    @Override
    public TyObject pow(TyObject operand, TyObject exponent) {
        
        return NativeConversion.wrapNumber(Math.pow(toDouble(operand), toDouble(exponent)));
    }
    
    @Override
    public TyObject log(TyObject operand, TyObject base) {
        
        return NativeMath.divide(NativeMath.ln(operand), NativeMath.ln(base));
    }
    
    @Override
    public TyObject arctan2(TyObject y, TyObject x) {
        
        return NativeConversion.wrapNumber(Math.atan2(toDouble(y), toDouble(x)));
    }
    
    private double toDouble(TyObject object) {
        
        return NativeConversion.asNumber(object);
    }
    
    public static RealTypesBinaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

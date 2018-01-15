package com.github.chrisblutz.trinity.natives.math.defaults;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
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
        
        return TrinityNatives.wrapNumber(Math.pow(toDouble(operand), toDouble(exponent)));
    }
    
    @Override
    public TyObject log(TyObject operand, TyObject base) {
        
        double opDouble = toDouble(operand);
        double baseDouble = toDouble(base);
        
        if (baseDouble == 10) {
            
            return TrinityNatives.wrapNumber(Math.log10(opDouble));
            
        } else {
            
            return TrinityNatives.wrapNumber(Math.log10(opDouble) / Math.log10(baseDouble));
        }
    }
    
    private double toDouble(TyObject object) {
        
        return TrinityNatives.asNumber(object);
    }
    
    public static RealTypesBinaryMathMethodHandler getInstance() {
        
        return instance;
    }
}

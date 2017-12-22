package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeFloat {
    
    private static final String CLASS = TrinityNatives.Classes.FLOAT;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerField(CLASS, "NaN", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Double.NaN));
        TrinityNatives.registerField(CLASS, "POSITIVE_INFINITY", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Double.POSITIVE_INFINITY));
        TrinityNatives.registerField(CLASS, "NEGATIVE_INFINITY", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Double.NEGATIVE_INFINITY));
        TrinityNatives.registerField(CLASS, "MIN_VALUE", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Double.MIN_VALUE));
        TrinityNatives.registerField(CLASS, "MAX_VALUE", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Double.MAX_VALUE));
        
        TrinityNatives.registerMethod(CLASS, "isNaN", (runtime, thisObj, params) -> {
            
            double thisDouble = TrinityNatives.toFloat(thisObj);
            return TyBoolean.valueFor(Double.isNaN(thisDouble));
        });
        TrinityNatives.registerMethod(CLASS, "isFinite", (runtime, thisObj, params) -> {
            
            double thisDouble = TrinityNatives.toFloat(thisObj);
            return TyBoolean.valueFor(Double.isFinite(thisDouble));
        });
        TrinityNatives.registerMethod(CLASS, "isInfinite", (runtime, thisObj, params) -> {
            
            double thisDouble = TrinityNatives.toFloat(thisObj);
            return TyBoolean.valueFor(Double.isInfinite(thisDouble));
        });
    }
}

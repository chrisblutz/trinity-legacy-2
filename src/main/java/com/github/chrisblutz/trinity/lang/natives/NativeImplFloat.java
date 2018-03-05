package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplFloat {
    
    private static final String CLASS = NativeReferences.Classes.FLOAT;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerField(CLASS, "NaN", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Double.NaN));
        NativeInvocation.registerField(CLASS, "POSITIVE_INFINITY", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Double.POSITIVE_INFINITY));
        NativeInvocation.registerField(CLASS, "NEGATIVE_INFINITY", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Double.NEGATIVE_INFINITY));
        NativeInvocation.registerField(CLASS, "MIN_VALUE", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Double.MIN_VALUE));
        NativeInvocation.registerField(CLASS, "MAX_VALUE", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Double.MAX_VALUE));
        
        NativeInvocation.registerMethod(CLASS, "isNaN", (runtime, thisObj, params) -> {
            
            double thisDouble = NativeConversion.toFloat(thisObj);
            return TyBoolean.valueFor(Double.isNaN(thisDouble));
        });
        NativeInvocation.registerMethod(CLASS, "isFinite", (runtime, thisObj, params) -> {
            
            double thisDouble = NativeConversion.toFloat(thisObj);
            return TyBoolean.valueFor(Double.isFinite(thisDouble));
        });
        NativeInvocation.registerMethod(CLASS, "isInfinite", (runtime, thisObj, params) -> {
            
            double thisDouble = NativeConversion.toFloat(thisObj);
            return TyBoolean.valueFor(Double.isInfinite(thisDouble));
        });
    }
}

package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplInt {
    
    private static final String CLASS = NativeReferences.Classes.INT;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerField(CLASS, "MIN_VALUE", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Integer.MIN_VALUE));
        NativeInvocation.registerField(CLASS, "MAX_VALUE", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Integer.MAX_VALUE));
    }
}

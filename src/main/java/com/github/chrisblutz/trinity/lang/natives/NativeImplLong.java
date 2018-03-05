package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplLong {
    
    private static final String CLASS = NativeReferences.Classes.LONG;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerField(CLASS, "MIN_VALUE", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Long.MIN_VALUE));
        NativeInvocation.registerField(CLASS, "MAX_VALUE", (runtime, thisObj, params) -> NativeConversion.wrapNumber(Long.MAX_VALUE));
    }
}

package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeInt {
    
    private static final String CLASS = TrinityNatives.Classes.INT;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerField(CLASS, "MIN_VALUE", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Integer.MIN_VALUE));
        TrinityNatives.registerField(CLASS, "MAX_VALUE", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Integer.MAX_VALUE));
    }
}

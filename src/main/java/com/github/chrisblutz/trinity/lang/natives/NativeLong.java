package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeLong {
    
    private static final String CLASS = TrinityNatives.Classes.LONG;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerField(CLASS, "MIN_VALUE", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Long.MIN_VALUE));
        TrinityNatives.registerField(CLASS, "MAX_VALUE", (runtime, thisObj, params) -> TrinityNatives.wrapNumber(Long.MAX_VALUE));
    }
}

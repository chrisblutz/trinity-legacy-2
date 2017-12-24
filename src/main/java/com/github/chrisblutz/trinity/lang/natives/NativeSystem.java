package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.types.TyLong;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.properties.TrinityProperties;


/**
 * @author Christopher Lutz
 */
public class NativeSystem {
    
    private static final String CLASS = TrinityNatives.Classes.SYSTEM;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "loadProperties", (runtime, thisObj, params) -> TrinityProperties.loadProperties());
        TrinityNatives.registerMethod(CLASS, "currentTimeMillis", (runtime, thisObj, params) -> new TyLong(System.currentTimeMillis()));
    }
}

package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.types.TyClassObject;
import com.github.chrisblutz.trinity.natives.NativeStorage;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeClass {
    
    private static final String CLASS = TrinityNatives.Classes.CLASS;
    
    @NativeHook(CLASS)
    public static void register() {
        
        
        TrinityNatives.registerMethod(CLASS, "getName", (runtime, thisObj, args) -> NativeStorage.getClassName(TrinityNatives.cast(TyClassObject.class, thisObj).getInternal()));
        TrinityNatives.registerMethod(CLASS, "getSimpleName", (runtime, thisObj, args) -> NativeStorage.getClassSimpleName(TrinityNatives.cast(TyClassObject.class, thisObj).getInternal()));
    }
}

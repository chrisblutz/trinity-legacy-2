package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.types.TyClassObject;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;
import com.github.chrisblutz.trinity.natives.NativeStorage;


/**
 * @author Christopher Lutz
 */
public class NativeImplClass {
    
    private static final String CLASS = NativeReferences.Classes.CLASS;
    
    @NativeHook(CLASS)
    public static void register() {
        
        
        NativeInvocation.registerMethod(CLASS, "getName", (runtime, thisObj, args) -> NativeStorage.getClassName(NativeConversion.cast(TyClassObject.class, thisObj).getInternal()));
        NativeInvocation.registerMethod(CLASS, "getSimpleName", (runtime, thisObj, args) -> NativeStorage.getClassSimpleName(NativeConversion.cast(TyClassObject.class, thisObj).getInternal()));
    }
}

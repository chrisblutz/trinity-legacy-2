package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.types.TyLong;
import com.github.chrisblutz.trinity.lang.types.TyMap;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;
import com.github.chrisblutz.trinity.properties.TrinityProperties;


/**
 * @author Christopher Lutz
 */
public class NativeImplSystem {
    
    private static final String CLASS = NativeReferences.Classes.SYSTEM;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "loadProperties", (runtime, thisObj, params) -> TrinityProperties.loadProperties());
        NativeInvocation.registerMethod(CLASS, "getFullEnvironment", (runtime, thisObj, args) -> getEnvironmentMap());
        NativeInvocation.registerMethod(CLASS, "currentTimeMillis", (runtime, thisObj, params) -> new TyLong(System.currentTimeMillis()));
    }
    
    private static TyMap environmentMap = null;
    
    private static TyMap getEnvironmentMap() {
        
        if (environmentMap == null) {
            
            environmentMap = NativeConversion.getMapFor(System.getenv(), TyMap.getFastStorage());
        }
        
        return environmentMap;
    }
}

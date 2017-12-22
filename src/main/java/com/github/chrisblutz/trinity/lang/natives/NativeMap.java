package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyMap;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class NativeMap {
    
    private static final String CLASS = TrinityNatives.Classes.MAP;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerForNativeConstruction(CLASS);
        
        TrinityNatives.registerMethod(CLASS, "initialize", (runtime, thisObj, params) -> {
            
            int storageType = TrinityNatives.toInt(runtime.getVariable("storageType"));
            return new TyMap(TyMap.getMapForStorageType(storageType), storageType);
        });
        TrinityNatives.registerMethod(CLASS, "length", (runtime, thisObj, params) -> new TyInt(TrinityNatives.cast(TyMap.class, thisObj).size()));
        TrinityNatives.registerMethod(CLASS, "keys", (runtime, thisObj, params) -> new TyArray(new ArrayList<>(TrinityNatives.cast(TyMap.class, thisObj).getInternal().keySet())));
        TrinityNatives.registerMethod(CLASS, "values", (runtime, thisObj, params) -> new TyArray(new ArrayList<>(TrinityNatives.cast(TyMap.class, thisObj).getInternal().values())));
        TrinityNatives.registerMethod(CLASS, "put", (runtime, thisObj, params) -> put(TrinityNatives.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime.getVariable("value"), runtime));
        TrinityNatives.registerMethod(CLASS, "remove", (runtime, thisObj, params) -> remove(TrinityNatives.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime));
        TrinityNatives.registerMethod(CLASS, "clear", (runtime, thisObj, params) -> {
            
            TrinityNatives.cast(TyMap.class, thisObj).getInternal().clear();
            
            return thisObj;
        });
        TrinityNatives.registerMethod(CLASS, "[]", (runtime, thisObj, params) -> get(TrinityNatives.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime.getVariable("defaultValue"), runtime));
        TrinityNatives.registerMethod(CLASS, "[]=", (runtime, thisObj, params) -> put(TrinityNatives.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime.getVariable("value"), runtime));
        TrinityNatives.registerMethod(CLASS, "getStorageType", (runtime, thisObj, params) -> new TyInt(TrinityNatives.cast(TyMap.class, thisObj).getStorageType()));
        TrinityNatives.registerMethod(CLASS, "swapStorageType", (runtime, thisObj, params) -> {
            
            int storageType = TrinityNatives.toInt(runtime.getVariable("storageType"));
            TrinityNatives.cast(TyMap.class, thisObj).setStorageType(storageType);
            
            return thisObj;
        });
    }
    
    private static TyObject get(TyMap tyMap, TyObject obj, TyObject def, TyRuntime runtime) {
        
        Map<TyObject, TyObject> map = tyMap.getInternal();
        
        for (TyObject key : map.keySet()) {
            
            boolean equal = TrinityNatives.toBoolean(key.tyInvoke("==", runtime, null, null, obj));
            
            if (equal) {
                
                return map.get(key);
            }
        }
        
        return def;
    }
    
    private static TyObject put(TyMap tyMap, TyObject obj, TyObject value, TyRuntime runtime) {
        
        Map<TyObject, TyObject> map = tyMap.getInternal();
        
        boolean exists = false;
        for (TyObject key : map.keySet()) {
            
            boolean equal = TrinityNatives.toBoolean(key.tyInvoke("==", runtime, null, null, obj));
            
            if (equal) {
                
                exists = true;
                map.put(key, value);
            }
        }
        
        if (!exists) {
            
            return map.put(obj, value);
        }
        
        return TyObject.NIL;
    }
    
    private static TyObject remove(TyMap tyMap, TyObject obj, TyRuntime runtime) {
        
        Map<TyObject, TyObject> map = tyMap.getInternal();
        
        for (TyObject key : map.keySet()) {
            
            boolean equal = TrinityNatives.toBoolean(key.tyInvoke("==", runtime, null, null, obj));
            
            if (equal) {
                
                return map.remove(key);
            }
        }
        
        return TyObject.NIL;
    }
}

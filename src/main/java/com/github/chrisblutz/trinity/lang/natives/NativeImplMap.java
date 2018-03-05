package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyMap;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.util.ArrayList;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class NativeImplMap {
    
    private static final String CLASS = NativeReferences.Classes.MAP;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerForNativeConstruction(CLASS);
        
        NativeInvocation.registerMethod(CLASS, "initialize", (runtime, thisObj, params) -> {
            
            int storageType = NativeConversion.toInt(runtime.getVariable("storageType"));
            return new TyMap(TyMap.getMapForStorageType(storageType), storageType);
        });
        NativeInvocation.registerMethod(CLASS, "length", (runtime, thisObj, params) -> new TyInt(NativeConversion.cast(TyMap.class, thisObj).size()));
        NativeInvocation.registerMethod(CLASS, "keys", (runtime, thisObj, params) -> new TyArray(new ArrayList<>(NativeConversion.cast(TyMap.class, thisObj).getInternal().keySet())));
        NativeInvocation.registerMethod(CLASS, "values", (runtime, thisObj, params) -> new TyArray(new ArrayList<>(NativeConversion.cast(TyMap.class, thisObj).getInternal().values())));
        NativeInvocation.registerMethod(CLASS, "put", (runtime, thisObj, params) -> put(NativeConversion.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime.getVariable("value"), runtime));
        NativeInvocation.registerMethod(CLASS, "remove", (runtime, thisObj, params) -> remove(NativeConversion.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime));
        NativeInvocation.registerMethod(CLASS, "clear", (runtime, thisObj, params) -> {
            
            NativeConversion.cast(TyMap.class, thisObj).getInternal().clear();
            
            return thisObj;
        });
        NativeInvocation.registerMethod(CLASS, "[]", (runtime, thisObj, params) -> get(NativeConversion.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime.getVariable("defaultValue"), runtime));
        NativeInvocation.registerMethod(CLASS, "[]=", (runtime, thisObj, params) -> put(NativeConversion.cast(TyMap.class, thisObj), runtime.getVariable("key"), runtime.getVariable("value"), runtime));
        NativeInvocation.registerMethod(CLASS, "getStorageType", (runtime, thisObj, params) -> new TyInt(NativeConversion.cast(TyMap.class, thisObj).getStorageType()));
        NativeInvocation.registerMethod(CLASS, "swapStorageType", (runtime, thisObj, params) -> {
            
            int storageType = NativeConversion.toInt(runtime.getVariable("storageType"));
            NativeConversion.cast(TyMap.class, thisObj).setStorageType(storageType);
            
            return thisObj;
        });
    }
    
    private static TyObject get(TyMap tyMap, TyObject obj, TyObject def, TyRuntime runtime) {
        
        Map<TyObject, TyObject> map = tyMap.getInternal();
        
        for (TyObject key : map.keySet()) {
            
            boolean equal = NativeConversion.toBoolean(key.tyInvoke("==", runtime, null, null, obj));
            
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
            
            boolean equal = NativeConversion.toBoolean(key.tyInvoke("==", runtime, null, null, obj));
            
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
            
            boolean equal = NativeConversion.toBoolean(key.tyInvoke("==", runtime, null, null, obj));
            
            if (equal) {
                
                return map.remove(key);
            }
        }
        
        return TyObject.NIL;
    }
}

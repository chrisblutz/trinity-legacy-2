package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;


/**
 * @author Christopher Lutz
 */
public class NativeArray {
    
    private static final String CLASS = TrinityNatives.Classes.ARRAY;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "[]", (runtime, thisObj, args) -> {
            
            int index = TrinityNatives.toInt(runtime.getVariable("index"));
            TyArray array = TrinityNatives.cast(TyArray.class, thisObj);
            
            if (index >= array.size() || index < 0) {
                
                Errors.throwError(Errors.Classes.ARGUMENT_ERROR, runtime, "Index out of bounds.  Index: " + index + ", Size: " + array.size());
            }
            
            return array.getInternal().get(index);
        });
        TrinityNatives.registerMethod(CLASS, "[]=", (runtime, thisObj, args) -> {
            
            int index = TrinityNatives.toInt(runtime.getVariable("index"));
            TyObject value = runtime.getVariable("value");
            TyArray array = TrinityNatives.cast(TyArray.class, thisObj);
            
            if (index >= array.size() || index < 0) {
                
                Errors.throwError(Errors.Classes.ARGUMENT_ERROR, runtime, "Index out of bounds.  Index: " + index + ", Size: " + array.size());
            }
            
            return array.getInternal().set(index, value);
        });
        TrinityNatives.registerMethod(CLASS, "add", (runtime, thisObj, params) -> {
            
            TyArray thisArray = TrinityNatives.cast(TyArray.class, thisObj);
            thisArray.getInternal().add(runtime.getVariable("value"));
            
            return TyObject.NIL;
        });
        TrinityNatives.registerMethod(CLASS, "insert", (runtime, thisObj, params) -> {
            
            TyArray thisArray = TrinityNatives.cast(TyArray.class, thisObj);
            thisArray.getInternal().add(TrinityNatives.toInt(runtime.getVariable("index")), runtime.getVariable("value"));
            
            return TyObject.NIL;
        });
        TrinityNatives.registerMethod(CLASS, "remove", (runtime, thisObj, params) -> {
            
            TyArray thisArray = TrinityNatives.cast(TyArray.class, thisObj);
            
            return thisArray.getInternal().remove(TrinityNatives.toInt(runtime.getVariable("index")));
        });
        TrinityNatives.registerMethod(CLASS, "clear", (runtime, thisObj, params) -> {
            
            TrinityNatives.cast(TyArray.class, thisObj).getInternal().clear();
            
            return TyObject.NIL;
        });
        TrinityNatives.registerMethod(CLASS, "length", (runtime, thisObj, args) -> new TyInt(TrinityNatives.cast(TyArray.class, thisObj).size()));
        
        TrinityNatives.registerMethod(CLASS, "copyOf", (runtime, thisObj, params) -> {
            
            TyArray thisArray = TrinityNatives.cast(TyArray.class, runtime.getVariable("array"));
            return new TyArray(new ArrayList<>(thisArray.getInternal()));
        });
    }
}

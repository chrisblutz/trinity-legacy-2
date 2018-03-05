package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.util.ArrayList;


/**
 * @author Christopher Lutz
 */
public class NativeImplArray {
    
    private static final String CLASS = NativeReferences.Classes.ARRAY;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "[]", (runtime, thisObj, args) -> {
            
            int index = NativeConversion.toInt(runtime.getVariable("index"));
            TyArray array = NativeConversion.cast(TyArray.class, thisObj);
            
            if (index >= array.size() || index < 0) {
                
                Errors.throwError(Errors.Classes.ARGUMENT_ERROR, runtime, "Index out of bounds.  Index: " + index + ", Size: " + array.size());
            }
            
            return array.getInternal().get(index);
        });
        NativeInvocation.registerMethod(CLASS, "[]=", (runtime, thisObj, args) -> {
            
            int index = NativeConversion.toInt(runtime.getVariable("index"));
            TyObject value = runtime.getVariable("value");
            TyArray array = NativeConversion.cast(TyArray.class, thisObj);
            
            if (index >= array.size() || index < 0) {
                
                Errors.throwError(Errors.Classes.ARGUMENT_ERROR, runtime, "Index out of bounds.  Index: " + index + ", Size: " + array.size());
            }
            
            return array.getInternal().set(index, value);
        });
        NativeInvocation.registerMethod(CLASS, "add", (runtime, thisObj, params) -> {
            
            TyArray thisArray = NativeConversion.cast(TyArray.class, thisObj);
            thisArray.getInternal().add(runtime.getVariable("value"));
            
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "insert", (runtime, thisObj, params) -> {
            
            TyArray thisArray = NativeConversion.cast(TyArray.class, thisObj);
            thisArray.getInternal().add(NativeConversion.toInt(runtime.getVariable("index")), runtime.getVariable("value"));
            
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "remove", (runtime, thisObj, params) -> {
            
            TyArray thisArray = NativeConversion.cast(TyArray.class, thisObj);
            
            return thisArray.getInternal().remove(NativeConversion.toInt(runtime.getVariable("index")));
        });
        NativeInvocation.registerMethod(CLASS, "clear", (runtime, thisObj, params) -> {
            
            NativeConversion.cast(TyArray.class, thisObj).getInternal().clear();
            
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "length", (runtime, thisObj, args) -> new TyInt(NativeConversion.cast(TyArray.class, thisObj).size()));
        
        NativeInvocation.registerMethod(CLASS, "copyOf", (runtime, thisObj, params) -> {
            
            TyArray thisArray = NativeConversion.cast(TyArray.class, runtime.getVariable("array"));
            return new TyArray(new ArrayList<>(thisArray.getInternal()));
        });
    }
}

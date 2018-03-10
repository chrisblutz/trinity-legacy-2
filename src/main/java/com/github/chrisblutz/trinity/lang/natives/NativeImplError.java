package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author Christopher Lutz
 */
public class NativeImplError {
    
    private static final String CLASS = NativeReferences.Classes.ERROR;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "populateStackTrace", (runtime, thisObj, args) -> {
            
            TyArray array = NativeConversion.cast(TyArray.class, NativeInvocation.call(NativeReferences.Classes.KERNEL, "caller", runtime, TyObject.NONE));
            for (int i = 0; i < 2 + thisObj.getSuperLevel(); i++) {
                
                array.getInternal().remove(0);
            }
            
            return array;
        });
    }
    
    private static boolean hintsLoaded = false;
    
    @NativeHook(Errors.Classes.NOT_FOUND_ERROR)
    public static void registerNotFoundError() {
        
        NativeInvocation.registerMethod(Errors.Classes.NOT_FOUND_ERROR, "loadHints", (runtime, thisObj, args) -> {
            
            if (!hintsLoaded) {
                
                NativeReferences.loadHints();
            }
            
            return TyObject.NIL;
        });
    }
}

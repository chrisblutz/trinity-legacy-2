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
    
    @NativeHook(Errors.Classes.NOT_FOUND_ERROR)
    public static void registerNotFoundError() {
        
        NativeInvocation.registerMethod(Errors.Classes.NOT_FOUND_ERROR, "getHints", (runtime, thisObj, args) -> {
            
            String name = NativeConversion.toString(runtime.getVariable("name"), runtime);
            List<NativeReferences.HintReference> references = NativeReferences.getHintReferencesForName(name);
            if (references != null) {
                
                String[][] strReferences = new String[references.size()][2];
                for (int i = 0; i < references.size(); i++) {
                    
                    NativeReferences.HintReference reference = references.get(i);
                    
                    strReferences[i] = new String[]{reference.getReference(), reference.getRequire()};
                }
                
                return NativeConversion.getArrayFor(strReferences);
                
            } else {
                
                return TyObject.NIL;
            }
        });
    }
}

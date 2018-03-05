package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplBoolean {
    
    private static final String CLASS = NativeReferences.Classes.BOOLEAN;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerField(CLASS, "TRUE", (runtime, thisObj, args) -> TyBoolean.TRUE);
        NativeInvocation.registerField(CLASS, "FALSE", (runtime, thisObj, args) -> TyBoolean.FALSE);
        
        NativeInvocation.registerMethod(CLASS, "==", (runtime, thisObj, params) -> {
            
            TyObject object = runtime.getVariable("a");
            
            if (object instanceof TyBoolean) {
                
                return TyBoolean.valueFor(NativeConversion.cast(TyBoolean.class, thisObj).getInternal() == ((TyBoolean) object).getInternal());
            }
            
            return TyBoolean.FALSE;
        });
    }
}

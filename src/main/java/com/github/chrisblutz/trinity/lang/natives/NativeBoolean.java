package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeBoolean {
    
    private static final String CLASS = TrinityNatives.Classes.BOOLEAN;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerField(CLASS, "TRUE", (runtime, thisObj, args) -> TyBoolean.TRUE);
        TrinityNatives.registerField(CLASS, "FALSE", (runtime, thisObj, args) -> TyBoolean.FALSE);
        
        TrinityNatives.registerMethod(CLASS, "==", (runtime, thisObj, params) -> {
            
            TyObject object = runtime.getVariable("a");
            
            if (object instanceof TyBoolean) {
                
                return TyBoolean.valueFor(TrinityNatives.cast(TyBoolean.class, thisObj).getInternal() == ((TyBoolean) object).getInternal());
            }
            
            return TyBoolean.FALSE;
        });
    }
}

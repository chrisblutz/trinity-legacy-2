package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeError {
    
    private static final String CLASS = TrinityNatives.Classes.ERROR;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "populateStackTrace", (runtime, thisObj, args) -> {
            
            TyArray array = TrinityNatives.cast(TyArray.class, TrinityNatives.call(TrinityNatives.Classes.KERNEL, "caller", runtime, TyObject.NONE));
            for (int i = 0; i < 2 + thisObj.getSuperLevel(); i++) {
                
                array.getInternal().remove(0);
            }
            
            return array;
        });
    }
}

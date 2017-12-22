package com.github.chrisblutz.trinity.utils;

import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class ArrayUtils {
    
    public static boolean isSolid(TyArray array, TyRuntime runtime) {
        
        return TrinityNatives.toBoolean(array.tyInvoke("isSolid", runtime, null, null));
    }
}

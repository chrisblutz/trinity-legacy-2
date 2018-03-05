package com.github.chrisblutz.trinity.utils;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.natives.NativeConversion;

import java.util.Arrays;


/**
 * @author Christopher Lutz
 */
public class ArrayUtils {
    
    public static boolean isSolid(TyArray array, TyRuntime runtime) {
        
        return NativeConversion.toBoolean(array.tyInvoke("isSolid", runtime, null, null));
    }
    
    public static TyClass[] combine(TyClass[] array, TyClass[]... arrays) {
        
        int size = array.length;
        for (TyClass[] other : arrays) {
            
            size += other.length;
        }
        
        TyClass[] newArray = Arrays.copyOf(array, size);
        
        int i = array.length;
        for (TyClass[] other : arrays) {
            
            System.arraycopy(other, 0, newArray, i, other.length);
            i += other.length;
        }
        
        return newArray;
    }
}

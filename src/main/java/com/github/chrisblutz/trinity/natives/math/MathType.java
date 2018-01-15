package com.github.chrisblutz.trinity.natives.math;

import com.github.chrisblutz.trinity.lang.TyClass;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class MathType {
    
    private static Map<TyClass, MathType> types = new HashMap<>();
    
    public MathType(TyClass... encompassingClasses) {
        
        for (TyClass encompassing : encompassingClasses) {
            
            types.put(encompassing, this);
        }
    }
    
    public static MathType forType(TyClass tyClass) {
        
        return types.get(tyClass);
    }
}

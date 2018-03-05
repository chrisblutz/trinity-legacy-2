package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class TyInt extends TyObject {
    
    public static final TyInt NEGATIVE_ONE = new TyInt(-1);
    
    private int internal;
    
    public TyInt(int internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.INT, true));
        
        this.internal = internal;
    }
    
    public int getInternal() {
        
        return internal;
    }
}

package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class TyFloat extends TyObject {
    
    private double internal;
    
    public TyFloat(double internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.FLOAT, true));
        
        this.internal = internal;
    }
    
    public double getInternal() {
        
        return internal;
    }
}

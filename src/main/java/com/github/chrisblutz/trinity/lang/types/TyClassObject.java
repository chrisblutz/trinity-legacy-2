package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class TyClassObject extends TyObject {
    
    private TyClass internal;
    
    public TyClassObject(TyClass internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.CLASS, true));
        
        this.internal = internal;
    }
    
    public TyClass getInternal() {
        
        return internal;
    }
}

package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class TyMethodObject extends TyObject {
    
    private TyMethod internal;
    
    public TyMethodObject(TyMethod internal) {
        
        super(ClassRegistry.forName(TrinityNatives.Classes.METHOD, true));
        
        this.internal = internal;
    }
    
    public TyMethod getInternal() {
        
        return internal;
    }
}

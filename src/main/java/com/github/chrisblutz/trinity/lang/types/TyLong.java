package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class TyLong extends TyObject {
    
    private long internal;
    
    public TyLong(long internal) {
        
        super(ClassRegistry.forName(TrinityNatives.Classes.LONG, true));
        
        this.internal = internal;
    }
    
    public long getInternal() {
        
        return internal;
    }
}

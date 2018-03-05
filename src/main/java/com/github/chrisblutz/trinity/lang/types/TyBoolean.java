package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class TyBoolean extends TyObject {
    
    public static final TyBoolean TRUE = new TyBoolean(true), FALSE = new TyBoolean(false);
    
    private boolean internal;
    
    private TyBoolean(boolean internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.BOOLEAN, true));
        
        this.internal = internal;
    }
    
    public boolean getInternal() {
        
        return internal;
    }
    
    public static TyBoolean valueFor(boolean b) {
        
        if (b) {
            
            return TRUE;
            
        } else {
            
            return FALSE;
        }
    }
}

package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.List;


/**
 * @author Christopher Lutz
 */
public class TyArray extends TyObject {
    
    private List<TyObject> internal;
    
    public TyArray(List<TyObject> internal) {
        
        super(ClassRegistry.forName(TrinityNatives.Classes.ARRAY));
        
        this.internal = internal;
    }
    
    public List<TyObject> getInternal() {
        
        return internal;
    }
    
    public int size() {
        
        return getInternal().size();
    }
}

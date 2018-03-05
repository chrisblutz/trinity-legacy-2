package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.util.List;


/**
 * @author Christopher Lutz
 */
public class TyArray extends TyObject {
    
    private List<TyObject> internal;
    
    public TyArray(List<TyObject> internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.ARRAY, true));
        
        this.internal = internal;
        
        // Make sure Array's instance fields are initialized, since its constructor is not called
        getObjectClass().initializeInstanceFields(this, new TyRuntime());
    }
    
    public List<TyObject> getInternal() {
        
        return internal;
    }
    
    public int size() {
        
        return getInternal().size();
    }
}

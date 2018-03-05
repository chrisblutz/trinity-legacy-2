package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyField;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class TyFieldObject extends TyObject {
    
    private TyField internal;
    
    public TyFieldObject(TyField internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.FIELD, true));
        
        this.internal = internal;
    }
    
    public TyField getInternal() {
        
        return internal;
    }
}

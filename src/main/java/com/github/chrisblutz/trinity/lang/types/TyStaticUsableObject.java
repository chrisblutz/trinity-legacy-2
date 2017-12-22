package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyModule;
import com.github.chrisblutz.trinity.lang.TyObject;


/**
 * @author Christopher Lutz
 */
public class TyStaticUsableObject extends TyObject {
    
    private TyModule internalModule;
    private TyClass internalClass;
    
    public TyStaticUsableObject(TyModule internalModule, TyClass internalClass) {
        
        super(TyStaticReferenceClass.INSTANCE);
        
        this.internalModule = internalModule;
        this.internalClass = internalClass;
    }
    
    public TyModule asModule() {
        
        return internalModule;
    }
    
    public TyClass asClass() {
        
        return internalClass;
    }
}

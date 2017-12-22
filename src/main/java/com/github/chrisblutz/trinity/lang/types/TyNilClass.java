package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.natives.NativeStorage;


/**
 * @author Christopher Lutz
 */
public class TyNilClass extends TyClass {
    
    public TyNilClass() {
        
        super("Trinity.Nil", "Nil");
        
        addMethod(new TyMethod("toString", false, true, this, new TyProcedure((runtime, thisObj, params) -> NativeStorage.getNilString(), true)));
    }
}

package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.TyModule;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.natives.NativeConversion;


/**
 * @author Christopher Lutz
 */
public class TyStaticReferenceClass extends TyClass {
    
    public static final TyStaticReferenceClass INSTANCE = new TyStaticReferenceClass();
    
    public TyStaticReferenceClass() {
        
        super("Trinity._StaticRef", "_StaticRef");
        
        addMethod(new TyMethod("toString", false, true, this, new TyProcedure((runtime, thisObj, params) -> {
            
            TyStaticUsableObject usable = NativeConversion.cast(TyStaticUsableObject.class, thisObj);
            TyModule tyModule = usable.asModule();
            TyClass tyClass = usable.asClass();
            
            String str = "static_ref[" + (tyModule != null ? tyModule.getFullName() : tyClass.getFullName()) + "]";
            return new TyString(str);
            
        }, true)));
    }
}

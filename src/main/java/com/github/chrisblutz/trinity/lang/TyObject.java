package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.types.TyNilClass;


/**
 * @author Christopher Lutz
 */
public class TyObject {
    
    public static final TyObject NIL = new TyObject(new TyNilClass()), NONE = new TyObject(ClassRegistry.MISSING_CLASS);
    
    private TyClass objectClass;
    private int superLevel = 0;
    
    public TyObject(TyClass objectClass) {
        
        this.objectClass = objectClass;
    }
    
    public TyClass getObjectClass() {
        
        return objectClass;
    }
    
    public int getSuperLevel() {
        
        return superLevel;
    }
    
    public void incrementSuperLevel() {
        
        superLevel++;
    }
    
    public TyObject tyInvoke(String method, TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject... args) {
        
        if (getSuperLevel() == 0) {
            
            return getObjectClass().tyInvoke(method, runtime, subProcedure, subProcedureRuntime, this, args);
            
        } else {
            
            TyClass superclass = getObjectClass();
            for (int i = 0; i < getSuperLevel(); i++) {
                
                superclass = superclass.getSuperclass();
                if (superclass == null) {
                    
                    Errors.throwError(Errors.Classes.INHERITANCE_ERROR, runtime, "Superclass does not exist.");
                    break;
                }
            }
            
            TyObject obj = NIL;
            if (superclass != null) {
                
                obj = superclass.tyInvoke(method, runtime, subProcedure, subProcedureRuntime, this, args);
            }
            
            superLevel--;
            
            return obj;
        }
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;


/**
 * @author Christopher Lutz
 */
public class SingleTokenVariableLocationRetriever implements VariableLocationRetriever {
    
    private String contents;
    
    public SingleTokenVariableLocationRetriever(String contents) {
        
        this.contents = contents;
    }
    
    public String getContents() {
        
        return contents;
    }
    
    @Override
    public VariableLocation evaluate(TyObject thisObj, TyRuntime runtime) {
        
        if (thisObj == TyObject.NONE) {
            
            if (runtime.hasVariable(getContents())) {
                
                return runtime.getVariableLocation(getContents());
                
            } else if (runtime.getThis() != TyObject.NONE && runtime.getThis().getObjectClass().hasField(getContents(), true)) {
                
                return runtime.getThis().getObjectClass().getField(getContents(), true, runtime.getThis());
                
            } else if (runtime.isStaticScope() && runtime.getCurrentUsable().hasField(getContents(), false)) {
                
                return runtime.getCurrentUsable().getField(getContents(), false, TyObject.NONE);
                
            } else {
                
                VariableLocation location = new VariableLocation();
                VariableManager.put(location, TyObject.NIL);
                runtime.setVariableLocation(getContents(), location);
                
                return location;
            }
            
        } else if (thisObj instanceof TyStaticUsableObject) {
            
            TyStaticUsableObject usable = (TyStaticUsableObject) thisObj;
            
            if (usable.asClass() != null && usable.asClass().hasField(getContents(), false)) {
                
                return usable.asClass().getField(getContents(), false, TyObject.NONE);
                
            } else if (usable.asModule() != null && usable.asModule().hasField(getContents(), false)) {
                
                return usable.asModule().getField(getContents(), false, TyObject.NONE);
            }
            
        } else {
            
            TyClass tyClass = thisObj.getObjectClass();
            if (tyClass.hasField(getContents(), true)) {
                
                return tyClass.getField(getContents(), true, thisObj);
            }
        }
        
        Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "No field '" + getContents() + "' found.", getContents());
        
        return null;
    }
}

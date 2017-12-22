package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;


/**
 * @author Christopher Lutz
 */
public class GlobalVariableLocationRetriever implements VariableLocationRetriever {
    
    private String name;
    
    public GlobalVariableLocationRetriever(String name) {
        
        this.name = name;
    }
    
    public String getName() {
        
        return name;
    }
    
    @Override
    public VariableLocation evaluate(TyObject thisObj, TyRuntime runtime) {
        
        if (!VariableManager.hasGlobalVariable(getName())) {
            
            VariableManager.setGlobalVariable(getName(), TyObject.NIL);
        }
        
        return VariableManager.getGlobalVariable(getName());
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;


/**
 * @author Christopher Lutz
 */
public class GlobalVariableInstruction extends Instruction {
    
    private String name;
    
    public GlobalVariableInstruction(String name, Location location) {
        
        super(location);
        
        this.name = name;
    }
    
    public String getName() {
        
        return name;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        if (VariableManager.hasGlobalVariable(getName())) {
            
            return VariableManager.getGlobalVariable(getName()).getValue();
            
        } else {
            
            Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "Global field '" + getName() + "' not found.");
        }
        
        return TyObject.NIL;
    }
}

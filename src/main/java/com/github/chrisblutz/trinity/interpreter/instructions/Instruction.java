package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public abstract class Instruction {
    
    private Location location;
    
    public Instruction(Location location) {
        
        this.location = location;
    }
    
    public Location getLocation() {
        
        return location;
    }
    
    public void updateLocation(TyRuntime runtime) {
        
        getLocation().makeCurrent();
        runtime.setCurrentLocation(getLocation());
    }
    
    protected abstract TyObject evaluate(TyObject thisObj, TyRuntime runtime);
}

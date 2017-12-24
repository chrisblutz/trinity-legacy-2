package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class TyProcedureObject extends TyObject {
    
    private TyProcedure internal;
    private TyRuntime runtime;
    private boolean broken = false;
    
    public TyProcedureObject(TyProcedure internal, TyRuntime runtime) {
        
        super(ClassRegistry.forName(TrinityNatives.Classes.PROCEDURE, true));
        
        this.internal = internal;
        this.runtime = runtime;
    }
    
    public TyProcedure getInternal() {
        
        return internal;
    }
    
    public TyRuntime getRuntime() {
        
        return runtime;
    }
    
    public boolean isBroken() {
        
        return broken;
    }
    
    public void setBroken(boolean broken) {
        
        this.broken = broken;
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;


/**
 * @author Christopher Lutz
 */
public class FinallyInstructionSet extends InstructionSet {
    
    private ProcedureAction action;
    
    public FinallyInstructionSet(ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.action = action;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyRuntime newRuntime = runtime.cloneWithImports();
        
        TyObject result = TyObject.NIL;
        
        if (getAction() != null) {
            
            result = getAction().onAction(newRuntime, TyObject.NONE);
        }
        
        newRuntime.disposeInto(runtime);
        
        return result;
    }
}

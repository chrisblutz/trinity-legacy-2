package com.github.chrisblutz.trinity.interpreter.actions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;


/**
 * @author Christopher Lutz
 */
public class VariableProcedureAction implements ProcedureAction {
    
    private InstructionSet set;
    private Location location;
    
    public VariableProcedureAction(InstructionSet set, Location location) {
        
        this.set = set;
        this.location = location;
    }
    
    @Override
    public TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args) {
        
        TrinityStack stack = TrinityStack.getCurrentThreadStack();
        
        stack.add(location.getFileName(), location.getLineNumber());
        
        TyObject result = set.evaluate(TyObject.NONE, runtime);
        
        stack.pop();
        
        return result;
    }
}

package com.github.chrisblutz.trinity.interpreter.actions;

import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;


/**
 * @author Christopher Lutz
 */
public class ArgumentProcedureAction implements ProcedureAction {
    
    private InstructionSet set;
    
    public ArgumentProcedureAction(InstructionSet set) {
        
        this.set = set;
    }
    
    @Override
    public TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args) {
        
        return set.evaluate(thisObj, runtime);
    }
}

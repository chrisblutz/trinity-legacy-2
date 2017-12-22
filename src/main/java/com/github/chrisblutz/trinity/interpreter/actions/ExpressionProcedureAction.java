package com.github.chrisblutz.trinity.interpreter.actions;

import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;


/**
 * @author Christopher Lutz
 */
public class ExpressionProcedureAction implements ProcedureAction {
    
    private InstructionSet[] sets;
    private boolean appendToStackTrace;
    
    public ExpressionProcedureAction(InstructionSet[] sets, boolean appendToStackTrace) {
        
        this.sets = sets;
        this.appendToStackTrace = appendToStackTrace;
    }
    
    @Override
    public TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args) {
        
        TyObject returnObj = TyObject.NONE;
        
        TrinityStack stack = TrinityStack.getCurrentThreadStack();
        for (InstructionSet set : sets) {
            
            if (!appendToStackTrace) {
                
                stack.pop();
            }
            
            stack.add(set.getLocation().getFileName(), set.getLocation().getLineNumber());
            
            TyObject result = set.evaluate(TyObject.NONE, runtime);
            
            if (appendToStackTrace) {
                
                stack.pop();
            }
            
            if (result != null && !runtime.isReturning()) {
                
                returnObj = result;
                
            } else if (runtime.isReturning()) {
                
                return runtime.getReturnObject();
                
            } else {
                
                return returnObj;
            }
        }
        
        return returnObj;
    }
}

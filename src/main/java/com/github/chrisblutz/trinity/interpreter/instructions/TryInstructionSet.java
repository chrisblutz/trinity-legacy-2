package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.errors.TrinityError;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;


/**
 * @author Christopher Lutz
 */
public class TryInstructionSet extends InstructionSet {
    
    private ProcedureAction action;
    private CatchInstructionSet catchSet = null;
    private FinallyInstructionSet finallySet = null;
    
    public TryInstructionSet(ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.action = action;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    public CatchInstructionSet getCatchSet() {
        
        return catchSet;
    }
    
    public void setCatchSet(CatchInstructionSet catchSet) {
        
        this.catchSet = catchSet;
    }
    
    public FinallyInstructionSet getFinallySet() {
        
        return finallySet;
    }
    
    public void setFinallySet(FinallyInstructionSet finallySet) {
        
        this.finallySet = finallySet;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        TyRuntime newRuntime = runtime.cloneWithImports();
        
        TyObject result = TyObject.NIL;
        
        int stackDepth = TrinityStack.getCurrentThreadStack().size();
        try {
            
            if (getAction() != null) {
                
                result = getAction().onAction(newRuntime, TyObject.NONE);
            }
            
        } catch (TrinityError e) {
            
            
            // Remove stack trace elements from the top of the stack.
            // These are normally removed after an instruction set completes,
            // but since this instruction set exited with an error, its
            // corresponding stack elements were not removed.
            TrinityStack.getCurrentThreadStack().popToSize(stackDepth);
            
            if (getCatchSet() != null) {
                
                getCatchSet().setErrorObject(e.getErrorObject());
                result = getCatchSet().evaluate(TyObject.NONE, runtime);
            }
            
        } finally {
            
            if (getFinallySet() != null) {
                
                result = getFinallySet().evaluate(TyObject.NONE, runtime);
            }
        }
        
        newRuntime.disposeInto(runtime);
        
        return result;
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;


/**
 * @author Christopher Lutz
 */
public class CatchInstructionSet extends InstructionSet {
    
    private String errorVariable;
    private ProcedureAction action;
    private TyObject errorObject = TyObject.NIL;
    private TryInstructionSet trySet;
    
    public CatchInstructionSet(String errorVariable, ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.action = action;
        this.errorVariable = errorVariable;
    }
    
    public String getErrorVariable() {
        
        return errorVariable;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    public TyObject getErrorObject() {
        
        return errorObject;
    }
    
    public void setErrorObject(TyObject errorObject) {
        
        this.errorObject = errorObject;
    }
    
    public TryInstructionSet getTrySet() {
        
        return trySet;
    }
    
    public void setTrySet(TryInstructionSet trySet) {
        
        this.trySet = trySet;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        TyRuntime newRuntime = runtime.clone();
        
        TyObject result = TyObject.NIL;
        
        if (getAction() != null) {
            
            newRuntime.setVariable(getErrorVariable(), getErrorObject());
            result = getAction().onAction(newRuntime, TyObject.NONE);
        }
        
        newRuntime.disposeInto(runtime);
        
        return result;
    }
}

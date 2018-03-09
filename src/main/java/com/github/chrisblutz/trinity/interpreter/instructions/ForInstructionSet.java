package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.NativeConversion;


/**
 * @author Christopher Lutz
 */
public class ForInstructionSet extends InstructionSet {
    
    private InstructionSet initial, expression, after;
    private ProcedureAction action;
    
    public ForInstructionSet(InstructionSet initial, InstructionSet expression, InstructionSet after, ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.initial = initial;
        this.expression = expression;
        this.after = after;
        this.action = action;
    }
    
    public InstructionSet getInitial() {
        
        return initial;
    }
    
    public InstructionSet getExpression() {
        
        return expression;
    }
    
    public InstructionSet getAfter() {
        
        return after;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyRuntime newRuntime = runtime.cloneWithImports();
        
        if (getInitial() != null) {
            
            getInitial().evaluate(TyObject.NONE, newRuntime);
        }
        
        while (NativeConversion.toBoolean(getExpression().evaluate(TyObject.NONE, newRuntime))) {
            
            if (getAction() != null) {
                
                getAction().onAction(newRuntime, TyObject.NONE);
            }
            
            if (newRuntime.isReturning()) {
                
                break;
                
            } else if (newRuntime.isBroken()) {
                
                newRuntime.setBroken(false);
                break;
                
            } else if (newRuntime.isContinuing()) {
                
                newRuntime.setContinuing(false);
            }
            
            if (getAfter() != null) {
                
                getAfter().evaluate(TyObject.NONE, newRuntime);
            }
        }
        
        newRuntime.disposeInto(runtime);
        
        return TyObject.NONE;
    }
}

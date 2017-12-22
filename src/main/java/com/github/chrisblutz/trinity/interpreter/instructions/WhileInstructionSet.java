package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class WhileInstructionSet extends InstructionSet {
    
    private InstructionSet expression;
    private ProcedureAction action;
    
    public WhileInstructionSet(InstructionSet expression, ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.expression = expression;
        this.action = action;
    }
    
    public InstructionSet getExpression() {
        
        return expression;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        TyRuntime newRuntime = runtime.clone();
        
        while (TrinityNatives.toBoolean(getExpression().evaluate(TyObject.NONE, newRuntime))) {
            
            if (getAction() != null) {
                
                getAction().onAction(newRuntime, TyObject.NONE);
            }
            
            if (newRuntime.isReturning()) {
                
                break;
                
            } else if (newRuntime.isBroken()) {
                
                newRuntime.setBroken(false);
                break;
            }
        }
        
        newRuntime.disposeInto(runtime);
        
        return TyObject.NONE;
    }
}

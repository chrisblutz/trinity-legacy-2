package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.NativeConversion;


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
        
        updateLocation(runtime);
        
        TyRuntime newRuntime = runtime.cloneWithImports();
        
        while (NativeConversion.toBoolean(getExpression().evaluate(TyObject.NONE, newRuntime))) {
            
            if (getAction() != null) {
                
                getAction().onAction(newRuntime, TyObject.NONE);
            }
            
            if (newRuntime.isReturning()) {
                
                break;
                
            } else if (newRuntime.isBroken()) {
                
                newRuntime.setBroken(false);
                break;
                
            }else if(newRuntime.isContinuing()){
                
                newRuntime.setContinuing(false);
            }
        }
        
        newRuntime.disposeInto(runtime);
        
        return TyObject.NONE;
    }
}

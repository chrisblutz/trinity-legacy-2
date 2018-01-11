package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public class ReturnInstructionSet extends InstructionSet {
    
    private InstructionSet expression;
    
    public ReturnInstructionSet(InstructionSet expression, Location location) {
        
        super(new Instruction[0], location);
        
        this.expression = expression;
    }
    
    public InstructionSet getExpression() {
        
        return expression;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyObject result = TyObject.NIL;
        
        if (getExpression() != null) {
            
            result = getExpression().evaluate(TyObject.NONE, runtime);
        }
        
        runtime.setReturning(true);
        runtime.setReturnObject(result);
        
        return result;
    }
}

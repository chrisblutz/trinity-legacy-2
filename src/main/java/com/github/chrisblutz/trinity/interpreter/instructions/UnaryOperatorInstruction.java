package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.UnaryOperator;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public class UnaryOperatorInstruction extends Instruction {
    
    private UnaryOperator operator;
    
    public UnaryOperatorInstruction(UnaryOperator operator, Location location) {
        
        super(location);
        
        this.operator = operator;
    }
    
    public UnaryOperator getOperator() {
        
        return operator;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        return getOperator().operate(thisObj, runtime);
    }
}

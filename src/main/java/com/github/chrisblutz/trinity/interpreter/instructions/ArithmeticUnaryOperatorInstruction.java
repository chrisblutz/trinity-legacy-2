package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.ArithmeticUnaryOperator;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public class ArithmeticUnaryOperatorInstruction extends Instruction {
    
    private ArithmeticUnaryOperator operator;
    
    public ArithmeticUnaryOperatorInstruction(ArithmeticUnaryOperator operator, Location location) {
        
        super(location);
        
        this.operator = operator;
    }
    
    public ArithmeticUnaryOperator getOperator() {
        
        return operator;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        return getOperator().operate(thisObj, runtime);
    }
}

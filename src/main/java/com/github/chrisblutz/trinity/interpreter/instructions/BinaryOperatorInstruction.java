package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.BinaryOperator;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public class BinaryOperatorInstruction extends Instruction {
    
    private BinaryOperator operator;
    private InstructionSet operand;
    
    public BinaryOperatorInstruction(BinaryOperator operator, InstructionSet operand, Location location) {
        
        super(location);
        
        this.operator = operator;
        this.operand = operand;
    }
    
    public BinaryOperator getOperator() {
        
        return operator;
    }
    
    public InstructionSet getOperand() {
        
        return operand;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        TyObject operand = getOperand().evaluate(TyObject.NONE, runtime);
        
        return getOperator().operate(thisObj, operand, runtime);
    }
}

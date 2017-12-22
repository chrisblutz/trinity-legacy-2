package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.LogicalOperator;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public class LogicalOperatorInstruction extends Instruction {
    
    private LogicalOperator operator;
    private InstructionSet operand;
    
    public LogicalOperatorInstruction(LogicalOperator operator, InstructionSet operand, Location location) {
        
        super(location);
        
        this.operator = operator;
        this.operand = operand;
    }
    
    public LogicalOperator getOperator() {
        
        return operator;
    }
    
    public InstructionSet getOperand() {
        
        return operand;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        return getOperator().operate(thisObj, getOperand(), runtime);
    }
}

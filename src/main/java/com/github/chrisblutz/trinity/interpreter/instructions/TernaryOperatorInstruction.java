package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class TernaryOperatorInstruction extends Instruction {
    
    private InstructionSet condition, trueValue, falseValue;
    
    public TernaryOperatorInstruction(InstructionSet condition, InstructionSet trueValue, InstructionSet falseValue, Location location) {
        
        super(location);
        
        this.condition = condition;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }
    
    public InstructionSet getCondition() {
        
        return condition;
    }
    
    public InstructionSet getTrueValue() {
        
        return trueValue;
    }
    
    public InstructionSet getFalseValue() {
        
        return falseValue;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyObject condition = getCondition().evaluate(TyObject.NONE, runtime);
        if (TrinityNatives.toBoolean(condition)) {
            
            return getTrueValue().evaluate(TyObject.NONE, runtime);
            
        } else {
            
            return getFalseValue().evaluate(TyObject.NONE, runtime);
        }
    }
}

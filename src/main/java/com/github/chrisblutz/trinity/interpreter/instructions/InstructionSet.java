package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public class InstructionSet extends Instruction {
    
    private Instruction[] instructions;
    
    public InstructionSet(Instruction[] instructions, Location location) {
        
        super(location);
        
        this.instructions = instructions;
    }
    
    public Instruction[] getInstructions() {
        
        return instructions;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        TyObject currentThis = thisObj;
        for (Instruction instruction : getInstructions()) {
            
            currentThis = instruction.evaluate(currentThis, runtime);
        }
        
        return currentThis;
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;


/**
 * @author Christopher Lutz
 */
public class IndexAccessInstruction extends Instruction {
    
    private InstructionSet[] indices;
    private TyProcedure procedure;
    
    public IndexAccessInstruction(InstructionSet[] indices, TyProcedure procedure, Location location) {
        
        super(location);
        
        this.indices = indices;
        this.procedure = procedure;
    }
    
    public InstructionSet[] getIndices() {
        
        return indices;
    }
    
    public TyProcedure getProcedure() {
        
        return procedure;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyObject[] indices = new TyObject[getIndices().length];
        for (int i = 0; i < getIndices().length; i++) {
            
            indices[i] = getIndices()[i].evaluate(TyObject.NONE, runtime);
        }
        
        return thisObj.tyInvoke("[]", runtime, getProcedure(), runtime, indices);
    }
}

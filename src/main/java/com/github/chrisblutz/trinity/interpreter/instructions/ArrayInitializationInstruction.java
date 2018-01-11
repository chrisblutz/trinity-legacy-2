package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyArray;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class ArrayInitializationInstruction extends Instruction {
    
    private InstructionSet[] components;
    
    public ArrayInitializationInstruction(InstructionSet[] components, Location location) {
        
        super(location);
        
        this.components = components;
    }
    
    public InstructionSet[] getComponents() {
        
        return components;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        List<TyObject> components = new ArrayList<>();
        for (InstructionSet component : getComponents()) {
            
            components.add(component.evaluate(TyObject.NONE, runtime));
        }
        
        return new TyArray(components);
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.types.TyMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class MapInitializationInstruction extends Instruction {
    
    private List<InstructionSet[]> components;
    
    public MapInitializationInstruction(List<InstructionSet[]> components, Location location) {
        
        super(location);
        
        this.components = components;
    }
    
    public List<InstructionSet[]> getComponents() {
        
        return components;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        Map<TyObject, TyObject> map = new LinkedHashMap<>();
        for (InstructionSet[] sets : getComponents()) {
            
            if (sets.length == 2) {
                
                TyObject key = sets[0].evaluate(TyObject.NONE, runtime);
                TyObject value = sets[1].evaluate(TyObject.NONE, runtime);
                
                map.put(key, value);
            }
        }
        
        return new TyMap(map, TyMap.getOrderedStorage());
    }
}

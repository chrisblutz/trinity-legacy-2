package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyModule;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;


/**
 * @author Christopher Lutz
 */
public class UsingInstructionSet extends InstructionSet {
    
    private InstructionSet module;
    
    public UsingInstructionSet(InstructionSet module, Location location) {
        
        super(new Instruction[0], location);
        
        this.module = module;
    }
    
    public InstructionSet getModule() {
        
        return module;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        TyObject result = getModule().evaluate(TyObject.NONE, runtime);
        if (result instanceof TyStaticUsableObject) {
            
            TyModule module = ((TyStaticUsableObject) result).asModule();
            if (module != null) {
                
                runtime.importModule(module);
                
            } else {
                
                Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "Module not found.");
            }
            
        } else {
            
            Errors.throwError(Errors.Classes.ARGUMENT_ERROR, runtime, "'using' statements require a module as an argument.");
        }
        
        return TyObject.NIL;
    }
}

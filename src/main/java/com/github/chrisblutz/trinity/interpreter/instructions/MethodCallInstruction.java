package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;


/**
 * @author Christopher Lutz
 */
public class MethodCallInstruction extends Instruction {
    
    private String name;
    private InstructionSet[] arguments;
    private TyProcedure procedure;
    
    public MethodCallInstruction(String name, InstructionSet[] arguments, TyProcedure procedure, Location location) {
        
        super(location);
        
        this.name = name;
        this.arguments = arguments;
        this.procedure = procedure;
    }
    
    public String getName() {
        
        return name;
    }
    
    public InstructionSet[] getArguments() {
        
        return arguments;
    }
    
    public TyProcedure getProcedure() {
        
        return procedure;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        runtime.setCurrentFilePath(getLocation().getFilePath());
        
        TyObject[] arguments = new TyObject[getArguments().length];
        for (int i = 0; i < arguments.length; i++) {
            
            arguments[i] = getArguments()[i].evaluate(TyObject.NONE, runtime);
        }
        
        if (thisObj == TyObject.NONE) {
            
            if (runtime.isStaticScope()) {
                
                return runtime.getCurrentUsable().tyInvoke(getName(), runtime, getProcedure(), runtime, TyObject.NONE, arguments);
                
            } else {
                
                runtime.setInitialStatement(true);
                TyObject result = runtime.getThis().tyInvoke(getName(), runtime, getProcedure(), runtime, arguments);
                runtime.setInitialStatement(false);
                return result;
            }
            
        } else if (thisObj instanceof TyStaticUsableObject) {
            
            TyStaticUsableObject usableObject = (TyStaticUsableObject) thisObj;
            
            if (usableObject.asClass() != null && (usableObject.asClass().hasMethod(getName(), false) || getName().contentEquals("new"))) {
                
                return usableObject.asClass().tyInvoke(getName(), runtime, getProcedure(), runtime, TyObject.NONE, arguments);
                
            } else if (usableObject.asModule() != null && usableObject.asModule().hasMethod(getName(), false)) {
                
                return usableObject.asModule().tyInvoke(getName(), runtime, getProcedure(), runtime, TyObject.NONE, arguments);
            }
            
            Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "No method '" + getName() + "' found in '" + (usableObject.asClass() != null ? usableObject.asClass() : usableObject.asModule()).getFullName() + "'.");
            return TyObject.NONE;
            
        } else {
            
            return thisObj.tyInvoke(getName(), runtime, getProcedure(), runtime, arguments);
        }
    }
}

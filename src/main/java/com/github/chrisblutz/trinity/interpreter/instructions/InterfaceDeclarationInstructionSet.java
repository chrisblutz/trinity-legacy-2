package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.*;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.utils.ArrayUtils;


/**
 * @author Christopher Lutz
 */
public class InterfaceDeclarationInstructionSet extends InstructionSet {
    
    private String name;
    private String[] superinterfaces;
    private ProcedureAction body;
    
    public InterfaceDeclarationInstructionSet(String name, String[] superinterfaces, ProcedureAction body, Location location) {
        
        super(new Instruction[0], location);
        
        this.name = name;
        this.superinterfaces = superinterfaces;
        this.body = body;
    }
    
    public String getName() {
        
        return name;
    }
    
    public String[] getSuperinterfaces() {
        
        return superinterfaces;
    }
    
    public ProcedureAction getBody() {
        
        return body;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        
        String priorName = "";
        if (runtime.getCurrentUsable() != null) {
            
            priorName = runtime.getCurrentUsable().getFullName() + ".";
        }
        
        TyClass tyClass = ClassRegistry.forName(priorName + getName(), false);
        tyClass.setParent(runtime.getCurrentUsable());
        tyClass.setInterface(true);
        
        TyClass[] superinterfaces = new TyClass[getSuperinterfaces().length];
        for (int i = 0; i < getSuperinterfaces().length; i++) {
            
            String superinterface = getSuperinterfaces()[i];
            TyClass superinterfaceClass;
            if (runtime.getCurrentUsable() != null && runtime.getCurrentUsable().hasInternalClass(superinterface)) {
                
                superinterfaceClass = runtime.getCurrentUsable().getInternalClass(superinterface);
                
            } else if (runtime.hasImports() && runtime.hasImportedClass(superinterface)) {
                
                superinterfaceClass = runtime.getImportedClass(superinterface);
                
            } else if (runtime.getCurrentModule() != null && runtime.getCurrentModule().hasInternalClass(superinterface)) {
                
                superinterfaceClass = runtime.getCurrentModule().getInternalClass(superinterface);
                
            } else if (ModuleRegistry.getModule("Trinity").hasInternalClass(superinterface)) {
                
                superinterfaceClass = ModuleRegistry.getModule("Trinity").getInternalClass(superinterface);
                
            } else {
                
                superinterfaceClass = ClassRegistry.getClass(getSuperinterfaces()[i]);
            }
            
            superinterfaces[i] = superinterfaceClass;
        }
        
        if (superinterfaces.length > 0) {
            
            TyClass[] combined = ArrayUtils.combine(tyClass.getSuperinterfaces(), superinterfaces);
            tyClass.setSuperinterfaces(combined);
        }
        
        if (runtime.getCurrentUsable() != null) {
            
            runtime.getCurrentUsable().addInternalClass(tyClass);
        }
        
        if (getBody() != null) {
            
            TyRuntime newRuntime = runtime.clone();
            newRuntime.setCurrentUsable(tyClass);
            
            getBody().onAction(newRuntime, TyObject.NONE);
            newRuntime.disposeVariablesInto(runtime);
        }
        
        return TyObject.NONE;
    }
}

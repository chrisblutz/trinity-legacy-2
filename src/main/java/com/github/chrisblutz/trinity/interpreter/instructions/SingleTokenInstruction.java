package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.*;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;


/**
 * @author Christopher Lutz
 */
public class SingleTokenInstruction extends Instruction {
    
    private String contents;
    
    public SingleTokenInstruction(String contents, Location location) {
        
        super(location);
        
        this.contents = contents;
    }
    
    public String getContents() {
        
        return contents;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        if (thisObj == TyObject.NONE) {
            
            if (runtime.hasVariable(getContents())) {
                
                return runtime.getVariable(getContents());
                
            } else if (runtime.getThis() != TyObject.NONE && runtime.getThis().getObjectClass().hasField(getContents(), true)) {
                
                VariableLocation location = runtime.getThis().getObjectClass().getField(getContents(), true, runtime.getThis());
                
                if (location.checkScope(runtime)) {
                    
                    return location.getValue();
                    
                } else {
                    
                    location.getScope().reportAccessViolation(runtime);
                }
                
            } else if (runtime.isStaticScope() && runtime.getCurrentUsable().hasField(getContents(), false)) {
                
                VariableLocation location = runtime.getCurrentUsable().getField(getContents(), false, TyObject.NONE);
                
                if (location.checkScope(runtime)) {
                    
                    return location.getValue();
                    
                } else {
                    
                    location.getScope().reportAccessViolation(runtime);
                }
                
            } else if (runtime.getCurrentUsable() != null && (runtime.getCurrentUsable().hasInternalClass(getContents()) || runtime.getCurrentUsable().hasInternalModule(getContents()))) {
                
                TyClass tyClass = null;
                if (runtime.getCurrentUsable().hasInternalClass(getContents())) {
                    
                    tyClass = runtime.getCurrentUsable().getInternalClass(getContents());
                }
                
                TyModule tyModule = null;
                if (runtime.getCurrentUsable().hasInternalModule(getContents())) {
                    
                    tyModule = runtime.getCurrentUsable().getInternalModule(getContents());
                }
                
                return new TyStaticUsableObject(tyModule, tyClass);
                
            } else if (runtime.hasImports() && (runtime.hasImportedClass(getContents()) || runtime.hasImportedModule(getContents()))) {
                
                TyClass tyClass = null;
                if (runtime.hasImportedClass(getContents())) {
                    
                    tyClass = runtime.getImportedClass(getContents());
                }
                
                TyModule tyModule = null;
                if (runtime.hasImportedModule(getContents())) {
                    
                    tyModule = runtime.getImportedModule(getContents());
                }
                
                return new TyStaticUsableObject(tyModule, tyClass);
                
            } else if (runtime.getCurrentModule() != null && (runtime.getCurrentModule().hasInternalClass(getContents()) || runtime.getCurrentModule().hasInternalModule(getContents()))) {
                
                TyClass tyClass = null;
                if (runtime.getCurrentModule().hasInternalClass(getContents())) {
                    
                    tyClass = runtime.getCurrentModule().getInternalClass(getContents());
                }
                
                TyModule tyModule = null;
                if (runtime.getCurrentModule().hasInternalModule(getContents())) {
                    
                    tyModule = runtime.getCurrentModule().getInternalModule(getContents());
                }
                
                return new TyStaticUsableObject(tyModule, tyClass);
                
            } else if (ModuleRegistry.moduleExists(getContents()) || ClassRegistry.classExists(getContents())) {
                
                TyClass tyClass = null;
                if (ClassRegistry.classExists(getContents())) {
                    
                    tyClass = ClassRegistry.getClass(getContents());
                }
                
                TyModule tyModule = null;
                if (ModuleRegistry.moduleExists(getContents())) {
                    
                    tyModule = ModuleRegistry.getModule(getContents());
                }
                
                return new TyStaticUsableObject(tyModule, tyClass);
                
            } else if (ModuleRegistry.getModule("Trinity").hasInternalClass(getContents())) {
                
                return new TyStaticUsableObject(null, ModuleRegistry.getModule("Trinity").getInternalClass(getContents()));
            }
            
        } else if (thisObj instanceof TyStaticUsableObject) {
            
            TyStaticUsableObject usable = (TyStaticUsableObject) thisObj;
            
            if (usable.asClass() != null) {
                
                if (usable.asClass().hasField(getContents(), false)) {
                    
                    VariableLocation location = usable.asClass().getField(getContents(), false, TyObject.NONE);
                    
                    if (location.checkScope(runtime)) {
                        
                        return location.getValue();
                        
                    } else {
                        
                        location.getScope().reportAccessViolation(runtime);
                    }
                    
                } else if (usable.asClass().hasInternalClass(getContents())) {
                    
                    return new TyStaticUsableObject(null, usable.asClass().getInternalClass(getContents()));
                }
                
            } else if (usable.asModule() != null) {
                
                if (usable.asModule().hasField(getContents(), false)) {
                    
                    VariableLocation location = usable.asModule().getField(getContents(), false, TyObject.NONE);
                    
                    if (location.checkScope(runtime)) {
                        
                        return location.getValue();
                        
                    } else {
                        
                        location.getScope().reportAccessViolation(runtime);
                    }
                    
                } else if (usable.asModule().hasInternalModule(getContents()) || usable.asModule().hasInternalClass(getContents())) {
                    
                    TyClass tyClass = null;
                    if (usable.asModule().hasInternalClass(getContents())) {
                        
                        tyClass = usable.asModule().getInternalClass(getContents());
                    }
                    
                    TyModule tyModule = null;
                    if (usable.asModule().hasInternalModule(getContents())) {
                        
                        tyModule = usable.asModule().getInternalModule(getContents());
                    }
                    
                    return new TyStaticUsableObject(tyModule, tyClass);
                }
            }
            
        } else {
            
            TyClass tyClass = thisObj.getObjectClass();
            if (tyClass.hasField(getContents(), true)) {
                
                VariableLocation location = tyClass.getField(getContents(), thisObj == TyObject.NONE, thisObj);
                
                if (location.checkScope(runtime)) {
                    
                    return location.getValue();
                    
                } else {
                    
                    location.getScope().reportAccessViolation(runtime);
                }
            }
        }
        
        Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "No field '" + getContents() + "' found.");
        
        return TyObject.NIL;
    }
}

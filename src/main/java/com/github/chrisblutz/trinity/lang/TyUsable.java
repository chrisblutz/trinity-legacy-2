package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.scope.Scope;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;

import java.util.Map;


/**
 * @author Christopher Lutz
 */
public abstract class TyUsable {
    
    private String fullName, shortName;
    private TyUsable parent;
    
    public TyUsable(String fullName, String shortName) {
        
        this.fullName = fullName;
        this.shortName = shortName;
    }
    
    public String getFullName() {
        
        return fullName;
    }
    
    public String getShortName() {
        
        return shortName;
    }
    
    public abstract boolean hasMethod(String name, boolean isInstance);
    
    public abstract Map<String, TyMethod> getMethods();
    
    public abstract TyMethod getMethod(String name);
    
    public abstract void addMethod(TyMethod method);
    
    public abstract boolean hasField(String name, boolean isInstance);
    
    public abstract VariableLocation getField(String name, boolean isInstance, TyObject instance);
    
    public abstract TyField getFieldData(String name);
    
    public abstract void addField(TyField field, TyRuntime runtime);
    
    public abstract boolean hasInternalClass(String name);
    
    public TyClass getInternalClass(String name) {
        
        if (!hasInternalClass(name)) {
            
            Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, "Internal class '" + name + "' not found in " + getFullName() + ".");
        }
        
        return getInternalClasses().get(name);
    }
    
    public abstract Map<String, TyClass> getInternalClasses();
    
    public abstract void addInternalClass(TyClass tyClass);
    
    public abstract boolean hasInternalModule(String name);
    
    public TyModule getInternalModule(String name) {
        
        if (!hasInternalModule(name)) {
            
            Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, "Internal module '" + name + "' not found in " + getFullName() + ".");
        }
        
        return getInternalModules().get(name);
    }
    
    public abstract Map<String, TyModule> getInternalModules();
    
    public abstract void addInternalModule(TyModule tyModule);
    
    public TyUsable getParent() {
        
        return parent;
    }
    
    public void setParent(TyUsable parent) {
        
        this.parent = parent;
    }
    
    public TyModule getParentModule() {
        
        if (getParent() instanceof TyModule) {
            
            return (TyModule) getParent();
            
        } else if (getParent() != null) {
            
            return getParent().getParentModule();
            
        } else {
            
            return null;
        }
    }
    
    public TyObject tyInvoke(String method, TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject thisObj, TyObject... args) {
        
        return tyInvoke(this, method, runtime, subProcedure, subProcedureRuntime, thisObj, args);
    }
    
    public abstract TyObject tyInvoke(TyUsable origin, String method, TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject thisObj, TyObject... args);
    
    protected void throwFinalMethodError(String name) {
        
        Errors.throwError(Errors.Classes.INHERITANCE_ERROR, "Final method '" + name + "' cannot be overridden or re-declared.");
    }
    
    protected void throwFieldNotFoundError(String name) {
        
        Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, "Field '" + name + "' not found in class " + getFullName() + ".", name);
    }
    
    protected void throwInstanceMethodStaticScopeError(String name, TyRuntime runtime) {
        
        Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Instance method '" + name + "' cannot be called from a static context.");
    }
    
    protected void throwMethodScopeError(String name, Scope scope, TyRuntime runtime) {
        
        Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Method '" + name + "' cannot be accessed from this context because it is marked '" + scope.toString() + "'.");
    }
    
    protected void throwMethodNotFoundError(String name, TyUsable origin, TyRuntime runtime) {
        
        Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "No method '" + name + "' found in '" + origin.getFullName() + "'.");
    }
}

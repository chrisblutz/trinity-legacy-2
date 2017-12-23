package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.scope.Scope;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class TyModule extends TyUsable {
    
    private Map<String, TyClass> internalClasses = new HashMap<>();
    private Map<String, TyModule> internalModules = new HashMap<>();
    
    private Map<String, TyMethod> methods = new HashMap<>();
    private Map<String, TyField> variables = new HashMap<>();
    private Map<TyField, VariableLocation> moduleVariables = new HashMap<>();
    
    public TyModule(String fullName, String shortName) {
        
        super(fullName, shortName);
    }
    
    @Override
    public boolean hasMethod(String name, boolean isInstance) {
        
        return methods.containsKey(name);
    }
    
    @Override
    public Map<String, TyMethod> getMethods() {
        
        return methods;
    }
    
    @Override
    public void addMethod(TyMethod method) {
        
        if (methods.containsKey(method.getName()) && methods.get(method.getName()).isSecure()) {
            
            return;
            
        } else if (method.getName().contentEquals("initialize")) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, "Modules cannot declare an 'initialize' method.");
            
        } else if (method.isStatic()) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, "Methods in modules are static by default.");
            
        } else if (method.getScope() == Scope.PROTECTED) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, "Methods in modules cannot be declared 'protected'.");
        }
        
        methods.put(method.getName(), method);
    }
    
    @Override
    public boolean hasField(String name, boolean isInstance) {
        
        return variables.containsKey(name);
    }
    
    @Override
    public VariableLocation getField(String name, boolean isInstance, TyObject instance) {
        
        TyField field = getFieldData(name);
        if (hasField(name, isInstance)) {
            
            return moduleVariables.get(field);
            
        } else {
            
            throwFieldNotFoundError(name);
            return null;
        }
    }
    
    @Override
    public TyField getFieldData(String name) {
        
        return variables.get(name);
    }
    
    @Override
    public void addField(TyField field, TyRuntime runtime) {
        
        if (field.isStatic()) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Fields in modules are static by default.");
            
        } else if (field.getScope() == Scope.PROTECTED) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, "Fields in modules cannot be declared 'protected'.");
        }
        
        variables.put(field.getName(), field);
        
        VariableLocation location = new VariableLocation();
        location.setContainer(this);
        location.setConstant(field.isConstant());
        location.setScope(field.getScope());
        VariableManager.put(location, field.getDefaultValueAction().onAction(runtime, TyObject.NONE));
        moduleVariables.put(field, location);
    }
    
    @Override
    public boolean hasInternalClass(String name) {
        
        return getInternalClasses().containsKey(name);
    }
    
    @Override
    public Map<String, TyClass> getInternalClasses() {
        
        return internalClasses;
    }
    
    @Override
    public void addInternalClass(TyClass tyClass) {
        
        getInternalClasses().put(tyClass.getShortName(), tyClass);
    }
    
    @Override
    public boolean hasInternalModule(String name) {
        
        return getInternalModules().containsKey(name);
    }
    
    @Override
    public Map<String, TyModule> getInternalModules() {
        
        return internalModules;
    }
    
    @Override
    public void addInternalModule(TyModule tyModule) {
        
        getInternalModules().put(tyModule.getShortName(), tyModule);
    }
    
    @Override
    public TyObject tyInvoke(TyUsable origin, String method, TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject thisObj, TyObject... args) {
        
        if (methods.containsKey(method)) {
            
            TyMethod tyMethod = methods.get(method);
            
            Scope scope = tyMethod.getScope();
            boolean run = checkScope(scope, tyMethod, runtime);
            
            if (run) {
                
                TyRuntime newRuntime = runtime.clone();
                newRuntime.clearVariables();
                newRuntime.setImports(tyMethod.getImports());
                newRuntime.setCurrentUsable(this);
                newRuntime.setStaticScope(true);
                newRuntime.setStaticScopeObject(new TyStaticUsableObject(this, null));
                
                TyObject result = tyMethod.getProcedure().call(newRuntime, subProcedure, subProcedureRuntime, thisObj, args);
                
                if (newRuntime.isReturning()) {
                    
                    return newRuntime.getReturnObject();
                }
                
                return result;
                
            } else {
                
                throwMethodScopeError(method, scope, runtime);
                
                return TyObject.NONE;
            }
            
        } else if (thisObj == TyObject.NONE && ClassRegistry.getClass(TrinityNatives.Classes.KERNEL).getMethods().containsKey(method)) {
            
            return ClassRegistry.getClass(TrinityNatives.Classes.KERNEL).tyInvoke(origin, method, runtime, subProcedure, subProcedureRuntime, thisObj, args);
            
        } else {
            
            throwMethodNotFoundError(method, origin, runtime);
        }
        
        return TyObject.NONE;
    }
    
    private boolean checkScope(Scope scope, TyMethod method, TyRuntime runtime) {
        
        switch (scope) {
            
            case PUBLIC:
                
                return true;
            
            case MODULE_PROTECTED:
                
                return method.getContainer() == runtime.getCurrentModule();
            
            case PROTECTED:
                
                return false;
            
            case PRIVATE:
                
                return method.getContainer() == runtime.getCurrentUsable();
            
            default:
                
                return false;
        }
    }
}

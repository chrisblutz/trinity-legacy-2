package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.scope.Scope;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class TyRuntime implements Cloneable {
    
    private Map<String, VariableLocation> variables = new HashMap<>();
    private List<VariableLocation> clonedVariables = new ArrayList<>();
    private TyObject thisObject = TyObject.NONE;
    private boolean staticScope = false;
    private TyObject staticScopeObject = TyObject.NONE;
    private TyUsable currentUsable = null;
    private Scope currentScope = Scope.PUBLIC;
    private TyProcedure procedure = null;
    private boolean isBroken = false, isSwitchChaining = false, isReturning = false;
    private TyObject switchObject = TyObject.NONE;
    private TyObject returnObject = TyObject.NONE;
    private List<TyModule> imports = null;
    private Location currentLocation = null;
    
    // Used to determine Kernel or null-class/null-module method calls are placed without prior objects
    // even in non-static scenarios, where 'thisObj' might not be NONE
    private boolean isInitialStatement = false;
    
    public boolean hasVariable(String variable) {
        
        return variables.containsKey(variable);
    }
    
    public VariableLocation getVariableLocation(String variable) {
        
        return variables.get(variable);
    }
    
    public void setVariableLocation(String variable, VariableLocation variableLocation) {
        
        variables.put(variable, variableLocation);
    }
    
    public TyObject getVariable(String variable) {
        
        return getVariableLocation(variable).getValue();
    }
    
    public void setVariable(String variable, TyObject value) {
        
        VariableLocation location = new VariableLocation();
        setVariableLocation(variable, location);
        VariableManager.put(location, value);
    }
    
    public void clearVariables() {
        
        variables.clear();
    }
    
    public boolean isStaticScope() {
        
        return staticScope;
    }
    
    public void setStaticScope(boolean staticScope) {
        
        this.staticScope = staticScope;
    }
    
    public TyObject getStaticScopeObject() {
        
        return staticScopeObject;
    }
    
    public void setStaticScopeObject(TyObject staticScopeObject) {
        
        this.staticScopeObject = staticScopeObject;
    }
    
    public TyUsable getCurrentUsable() {
        
        return currentUsable;
    }
    
    public void setCurrentUsable(TyUsable currentUsable) {
        
        this.currentUsable = currentUsable;
    }
    
    public TyModule getCurrentModule() {
        
        if (getCurrentUsable() instanceof TyModule) {
            
            return (TyModule) getCurrentUsable();
            
        } else if (getCurrentUsable() != null) {
            
            return getCurrentUsable().getParentModule();
            
        } else {
            
            return null;
        }
    }
    
    public Scope getCurrentScope() {
        
        return currentScope;
    }
    
    public void setCurrentScope(Scope currentScope) {
        
        this.currentScope = currentScope;
    }
    
    public TyObject getThis() {
        
        return thisObject;
    }
    
    public void setThis(TyObject thisObject) {
        
        this.thisObject = thisObject;
    }
    
    public boolean isBroken() {
        
        return isBroken;
    }
    
    public void setBroken(boolean broken) {
        
        isBroken = broken;
    }
    
    public boolean isSwitchChaining() {
        
        return isSwitchChaining;
    }
    
    public void setSwitchChaining(boolean switchChaining) {
        
        isSwitchChaining = switchChaining;
    }
    
    public TyObject getSwitchObject() {
        
        return switchObject;
    }
    
    public void setSwitchObject(TyObject switchObject) {
        
        this.switchObject = switchObject;
    }
    
    public boolean isReturning() {
        
        return isReturning;
    }
    
    public void setReturning(boolean returning) {
        
        isReturning = returning;
    }
    
    public TyObject getReturnObject() {
        
        return returnObject;
    }
    
    public void setReturnObject(TyObject returnObject) {
        
        this.returnObject = returnObject;
    }
    
    public TyProcedure getProcedure() {
        
        return procedure;
    }
    
    public void setProcedure(TyProcedure procedure) {
        
        this.procedure = procedure;
    }
    
    public boolean isInitialStatement() {
        
        return isInitialStatement;
    }
    
    public void setInitialStatement(boolean initialStatement) {
        
        isInitialStatement = initialStatement;
    }
    
    public boolean hasImports() {
        
        return imports != null;
    }
    
    public void importModule(TyModule module) {
        
        if (imports == null) {
            
            imports = new ArrayList<>();
        }
        
        imports.add(module);
    }
    
    public boolean hasImportedClass(String name) {
        
        for (TyModule importedModule : imports) {
            
            if (importedModule.hasInternalClass(name)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public boolean hasImportedModule(String name) {
        
        for (TyModule importedModule : imports) {
            
            if (importedModule.hasInternalModule(name)) {
                
                return true;
            }
        }
        
        return false;
    }
    
    public TyClass getImportedClass(String name) {
        
        for (TyModule importedModule : imports) {
            
            if (importedModule.hasInternalClass(name)) {
                
                return importedModule.getInternalClass(name);
            }
        }
        
        return null;
    }
    
    public TyModule getImportedModule(String name) {
        
        for (TyModule importedModule : imports) {
            
            if (importedModule.hasInternalModule(name)) {
                
                return importedModule.getInternalModule(name);
            }
        }
        
        return null;
    }
    
    public List<TyModule> getImports() {
        
        return imports;
    }
    
    public Location getCurrentLocation() {
        
        return currentLocation;
    }
    
    public void setCurrentLocation(Location currentLocation) {
        
        this.currentLocation = currentLocation;
    }
    
    public void setImports(List<TyModule> imports) {
        
        this.imports = imports;
    }
    
    @Override
    public TyRuntime clone() {
        
        try {
            
            TyRuntime runtime = (TyRuntime) super.clone();
            Map<String, VariableLocation> variablesCopy = new HashMap<>();
            variablesCopy.putAll(variables);
            runtime.variables = variablesCopy;
            runtime.clonedVariables = new ArrayList<>(variablesCopy.values());
            runtime.staticScope = staticScope;
            runtime.staticScopeObject = staticScopeObject;
            runtime.currentUsable = currentUsable;
            runtime.procedure = procedure;
            runtime.currentLocation = currentLocation;
            
            return runtime;
            
        } catch (CloneNotSupportedException e) {
            
            return new TyRuntime();
        }
    }
    
    public TyRuntime cloneWithImports() {
        
        TyRuntime runtime = clone();
        runtime.imports = imports;
        
        return runtime;
    }
    
    public void disposeInto(TyRuntime runtime) {
        
        disposeVariablesInto(runtime);
        
        runtime.setBroken(isBroken());
        runtime.setReturning(isReturning());
        runtime.setReturnObject(getReturnObject());
    }
    
    public void disposeVariablesInto(TyRuntime runtime) {
        
        for (String variable : runtime.variables.keySet()) {
            
            if (hasVariable(variable)) {
                
                runtime.setVariableLocation(variable, getVariableLocation(variable));
            }
        }
        
        for (VariableLocation location : variables.values()) {
            
            if (!clonedVariables.contains(location)) {
                
                VariableManager.clearVariable(location);
            }
        }
    }
}

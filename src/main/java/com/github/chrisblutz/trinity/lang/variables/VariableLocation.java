package com.github.chrisblutz.trinity.lang.variables;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.TyUsable;
import com.github.chrisblutz.trinity.lang.scope.Scope;


/**
 * @author Christopher Lutz
 */
public class VariableLocation {
    
    private TyUsable container = null;
    private Scope scope = null;
    private boolean constant = false;
    
    public TyObject getValue() {
        
        return VariableManager.getVariable(this);
    }
    
    public TyUsable getContainer() {
        
        return container;
    }
    
    public void setContainer(TyUsable container) {
        
        this.container = container;
    }
    
    public Scope getScope() {
        
        return scope;
    }
    
    public void setScope(Scope scope) {
        
        this.scope = scope;
    }
    
    public boolean isConstant() {
        
        return constant;
    }
    
    public void setConstant(boolean constant) {
        
        this.constant = constant;
    }
    
    public boolean checkScope(TyRuntime runtime) {
        
        if (getContainer() == null) {
            
            return true;
        }
        
        switch (getScope()) {
            
            case PUBLIC:
                
                return true;
            
            case MODULE_PROTECTED:
                
                return getContainer().getParentModule() == runtime.getCurrentModule();
            
            case PROTECTED:
                
                return runtime.getCurrentUsable() instanceof TyClass && getContainer() instanceof TyClass && ((TyClass) runtime.getCurrentUsable()).isInstanceOf((TyClass) getContainer());
            
            case PRIVATE:
                
                return getContainer() == runtime.getCurrentUsable();
            
            default:
                
                return false;
        }
    }
}

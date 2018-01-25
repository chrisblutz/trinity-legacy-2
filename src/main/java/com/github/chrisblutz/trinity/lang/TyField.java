package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.scope.Scope;

import java.util.List;


/**
 * @author Christopher Lutz
 */
public class TyField {
    
    private String name;
    private boolean staticField, constantField;
    private TyUsable container;
    private ProcedureAction defaultValueAction;
    private Scope scope = Scope.PUBLIC;
    private List<TyModule> imports = null;
    
    public TyField(String name, boolean staticField, boolean constantField, TyUsable container, ProcedureAction defaultValueAction) {
        
        this.name = name;
        this.staticField = staticField;
        this.constantField = constantField;
        this.container = container;
        this.defaultValueAction = defaultValueAction;
    }
    
    public String getName() {
        
        return name;
    }
    
    public boolean isStatic() {
        
        return staticField;
    }
    
    public boolean isConstant() {
        
        return constantField;
    }
    
    public TyUsable getContainer() {
        
        return container;
    }
    
    public ProcedureAction getDefaultValueAction() {
        
        return defaultValueAction;
    }
    
    public Scope getScope() {
        
        return scope;
    }
    
    public void setScope(Scope scope) {
        
        this.scope = scope;
    }
    
    public List<TyModule> getImports() {
        
        return imports;
    }
    
    public void setImports(List<TyModule> imports) {
        
        this.imports = imports;
    }
}

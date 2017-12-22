package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.scope.Scope;


/**
 * @author Christopher Lutz
 */
public class TyField {
    
    private String name;
    private boolean staticField, constantField;
    private TyUsable container;
    private ProcedureAction defaultValueAction;
    private Scope scope = Scope.PUBLIC;
    
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
}

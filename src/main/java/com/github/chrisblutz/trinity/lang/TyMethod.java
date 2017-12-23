package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.scope.Scope;

import java.util.List;


/**
 * @author Christopher Lutz
 */
public class TyMethod {
    
    public static final ProcedureAction DEFAULT_METHOD = (runtime, thisObj, params) -> TyObject.NONE;
    
    private String name;
    private boolean staticMethod, nativeMethod, secureMethod;
    private TyUsable container;
    private TyProcedure procedure;
    private Scope scope = Scope.PUBLIC;
    private List<TyModule> imports = null;
    
    public TyMethod(String name, boolean staticMethod, boolean nativeMethod, TyUsable container, TyProcedure procedure) {
        
        this(name, staticMethod, nativeMethod, false, container, procedure);
    }
    
    public TyMethod(String name, boolean staticMethod, boolean nativeMethod, boolean secureMethod, TyUsable container, TyProcedure procedure) {
        
        this.name = name;
        this.staticMethod = staticMethod;
        this.nativeMethod = nativeMethod;
        this.secureMethod = secureMethod;
        this.container = container;
        this.procedure = procedure;
    }
    
    public String getName() {
        
        return name;
    }
    
    public boolean isStatic() {
        
        return staticMethod;
    }
    
    public boolean isNative() {
        
        return nativeMethod;
    }
    
    public boolean isSecure() {
        
        return secureMethod;
    }
    
    public void setSecure(boolean secureMethod) {
        
        this.secureMethod = secureMethod;
    }
    
    public TyUsable getContainer() {
        
        return container;
    }
    
    public TyProcedure getProcedure() {
        
        return procedure;
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

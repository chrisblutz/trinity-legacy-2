package com.github.chrisblutz.trinity.interpreter.actions;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;


/**
 * @author Christopher Lutz
 */
public class InterfaceMethodProcedureAction implements ProcedureAction {
    
    private String name;
    
    public InterfaceMethodProcedureAction(String name) {
        
        this.name = name;
    }
    
    @Override
    public TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args) {
        
        Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Cannot call interface method '" + name + ".");
        return TyObject.NONE;
    }
}

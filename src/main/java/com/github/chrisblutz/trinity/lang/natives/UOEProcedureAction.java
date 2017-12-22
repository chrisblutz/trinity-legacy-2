package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;


/**
 * @author Christopher Lutz
 */
public class UOEProcedureAction implements ProcedureAction {
    
    private String operation;
    
    public UOEProcedureAction(String operation) {
        
        this.operation = operation;
    }
    
    public String getOperation() {
        
        return operation;
    }
    
    @Override
    public TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args) {
        
        Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, runtime, "Operation '" + operation + "' not supported.");
        
        return thisObj;
    }
}

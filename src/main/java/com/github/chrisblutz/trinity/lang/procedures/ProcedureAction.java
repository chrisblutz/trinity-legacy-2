package com.github.chrisblutz.trinity.lang.procedures;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;


/**
 * @author Christopher Lutz
 */
public interface ProcedureAction {
    
    TyObject onAction(TyRuntime runtime, TyObject thisObj, TyObject... args);
}

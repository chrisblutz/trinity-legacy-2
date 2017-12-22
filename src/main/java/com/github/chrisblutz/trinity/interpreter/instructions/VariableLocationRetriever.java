package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;


/**
 * @author Christopher Lutz
 */
public interface VariableLocationRetriever {
    
    VariableLocation evaluate(TyObject thisObj, TyRuntime runtime);
}

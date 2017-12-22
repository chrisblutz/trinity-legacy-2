package com.github.chrisblutz.trinity.interpreter.components;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;


/**
 * @author Christopher Lutz
 */
public interface Keyword {
    
    TyObject evaluate(TyObject thisObj, SourceToken token, Location location, TyRuntime runtime);
}

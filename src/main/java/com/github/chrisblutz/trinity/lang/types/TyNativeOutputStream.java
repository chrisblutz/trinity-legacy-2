package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.io.PrintStream;


/**
 * @author Christopher Lutz
 */
public class TyNativeOutputStream extends TyObject {
    
    private PrintStream internal;
    
    public TyNativeOutputStream(PrintStream internal) {
        
        super(ClassRegistry.forName(TrinityNatives.Classes.NATIVE_OUTPUT_STREAM));
        
        this.internal = internal;
    }
    
    public PrintStream getInternal() {
        
        return internal;
    }
}

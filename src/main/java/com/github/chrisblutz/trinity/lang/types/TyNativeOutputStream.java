package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.io.PrintStream;


/**
 * @author Christopher Lutz
 */
public class TyNativeOutputStream extends TyObject {
    
    private PrintStream internal;
    
    public TyNativeOutputStream(PrintStream internal) {
        
        super(ClassRegistry.forName(NativeReferences.Classes.NATIVE_OUTPUT_STREAM, true));
        
        this.internal = internal;
    }
    
    public PrintStream getInternal() {
        
        return internal;
    }
}

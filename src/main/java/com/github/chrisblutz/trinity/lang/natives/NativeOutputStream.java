package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyNativeOutputStream;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.io.PrintStream;


/**
 * @author Christopher Lutz
 */
public class NativeOutputStream {
    
    private static final String CLASS = TrinityNatives.Classes.NATIVE_OUTPUT_STREAM;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "print", (runtime, thisObj, args) -> {
            
            PrintStream stream = TrinityNatives.cast(TyNativeOutputStream.class, thisObj).getInternal();
            String str = TrinityNatives.toString(runtime.getVariable("str"), runtime);
            stream.print(str);
            
            return TyObject.NIL;
        });
        TrinityNatives.registerMethod(CLASS, "flush", (runtime, thisObj, params) -> {
            
            PrintStream stream = TrinityNatives.cast(TyNativeOutputStream.class, thisObj).getInternal();
            stream.flush();
            
            return TyObject.NIL;
        });
        TrinityNatives.registerMethod(CLASS, "close", (runtime, thisObj, params) -> {
            
            PrintStream stream = TrinityNatives.cast(TyNativeOutputStream.class, thisObj).getInternal();
            stream.close();
            
            return TyObject.NIL;
        });
    }
}

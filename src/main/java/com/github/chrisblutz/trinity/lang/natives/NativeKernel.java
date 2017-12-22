package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.Trinity;
import com.github.chrisblutz.trinity.interpreter.errors.TrinityError;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyNativeOutputStream;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeKernel {
    
    private static final String CLASS = TrinityNatives.Classes.KERNEL;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerField(CLASS, "STDOUT", (runtime, thisObj, args) -> new TyNativeOutputStream(System.out));
        TrinityNatives.registerField(CLASS, "STDERR", (runtime, thisObj, args) -> new TyNativeOutputStream(System.err));
        
        TrinityNatives.registerMethod(CLASS, "throw", (runtime, thisObj, params) -> {
            
            TyObject error = runtime.getVariable("error");
            
            if (TrinityNatives.isInstanceOf(error, TrinityNatives.Classes.ERROR)) {
                
                throw new TrinityError(error);
            }
            
            return TyObject.NIL;
        });
        TrinityNatives.registerMethod(CLASS, "exit", (runtime, thisObj, params) -> {
            
            Trinity.exit(TrinityNatives.toInt(runtime.getVariable("code")));
            return TyObject.NIL;
        });
    }
}

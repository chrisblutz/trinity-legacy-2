package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyNativeOutputStream;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.io.PrintStream;


/**
 * @author Christopher Lutz
 */
public class NativeImplNativeOutputStream {
    
    private static final String CLASS = NativeReferences.Classes.NATIVE_OUTPUT_STREAM;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "print", (runtime, thisObj, args) -> {
            
            PrintStream stream = NativeConversion.cast(TyNativeOutputStream.class, thisObj).getInternal();
            String str = NativeConversion.toString(runtime.getVariable("str"), runtime);
            stream.print(str);
            
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "flush", (runtime, thisObj, params) -> {
            
            PrintStream stream = NativeConversion.cast(TyNativeOutputStream.class, thisObj).getInternal();
            stream.flush();
            
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "close", (runtime, thisObj, params) -> {
            
            PrintStream stream = NativeConversion.cast(TyNativeOutputStream.class, thisObj).getInternal();
            stream.close();
            
            return TyObject.NIL;
        });
    }
}

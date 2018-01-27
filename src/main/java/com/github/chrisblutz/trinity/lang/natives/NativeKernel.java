package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.Trinity;
import com.github.chrisblutz.trinity.interpreter.errors.TrinityError;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.stack.StackFrame;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyNativeOutputStream;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;
import java.util.List;


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
        TrinityNatives.registerMethod(CLASS, "caller", (runtime, thisObj, args) -> {
            
            List<TyObject> stackArray = new ArrayList<>();
            
            TrinityStack stack = TrinityStack.getCurrentThreadStack();
            for (int i = 1; i < stack.size(); i++) {
                
                StackFrame frame = stack.get(i);
                
                TyObject file = TrinityNatives.getObjectFor(frame.getFileName());
                TyObject line = TrinityNatives.getObjectFor(frame.getLineNumber());
                TyObject container = TrinityNatives.getObjectFor(frame.getUsable());
                TyObject method = TrinityNatives.getObjectFor(frame.getMethod());
                
                TyObject stackFrame = TrinityNatives.newInstance(TrinityNatives.Classes.STACK_FRAME, runtime, file, line, container, method);
                stackArray.add(stackFrame);
            }
            
            return new TyArray(stackArray);
        });
    }
}

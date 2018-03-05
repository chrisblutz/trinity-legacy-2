package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.Trinity;
import com.github.chrisblutz.trinity.interpreter.errors.TrinityError;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.stack.StackFrame;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyNativeOutputStream;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class NativeImplKernel {
    
    private static final String CLASS = NativeReferences.Classes.KERNEL;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerField(CLASS, "STDOUT", (runtime, thisObj, args) -> new TyNativeOutputStream(System.out));
        NativeInvocation.registerField(CLASS, "STDERR", (runtime, thisObj, args) -> new TyNativeOutputStream(System.err));
        
        NativeInvocation.registerMethod(CLASS, "throw", (runtime, thisObj, params) -> {
            
            TyObject error = runtime.getVariable("error");
            
            if (NativeConversion.isInstanceOf(error, NativeReferences.Classes.ERROR)) {
                
                throw new TrinityError(error);
            }
            
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "exit", (runtime, thisObj, params) -> {
            
            Trinity.exit(NativeConversion.toInt(runtime.getVariable("code")));
            return TyObject.NIL;
        });
        NativeInvocation.registerMethod(CLASS, "caller", (runtime, thisObj, args) -> {
            
            List<TyObject> stackArray = new ArrayList<>();
            
            TrinityStack stack = TrinityStack.getCurrentThreadStack();
            for (int i = 1; i < stack.size(); i++) {
                
                StackFrame frame = stack.get(i);
                
                TyObject file = NativeConversion.getObjectFor(frame.getFileName());
                TyObject line = NativeConversion.getObjectFor(frame.getLineNumber());
                TyObject container = NativeConversion.getObjectFor(frame.getUsable());
                TyObject method = NativeConversion.getObjectFor(frame.getMethod());
                
                TyObject stackFrame = NativeInvocation.newInstance(NativeReferences.Classes.STACK_FRAME, runtime, file, line, container, method);
                stackArray.add(stackFrame);
            }
            
            return new TyArray(stackArray);
        });
    }
}

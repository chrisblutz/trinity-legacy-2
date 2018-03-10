package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.Trinity;
import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.interpreter.Interpreter;
import com.github.chrisblutz.trinity.interpreter.errors.TrinityError;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.TyUsable;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.StackFrame;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.lang.types.*;
import com.github.chrisblutz.trinity.loading.LoadManager;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;
import com.github.chrisblutz.trinity.parser.Parser;
import com.github.chrisblutz.trinity.parser.sources.FileSourceEntry;
import com.github.chrisblutz.trinity.parser.sources.StringArraySourceEntry;
import com.github.chrisblutz.trinity.parser.sources.StringSourceEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        NativeInvocation.registerMethod(CLASS, "eval", (runtime, thisObj, args) -> {
            
            TyObject code = runtime.getVariable("code");
            TyObject argsObj = runtime.getVariable("args");
            TyObject context = runtime.getVariable("context");
            TyObject useStatic = runtime.getVariable("useStatic");
            boolean staticBool = NativeConversion.toBoolean(useStatic);
            
            String[] lines;
            if (code instanceof TyArray) {
                
                TyArray array = (TyArray) code;
                lines = new String[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    
                    lines[i] = NativeConversion.toString(array.getInternal().get(i), runtime);
                }
                
            } else {
                
                lines = new String[]{NativeConversion.toString(code, runtime)};
            }
            
            TyMap argsMap;
            if (argsObj instanceof TyMap) {
                
                argsMap = (TyMap) argsObj;
                
            } else {
                
                Errors.throwError(Errors.Classes.TYPE_ERROR, runtime, "Kernel.eval requires a " + NativeReferences.Classes.MAP + " as its 'args' argument.");
                argsMap = new TyMap(new HashMap<>(), TyMap.getFastStorage());
            }
            
            TyUsable usableContext = null;
            if (staticBool) {
                
                if (context instanceof TyClassObject) {
                    
                    usableContext = ((TyClassObject) context).getInternal();
                    
                } else if (context instanceof TyModuleObject) {
                    
                    usableContext = ((TyModuleObject) context).getInternal();
                }
            }
            
            StringArraySourceEntry entry = new StringArraySourceEntry(lines, "<eval>");
            LoadManager.loadSourceEntry(entry);
            ProcedureAction action = LoadManager.retrieve(entry);
            
            TyRuntime newRuntime = new TyRuntime();
            newRuntime.setCurrentUsable(usableContext);
            newRuntime.setStaticScope(true);
            
            if (!staticBool && context != TyObject.NIL) {
                
                newRuntime.setThis(context);
                newRuntime.setStaticScope(false);
                
            } else if (usableContext != null) {
                
                newRuntime.setStaticScopeObject(context);
            }
            
            Map<TyObject, TyObject> map = argsMap.getInternal();
            for (TyObject name : map.keySet()) {
                
                newRuntime.setVariable(NativeConversion.toString(name, runtime), map.get(name));
            }
            
            return action.onAction(newRuntime, TyObject.NONE);
        });
        NativeInvocation.registerMethod(CLASS, "load", (runtime, thisObj, args) -> {
            
            TyObject file = runtime.getVariable("file");
            String path = NativeConversion.toString(file, runtime);
            
            File actual = new File(path);
            try {
                
                FileSourceEntry entry = new FileSourceEntry(actual);
                LoadManager.load(entry);
                
            } catch (IOException e) {
                
                Errors.throwError(Errors.Classes.LOAD_ERROR, "Could not find file '" + path + "'.");
                
                if (Options.isDebuggingEnabled()) {
                    
                    System.err.println(e.getMessage());
                }
            }
            
            return TyObject.NIL;
        });
    }
}

package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.stack.StackElement;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class NativeError {
    
    private static final String CLASS = TrinityNatives.Classes.ERROR;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "populateStackTrace", (runtime, thisObj, args) -> {
            
            List<TyObject> stackTrace = new ArrayList<>();
            
            TrinityStack stack = TrinityStack.getCurrentThreadStack();
            for (int i = 2 + thisObj.getSuperLevel(); i < stack.size(); i++) {
                
                StackElement element = stack.get(i);
                
                TyObject file = TrinityNatives.getObjectFor(element.getFileName());
                TyObject line = TrinityNatives.getObjectFor(element.getLineNumber());
                
                TyObject stackTraceElement = TrinityNatives.newInstance("Trinity.Errors.StackTraceElement", runtime, file, line);
                stackTrace.add(stackTraceElement);
            }
            
            return new TyArray(stackTrace);
        });
    }
}

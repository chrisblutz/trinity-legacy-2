package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyProcedureObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class NativeProcedure {
    
    private static final String CLASS = TrinityNatives.Classes.PROCEDURE;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerForNativeConstruction(CLASS);
        
        TrinityNatives.registerMethod(CLASS, "initialize", (runtime, thisObj, params) -> {
            
            if (runtime.hasVariable("block")) {
                
                return runtime.getVariable("block");
                
            } else {
                
                return new TyProcedureObject(new TyProcedure((runtime1, thisObj1, params1) -> TyObject.NIL, false), new TyRuntime());
            }
        });
        TrinityNatives.registerMethod(CLASS, "getRequiredArguments", (runtime, thisObj, params) -> TrinityNatives.getArrayFor(TrinityNatives.cast(TyProcedureObject.class, thisObj).getInternal().getMandatoryParameters()));
        TrinityNatives.registerMethod(CLASS, "getOptionalArguments", (runtime, thisObj, params) -> TrinityNatives.getArrayFor(TrinityNatives.cast(TyProcedureObject.class, thisObj).getInternal().getOptionalParameters().keySet()));
        TrinityNatives.registerMethod(CLASS, "getBlockArgument", (runtime, thisObj, params) -> TrinityNatives.getObjectFor(TrinityNatives.cast(TyProcedureObject.class, thisObj).getInternal().getBlockParameter()));
        TrinityNatives.registerMethod(CLASS, "getOverflowArgument", (runtime, thisObj, params) -> TrinityNatives.getObjectFor(TrinityNatives.cast(TyProcedureObject.class, thisObj).getInternal().getOverflowParameter()));
        TrinityNatives.registerMethod(CLASS, "call", (runtime, thisObj, params) -> {
            
            TyProcedureObject obj = (TyProcedureObject) thisObj;
            TyProcedure proc = obj.getInternal();
            TyRuntime newRuntime = obj.getRuntime().clone();
            
            TyObject args = runtime.getVariable("args");
            List<TyObject> procParams = new ArrayList<>();
            if (args instanceof TyArray) {
                
                procParams.addAll(((TyArray) args).getInternal());
            }
            
            TyProcedure subProc = null;
            TyRuntime subRuntime = null;
            if (proc.getBlockParameter() != null) {
                
                TyProcedureObject subBlock = TrinityNatives.cast(TyProcedureObject.class, runtime.getVariable("block"));
                subProc = subBlock.getInternal();
                subRuntime = subBlock.getRuntime();
            }
            
            TyObject result = proc.call(newRuntime, subProc, subRuntime, TyObject.NONE, procParams.toArray(new TyObject[procParams.size()]));
            
            newRuntime.disposeVariablesInto(obj.getRuntime());
            
            obj.setBroken(newRuntime.isBroken());
            
            return result;
        });
        TrinityNatives.registerMethod(CLASS, "isBroken", (runtime, thisObj, params) -> {
            
            TyProcedureObject obj = (TyProcedureObject) thisObj;
            return TyBoolean.valueFor(obj.isBroken());
        });
    }
}

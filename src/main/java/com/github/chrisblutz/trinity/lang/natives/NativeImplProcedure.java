package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyProcedureObject;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class NativeImplProcedure {

    private static final String CLASS = NativeReferences.Classes.PROCEDURE;

    @NativeHook(CLASS)
    public static void register() {

        NativeInvocation.registerForNativeConstruction(CLASS);

        NativeInvocation.registerMethod(CLASS, "initialize", (runtime, thisObj, params) -> {

            if (runtime.hasVariable("block")) {

                return runtime.getVariable("block");

            } else {

                return new TyProcedureObject(new TyProcedure((runtime1, thisObj1, params1) -> TyObject.NIL, false), new TyRuntime());
            }
        });
        NativeInvocation.registerMethod(CLASS, "getRequiredArguments", (runtime, thisObj, params) -> NativeConversion.getArrayFor(NativeConversion.cast(TyProcedureObject.class, thisObj).getInternal().getMandatoryParameters()));
        NativeInvocation.registerMethod(CLASS, "getOptionalArguments", (runtime, thisObj, params) -> NativeConversion.getArrayFor(NativeConversion.cast(TyProcedureObject.class, thisObj).getInternal().getOptionalParameters().keySet()));
        NativeInvocation.registerMethod(CLASS, "getBlockArgument", (runtime, thisObj, params) -> NativeConversion.getObjectFor(NativeConversion.cast(TyProcedureObject.class, thisObj).getInternal().getBlockParameter()));
        NativeInvocation.registerMethod(CLASS, "getOverflowArgument", (runtime, thisObj, params) -> NativeConversion.getObjectFor(NativeConversion.cast(TyProcedureObject.class, thisObj).getInternal().getOverflowParameter()));
        NativeInvocation.registerMethod(CLASS, "call", (runtime, thisObj, params) -> {

            TyProcedureObject obj = (TyProcedureObject) thisObj;
            TyProcedure proc = obj.getInternal();
            TyRuntime newRuntime = obj.getRuntime().cloneWithImports();

            TyObject args = runtime.getVariable("args");
            List<TyObject> procParams = new ArrayList<>();
            if (args instanceof TyArray) {

                procParams.addAll(((TyArray) args).getInternal());
            }

            TyProcedure subProc = null;
            TyRuntime subRuntime = null;
            if (proc.getBlockParameter() != null) {

                TyProcedureObject subBlock = NativeConversion.cast(TyProcedureObject.class, runtime.getVariable("block"));
                subProc = subBlock.getInternal();
                subRuntime = subBlock.getRuntime();
            }

            TyObject result = proc.call(newRuntime, subProc, subRuntime, TyObject.NONE, procParams.toArray(new TyObject[procParams.size()]));

            newRuntime.disposeVariablesInto(obj.getRuntime());

            obj.setBroken(newRuntime.isBroken());

            return result;
        });
        NativeInvocation.registerMethod(CLASS, "isBroken", (runtime, thisObj, params) -> {

            TyProcedureObject obj = (TyProcedureObject) thisObj;
            return TyBoolean.valueFor(obj.isBroken());
        });
    }
}

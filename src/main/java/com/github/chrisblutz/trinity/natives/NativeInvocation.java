package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher
 */
public class NativeInvocation {

    private static Map<String, Map<String, ProcedureAction>> methods = new HashMap<>();
    private static Map<String, Map<String, ProcedureAction>> fields = new HashMap<>();

    private static List<String> nativeConstructors = new ArrayList<>();

    public static void registerMethod(String container, String methodName, ProcedureAction action) {

        ProcedureAction actionWithStackTrace = (runtime, thisObj, params) -> {

            TrinityStack stack = TrinityStack.getCurrentThreadStack();

            stack.add("<native>", -1, container, methodName);

            TyObject result = action.onAction(runtime, thisObj, params);

            stack.pop();

            return result;
        };

        if (!methods.containsKey(container)) {

            methods.put(container, new HashMap<>());
        }

        methods.get(container).put(methodName, actionWithStackTrace);
    }

    public static ProcedureAction getMethodProcedureAction(String container, String methodName) {

        if (methods.containsKey(container) && methods.get(container).containsKey(methodName)) {

            return methods.get(container).get(methodName);

        } else {

            Errors.throwError(Errors.Classes.NATIVE_ERROR, "Native field " + container + "." + methodName + " not implemented.");
            return null;
        }
    }

    public static void registerField(String container, String varName, ProcedureAction action) {

        if (!fields.containsKey(container)) {

            fields.put(container, new HashMap<>());
        }

        fields.get(container).put(varName, action);
    }

    public static ProcedureAction getFieldProcedureAction(String container, String varName) {

        if (fields.containsKey(container) && fields.get(container).containsKey(varName)) {

            return fields.get(container).get(varName);

        } else {

            Errors.throwError(Errors.Classes.NATIVE_ERROR, "Native field " + container + "." + varName + " not implemented.");
            return null;
        }
    }

    public static void registerForNativeConstruction(String className) {

        nativeConstructors.add(className);
    }

    public static boolean isClassNativelyConstructed(TyClass tyClass) {

        return nativeConstructors.contains(tyClass.getFullName());
    }

    public static TyObject newInstance(String className, TyObject... args) {

        return newInstance(className, new TyRuntime(), args);
    }

    public static TyObject newInstance(String className, TyRuntime runtime, TyObject... args) {

        return ClassRegistry.getClass(className).tyInvoke("new", runtime, null, null, TyObject.NONE, args);
    }

    public static TyObject call(TyObject thisObj, String methodName, TyRuntime runtime, TyObject... args) {

        return call(thisObj.getObjectClass().getFullName(), methodName, runtime, thisObj, args);
    }

    public static TyObject call(String container, String methodName, TyRuntime runtime, TyObject thisObj, TyObject... args) {

        return ClassRegistry.getClass(container).tyInvoke(methodName, runtime, null, null, thisObj, args);
    }
}

package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.interpreter.utils.StringUtils;
import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.lang.types.*;

import java.util.*;


/**
 * @author Christopher Lutz
 */
public class TrinityNatives {
    
    public static class Classes {
        
        public static final String ARRAY = "Trinity.Array";
        public static final String BOOLEAN = "Trinity.Boolean";
        public static final String CLASS = "Trinity.Class";
        public static final String ERROR = "Trinity.Error";
        public static final String FIELD = "Trinity.Field";
        public static final String FLOAT = "Trinity.Float";
        public static final String INT = "Trinity.Int";
        public static final String KERNEL = "Trinity.Kernel";
        public static final String LONG = "Trinity.Long";
        public static final String MAP = "Trinity.Map";
        public static final String METHOD = "Trinity.Method";
        public static final String MODULE = "Trinity.Module";
        public static final String NATIVE_OUTPUT_STREAM = "Trinity.NativeOutputStream";
        public static final String NUMERIC = "Trinity.Numeric";
        public static final String OBJECT = "Trinity.Object";
        public static final String PROCEDURE = "Trinity.Procedure";
        public static final String STRING = "Trinity.String";
        public static final String SYSTEM = "Trinity.System";
    }
    
    private static Map<String, Map<String, ProcedureAction>> methods = new HashMap<>();
    private static Map<String, Map<String, ProcedureAction>> fields = new HashMap<>();
    
    private static List<String> nativeConstructors = new ArrayList<>();
    
    public static void registerMethod(String container, String methodName, ProcedureAction action) {
        
        ProcedureAction actionWithStackTrace = (runtime, thisObj, params) -> {
            
            TrinityStack stack = TrinityStack.getCurrentThreadStack();
            
            stack.add("<native> (" + container + "." + methodName + ")", -1);
            
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
    
    public static TyObject getObjectFor(Object obj) {
        
        if (obj == null) {
            
            return TyObject.NIL;
            
        } else if (obj instanceof Integer) {
            
            return new TyInt((Integer) obj);
            
        } else if (obj instanceof Float) {
            
            return new TyFloat((Float) obj);
            
        } else if (obj instanceof Double) {
            
            return new TyFloat((Double) obj);
            
        } else if (obj instanceof Long) {
            
            return new TyLong((Long) obj);
            
        } else if (obj instanceof String) {
            
            return new TyString((String) obj);
            
        } else if (obj instanceof Boolean) {
            
            if ((Boolean) obj) {
                
                return TyBoolean.TRUE;
                
            } else {
                
                return TyBoolean.FALSE;
            }
            
        } else if (obj.getClass().isArray()) {
            
            return getArrayFor((Object[]) obj);
        }
        
        Errors.throwError(Errors.Classes.NATIVE_ERROR, "Trinity does not have native type-conversion utilities for " + obj.getClass() + ".");
        
        return TyObject.NIL;
    }
    
    public static TyArray getArrayFor(Object[] arr) {
        
        List<TyObject> objects = new ArrayList<>();
        
        for (Object o : arr) {
            
            objects.add(getObjectFor(o));
        }
        
        return new TyArray(objects);
    }
    
    public static TyArray getArrayFor(Collection<?> collection) {
        
        List<TyObject> objects = new ArrayList<>();
        
        for (Object o : collection) {
            
            objects.add(getObjectFor(o));
        }
        
        return new TyArray(objects);
    }
    
    public static TyMap getMapFor(Map<?, ?> map, int storageType) {
        
        Map<TyObject, TyObject> objects = TyMap.getMapForStorageType(storageType);
        
        for (Object o : map.keySet()) {
            
            objects.put(getObjectFor(o), getObjectFor(map.get(o)));
        }
        
        return new TyMap(objects, storageType);
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
    
    public static TyObject wrapNumber(double d) {
        
        if (d % 1 == 0) {
            
            if (d > Integer.MAX_VALUE || d < Integer.MIN_VALUE) {
                
                return new TyLong((long) d);
                
            } else {
                
                return new TyInt((int) d);
            }
            
        } else {
            
            return new TyFloat(d);
        }
    }
    
    public static <T extends TyObject> T cast(Class<T> desiredClass, TyObject object) {
        
        if (desiredClass.isInstance(object)) {
            
            return desiredClass.cast(object);
            
        } else {
            
            Errors.throwError(Errors.Classes.TYPE_ERROR, "Unexpected value of type " + object.getObjectClass().getFullName() + " found.");
            
            // This will throw an error, but the line above throws an error for
            // the interpreter to catch, so the program will never reach this point
            return desiredClass.cast(object);
        }
    }
    
    public static double asNumber(TyObject tyObject) {
        
        if (tyObject instanceof TyInt) {
            
            return ((TyInt) tyObject).getInternal();
            
        } else if (tyObject instanceof TyLong) {
            
            return ((TyLong) tyObject).getInternal();
            
        } else if (tyObject instanceof TyFloat) {
            
            return ((TyFloat) tyObject).getInternal();
            
        } else if (tyObject instanceof TyString) {
            
            return StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());
            
        } else {
            
            Errors.throwError(Errors.Classes.TYPE_ERROR, "Expected value of type " + Classes.NUMERIC + ", found " + tyObject.getObjectClass().getFullName() + ".");
        }
        
        return 0;
    }
    
    public static int toInt(TyObject tyObject) {
        
        if (tyObject instanceof TyLong) {
            
            return Math.toIntExact(((TyLong) tyObject).getInternal());
            
        } else if (tyObject instanceof TyFloat) {
            
            return (int) ((TyFloat) tyObject).getInternal();
            
        } else if (tyObject instanceof TyString) {
            
            return (int) StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());
        }
        
        return cast(TyInt.class, tyObject).getInternal();
    }
    
    public static long toLong(TyObject tyObject) {
        
        if (tyObject instanceof TyInt) {
            
            return ((TyInt) tyObject).getInternal();
            
        } else if (tyObject instanceof TyFloat) {
            
            return (long) ((TyFloat) tyObject).getInternal();
            
        } else if (tyObject instanceof TyString) {
            
            return (long) StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());
            
        }
        
        return cast(TyLong.class, tyObject).getInternal();
    }
    
    public static double toFloat(TyObject tyObject) {
        
        if (tyObject instanceof TyInt) {
            
            return ((TyInt) tyObject).getInternal();
            
        } else if (tyObject instanceof TyLong) {
            
            return ((TyLong) tyObject).getInternal();
            
        } else if (tyObject instanceof TyString) {
            
            return StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());
            
        }
        
        return TrinityNatives.cast(TyFloat.class, tyObject).getInternal();
    }
    
    public static String toString(TyObject tyObject, TyRuntime runtime) {
        
        if (tyObject instanceof TyString) {
            
            return ((TyString) tyObject).getInternal();
            
        } else {
            
            TyString tyString = cast(TyString.class, tyObject.tyInvoke("toString", runtime, null, null));
            return tyString.getInternal();
        }
    }
    
    public static boolean toBoolean(TyObject object) {
        
        if (object == TyObject.NIL || object == TyObject.NONE) {
            
            return true;
            
        } else if (object instanceof TyBoolean) {
            
            return ((TyBoolean) object).getInternal();
            
        } else if (object instanceof TyInt) {
            
            return ((TyInt) object).getInternal() != 0;
            
        } else if (object instanceof TyLong) {
            
            return ((TyLong) object).getInternal() != 0;
            
        } else if (object instanceof TyFloat) {
            
            return ((TyFloat) object).getInternal() != 0;
            
        } else {
            
            return true;
        }
    }
    
    public static boolean isInstanceOf(TyObject object, String className) {
        
        return object.getObjectClass().isInstanceOf(ClassRegistry.getClass(className));
    }
    
    public static boolean respondsTo(TyObject object, String method) {
        
        return object.getObjectClass().respondsTo(method);
    }
}

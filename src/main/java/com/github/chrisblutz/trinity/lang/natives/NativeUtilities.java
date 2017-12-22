package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.natives.addins.NativeMath;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * @author Christopher Lutz
 */
public class NativeUtilities {
    
    private static Map<String, List<Method>> nativeHooks = new HashMap<>();
    
    public static void registerHooks(Class registryClass) {
        
        for (Method m : registryClass.getMethods()) {
            
            NativeHook registry = m.getAnnotation(NativeHook.class);
            if (registry != null) {
                
                if (!nativeHooks.containsKey(registry.value())) {
                    
                    nativeHooks.put(registry.value(), new ArrayList<>());
                }
                
                nativeHooks.get(registry.value()).add(m);
            }
        }
    }
    
    public static void registerStandardLibraryHooks() {
        
        registerHooks(NativeArray.class);
        registerHooks(NativeBoolean.class);
        registerHooks(NativeClass.class);
        registerHooks(NativeError.class);
        registerHooks(NativeFloat.class);
        registerHooks(NativeInt.class);
        registerHooks(NativeKernel.class);
        registerHooks(NativeLong.class);
        registerHooks(NativeMap.class);
        registerHooks(NativeMath.class);
        registerHooks(NativeNumeric.class);
        registerHooks(NativeObject.class);
        registerHooks(NativeOutputStream.class);
        registerHooks(NativeProcedure.class);
        registerHooks(NativeString.class);
        registerHooks(NativeSystem.class);
    }
    
    public static void onClassLoad(String name) {
        
        if (nativeHooks.containsKey(name)) {
            
            for (Method m : nativeHooks.get(name)) {
                
                try {
                    
                    m.invoke(null);
                    
                } catch (IllegalAccessException | InvocationTargetException e) {
                    
                    Errors.throwError(Errors.Classes.LOAD_ERROR, "Unable to load native hooks for " + name + ".");
                    
                    if (Options.isDebuggingEnabled()) {
                        
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private static Map<String, UOEProcedureAction> uoeProcedures = new HashMap<>();
    
    public static UOEProcedureAction getDefaultUOEProcedure(String operation) {
        
        if (!uoeProcedures.containsKey(operation)) {
            
            uoeProcedures.put(operation, new UOEProcedureAction(operation));
        }
        
        return uoeProcedures.get(operation);
    }
    
    private static Comparator<TyObject> comparator = null;
    
    public static Comparator getComparator() {
        
        if (comparator == null) {
            
            comparator = (o1, o2) -> TrinityNatives.toInt(o1.tyInvoke("<=>", new TyRuntime(), null, null, o2));
        }
        
        return comparator;
    }
}

package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class FileMethodRegistry {
    
    private static Map<String, Map<String, TyMethod>> methods = new HashMap<>();
    
    public static void register(String filePath, TyMethod method) {
        
        if (method.isStatic()) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, "Methods outside of classes or modules are static by default.");
        }
        
        if (!methods.containsKey(filePath)) {
            
            methods.put(filePath, new HashMap<>());
        }
        
        methods.get(filePath).put(method.getName(), method);
    }
    
    public static boolean hasMethod(String filePath, String method) {
        
        return methods.containsKey(filePath) && methods.get(filePath).containsKey(method);
    }
    
    public static TyObject tyInvoke(String filePath, String method, TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject... args) {
        
        TyMethod tyMethod = methods.get(filePath).get(method);
        
        TyRuntime newRuntime = runtime.clone();
        newRuntime.clearVariables();
        newRuntime.setCurrentUsable(null);
        newRuntime.setImports(tyMethod.getImports());
        newRuntime.setStaticScope(false);
        
        TyObject result = tyMethod.getProcedure().call(newRuntime, subProcedure, subProcedureRuntime, TyObject.NONE, args);
        
        if (newRuntime.isReturning()) {
            
            return newRuntime.getReturnObject();
        }
        
        return result;
    }
}

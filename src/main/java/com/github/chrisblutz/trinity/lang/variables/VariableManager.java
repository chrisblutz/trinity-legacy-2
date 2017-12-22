package com.github.chrisblutz.trinity.lang.variables;

import com.github.chrisblutz.trinity.lang.TyObject;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * @author Christopher Lutz
 */
public class VariableManager {
    
    private static Map<VariableLocation, TyObject> varMap = new WeakHashMap<>();
    private static Map<String, VariableLocation> globalVariables = new HashMap<>();
    
    public static TyObject getVariable(VariableLocation loc) {
        
        return varMap.getOrDefault(loc, TyObject.NIL);
    }
    
    public static void put(VariableLocation loc, TyObject object) {
        
        varMap.put(loc, object);
    }
    
    public static void clearVariable(VariableLocation loc) {
        
        varMap.remove(loc);
    }
    
    public static int size() {
        
        return varMap.size();
    }
    
    public static Map<String, VariableLocation> getGlobalVariables() {
        
        return globalVariables;
    }
    
    public static boolean hasGlobalVariable(String name) {
        
        return getGlobalVariables().containsKey(name);
    }
    
    public static VariableLocation getGlobalVariable(String name) {
        
        return getGlobalVariables().get(name);
    }
    
    public static void setGlobalVariable(String name, TyObject value) {
        
        if (!hasGlobalVariable(name)) {
            
            getGlobalVariables().put(name, new VariableLocation());
        }
        
        VariableManager.put(getGlobalVariable(name), value);
    }
}

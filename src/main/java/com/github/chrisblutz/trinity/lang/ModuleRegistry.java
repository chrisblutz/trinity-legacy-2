package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class ModuleRegistry {
    
    public static final TyModule MISSING_MODULE = new TyModule("nil", "nil");
    
    private static Map<String, TyModule> modules = new HashMap<>();
    
    public static TyModule forName(String moduleName) {
        
        if (!moduleExists(moduleName)) {
            
            String shortModuleName;
            if (moduleName.contains(".")) {
                
                shortModuleName = moduleName.substring(moduleName.lastIndexOf('.') + 1);
                
            } else {
                
                shortModuleName = moduleName;
            }
            
            TyModule tyModule = new TyModule(moduleName, shortModuleName);
            modules.put(moduleName, tyModule);
        }
        
        return modules.get(moduleName);
    }
    
    public static TyModule getModule(String moduleName) {
        
        if (moduleExists(moduleName)) {
            
            return modules.get(moduleName);
            
        } else {
            
            Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, "Module '" + moduleName + "' does not exist.");
            return MISSING_MODULE;
        }
    }
    
    public static boolean moduleExists(String moduleName) {
        
        return modules.containsKey(moduleName);
    }
    
    public static Collection<TyModule> getModules() {
        
        return modules.values();
    }
}

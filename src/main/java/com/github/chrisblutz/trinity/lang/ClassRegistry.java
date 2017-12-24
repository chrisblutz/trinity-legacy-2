package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class ClassRegistry {
    
    public static final TyClass MISSING_CLASS = new TyClass("nil", "nil", null, false);
    
    private static Map<String, TyClass> classes = new HashMap<>();
    
    public static TyClass forName(String className, boolean isFinal) {
        
        if (!classExists(className)) {
            
            String shortClassName;
            if (className.contains(".")) {
                
                shortClassName = className.substring(className.lastIndexOf('.') + 1);
                
            } else {
                
                shortClassName = className;
            }
            
            TyClass tyClass = new TyClass(className, shortClassName, isFinal);
            classes.put(className, tyClass);
        }
        
        return classes.get(className);
    }
    
    public static TyClass getClass(String className) {
        
        if (classExists(className)) {
            
            return classes.get(className);
            
        } else {
            
            Errors.throwError(Errors.Classes.NOT_FOUND_ERROR, "Class '" + className + "' does not exist.");
            return MISSING_CLASS;
        }
    }
    
    public static boolean classExists(String className) {
        
        return classes.containsKey(className);
    }
    
    public static Collection<TyClass> getClasses() {
        
        return classes.values();
    }
}

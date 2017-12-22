package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyField;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.TyModule;
import com.github.chrisblutz.trinity.lang.types.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class NativeStorage {
    
    private static Map<TyClass, TyClassObject> classObjects = new HashMap<>();
    private static Map<TyModule, TyModuleObject> moduleObjects = new HashMap<>();
    private static Map<TyMethod, TyMethodObject> methodObjects = new HashMap<>();
    private static Map<TyField, TyFieldObject> fieldObjects = new HashMap<>();
    
    private static Map<TyClass, TyString> classNames = new HashMap<>();
    private static Map<TyClass, TyString> classSimpleNames = new HashMap<>();
    
    private static TyString nilString = null;
    private static TyFloat e = null, pi = null;
    
    public synchronized static TyClassObject getClassObject(TyClass tyClass) {
        
        if (!classObjects.containsKey(tyClass)) {
            
            classObjects.put(tyClass, new TyClassObject(tyClass));
        }
        
        return classObjects.get(tyClass);
    }
    
    public synchronized static TyModuleObject getModuleObject(TyModule tyModule) {
        
        if (!moduleObjects.containsKey(tyModule)) {
            
            moduleObjects.put(tyModule, new TyModuleObject(tyModule));
        }
        
        return moduleObjects.get(tyModule);
    }
    
    public synchronized static TyMethodObject getMethodObject(TyMethod tyMethod) {
        
        if (!methodObjects.containsKey(tyMethod)) {
            
            methodObjects.put(tyMethod, new TyMethodObject(tyMethod));
        }
        
        return methodObjects.get(tyMethod);
    }
    
    public synchronized static TyFieldObject getFieldObject(TyField tyField) {
        
        if (!fieldObjects.containsKey(tyField)) {
            
            fieldObjects.put(tyField, new TyFieldObject(tyField));
        }
        
        return fieldObjects.get(tyField);
    }
    
    public synchronized static TyString getClassName(TyClass tyClass) {
        
        if (!classNames.containsKey(tyClass)) {
            
            classNames.put(tyClass, new TyString(tyClass.getFullName()));
        }
        
        return classNames.get(tyClass);
    }
    
    public synchronized static TyString getClassSimpleName(TyClass tyClass) {
        
        if (!classSimpleNames.containsKey(tyClass)) {
            
            classSimpleNames.put(tyClass, new TyString(tyClass.getShortName()));
        }
        
        return classSimpleNames.get(tyClass);
    }
    
    public synchronized static TyString getNilString() {
        
        if (nilString == null) {
            
            nilString = new TyString("nil");
        }
        
        return nilString;
    }
    
    public synchronized static TyFloat getE() {
        
        if (e == null) {
            
            e = new TyFloat(Math.E);
        }
        
        return e;
    }
    
    public synchronized static TyFloat getPi() {
        
        if (pi == null) {
            
            pi = new TyFloat(Math.PI);
        }
        
        return pi;
    }
}

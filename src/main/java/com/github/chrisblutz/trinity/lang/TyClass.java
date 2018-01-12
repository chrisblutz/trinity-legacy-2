package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.natives.NativeUtilities;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.lang.scope.Scope;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;
import com.github.chrisblutz.trinity.lang.variables.VariableLocation;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.*;


/**
 * @author Christopher Lutz
 */
public class TyClass extends TyUsable {
    
    private Map<String, TyClass> internalClasses = new HashMap<>();
    private boolean isInterface = false, isFinal = false;
    private TyMethod constructor;
    private TyClass superclass;
    private TyClass[] superinterfaces = new TyClass[0];
    private List<TyClass> subclasses = new ArrayList<>();
    
    private Map<String, TyMethod> methods = new HashMap<>();
    private Map<String, TyField> variables = new HashMap<>();
    private Map<TyField, VariableLocation> classVariables = new HashMap<>();
    private Map<TyObject, Map<TyField, VariableLocation>> instanceVariables = new WeakHashMap<>();
    
    public TyClass(String fullName, String shortName) {
        
        this(fullName, shortName, fullName.contentEquals(TrinityNatives.Classes.OBJECT) ? null : ClassRegistry.forName(TrinityNatives.Classes.OBJECT, false), false);
    }
    
    public TyClass(String fullName, String shortName, boolean isFinal) {
        
        this(fullName, shortName, fullName.contentEquals(TrinityNatives.Classes.OBJECT) ? null : ClassRegistry.forName(TrinityNatives.Classes.OBJECT, false), isFinal);
    }
    
    public TyClass(String fullName, String shortName, TyClass superclass, boolean isFinal) {
        
        super(fullName, shortName);
        
        if (superclass != null) {
            
            setSuperclass(superclass);
        }
        
        this.isFinal = isFinal;
        
        NativeUtilities.onClassLoad(fullName);
    }
    
    protected void inherit(TyClass subclass) {
        
        subclasses.add(subclass);
    }
    
    public boolean isFinal() {
        
        return isFinal;
    }
    
    public boolean isInterface() {
        
        return isInterface;
    }
    
    public void setInterface(boolean isInterface) {
        
        this.isInterface = isInterface;
    }
    
    public TyClass getSuperclass() {
        
        return superclass;
    }
    
    public void setSuperclass(TyClass superclass) {
        
        this.superclass = superclass;
        superclass.inherit(this);
    }
    
    public TyClass[] getSuperinterfaces() {
        
        return superinterfaces;
    }
    
    public void setSuperinterfaces(TyClass[] superinterfaces) {
        
        this.superinterfaces = superinterfaces;
    }
    
    public boolean isInstanceOf(TyClass type) {
        
        boolean result = false;
        if (type == this) {
            
            return true;
            
        } else if (getSuperclass() != null) {
            
            result = getSuperclass().isInstanceOf(type);
        }
        
        for (int i = 0; !result && i < getSuperinterfaces().length; i++) {
            
            result = getSuperinterfaces()[i].isInstanceOf(type);
        }
        
        return result;
    }
    
    public List<TyClass> getSubclasses() {
        
        return subclasses;
    }
    
    public TyMethod getConstructor() {
        
        return constructor;
    }
    
    @Override
    public boolean hasInternalClass(String name) {
        
        return getInternalClasses().containsKey(name);
    }
    
    @Override
    public Map<String, TyClass> getInternalClasses() {
        
        return internalClasses;
    }
    
    @Override
    public void addInternalClass(TyClass tyClass) {
        
        getInternalClasses().put(tyClass.getShortName(), tyClass);
    }
    
    @Override
    public boolean hasInternalModule(String name) {
        
        return false;
    }
    
    @Override
    public Map<String, TyModule> getInternalModules() {
        
        return new HashMap<>();
    }
    
    @Override
    public void addInternalModule(TyModule tyModule) {
        
        Errors.throwError(Errors.Classes.SCOPE_ERROR, "No modules allowed within classes.");
    }
    
    @Override
    public boolean hasMethod(String name, boolean isInstance) {
        
        return (methods.containsKey(name) && (isInstance || methods.get(name).isStatic())) || (getSuperclass() != null && getSuperclass().hasMethod(name, isInstance));
    }
    
    @Override
    public Map<String, TyMethod> getMethods() {
        
        return methods;
    }
    
    @Override
    public TyMethod getMethod(String name) {
        
        if (hasMethod(name, true)) {
            
            if (methods.containsKey(name)) {
                
                return methods.get(name);
                
            } else if (getSuperclass() != null) {
                
                return getSuperclass().getMethod(name);
            }
        }
        
        return null;
    }
    
    @Override
    public void addMethod(TyMethod method) {
        
        if (methods.containsKey(method.getName()) && methods.get(method.getName()).isSecure()) {
            
            return;
            
        } else if (hasMethod(method.getName(), true) && getMethod(method.getName()).isFinal()) {
            
            throwFinalMethodError(method.getName());
        }
        
        methods.put(method.getName(), method);
        
        if (method.getName().contentEquals("initialize")) {
            
            constructor = method;
        }
    }
    
    @Override
    public boolean hasField(String name, boolean isInstance) {
        
        return (variables.containsKey(name) && (isInstance || classVariables.containsKey(variables.get(name)))) || (getSuperclass() != null && getSuperclass().hasField(name, isInstance));
    }
    
    @Override
    public VariableLocation getField(String name, boolean isInstance, TyObject instance) {
        
        TyField field = getFieldData(name);
        if (field != null) {
            
            if (hasField(name, false)) {
                
                return classVariables.get(field);
                
            } else if (isInstance) {
                
                return instanceVariables.
                        get(instance).
                        get(field);
            }
            
        } else if (getSuperclass() != null && getSuperclass().hasField(name, isInstance)) {
            
            return getSuperclass().getField(name, isInstance, instance);
        }
        
        throwFieldNotFoundError(name);
        return null;
    }
    
    @Override
    public TyField getFieldData(String name) {
        
        return variables.get(name);
    }
    
    @Override
    public void addField(TyField field, TyRuntime runtime) {
        
        variables.put(field.getName(), field);
        
        if (field.isStatic()) {
            
            VariableLocation location = new VariableLocation();
            location.setContainer(this);
            location.setConstant(field.isConstant());
            location.setScope(field.getScope());
            VariableManager.put(location, field.getDefaultValueAction().onAction(runtime, TyObject.NONE));
            classVariables.put(field, location);
        }
    }
    
    public void initializeInstanceFields(TyObject object, TyRuntime runtime) {
        
        instanceVariables.put(object, new HashMap<>());
        Map<TyField, VariableLocation> variableMap = instanceVariables.get(object);
        
        for (TyField field : variables.values()) {
            
            if (!field.isStatic()) {
                
                ProcedureAction defaultAction = field.getDefaultValueAction();
                
                TyObject value = TyObject.NIL;
                if (defaultAction != null) {
                    
                    TyRuntime newRuntime = runtime.clone();
                    newRuntime.clearVariables();
                    newRuntime.setImports(field.getImports());
                    newRuntime.setThis(object);
                    newRuntime.setStaticScope(false);
                    newRuntime.setCurrentUsable(this);
                    
                    value = defaultAction.onAction(newRuntime, TyObject.NONE);
                }
                
                VariableLocation location = new VariableLocation();
                location.setContainer(this);
                location.setConstant(field.isConstant());
                location.setScope(field.getScope());
                VariableManager.put(location, value);
                variableMap.put(field, location);
            }
        }
        
        if (getSuperclass() != null) {
            
            getSuperclass().initializeInstanceFields(object, runtime);
        }
    }
    
    @Override
    public TyObject tyInvoke(TyUsable origin, String method, TyRuntime runtime, TyProcedure subProcedure, TyRuntime subProcedureRuntime, TyObject thisObj, TyObject... args) {
        
        if (method.contentEquals("new")) {
            
            if (constructor == null) {
                
                // Default constructor
                TyRuntime newRuntime = runtime.clone();
                newRuntime.clearVariables();
                
                TyObject newObject = new TyObject(this);
                
                newRuntime.setThis(newObject);
                newRuntime.setStaticScope(false);
                newRuntime.setCurrentUsable(this);
                
                initializeInstanceFields(newObject, newRuntime);
                
                return newObject;
                
            } else {
                
                Scope scope = constructor.getScope();
                boolean run = checkScope(scope, constructor, runtime);
                
                if (run) {
                    
                    TyRuntime newRuntime = runtime.clone();
                    newRuntime.clearVariables();
                    newRuntime.setImports(constructor.getImports());
                    
                    TyObject newObject = new TyObject(this);
                    
                    newRuntime.setThis(newObject);
                    newRuntime.setStaticScope(false);
                    newRuntime.setCurrentUsable(this);
                    
                    initializeInstanceFields(newObject, newRuntime);
                    
                    TyObject obj = constructor.getProcedure().call(newRuntime, subProcedure, subProcedureRuntime, newObject, args);
                    
                    if (newRuntime.isReturning()) {
                        
                        Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Cannot return a value from a constructor.");
                        
                    } else if (TrinityNatives.isClassNativelyConstructed(this) && obj.getObjectClass() == this) {
                        
                        newObject = obj;
                    }
                    
                    return newObject;
                    
                } else {
                    
                    Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Constructor cannot be accessed from this context because it is marked '" + scope.toString() + "'.");
                    
                    return TyObject.NONE;
                }
            }
            
        } else if (methods.containsKey(method)) {
            
            TyMethod tyMethod = methods.get(method);
            
            Scope scope = tyMethod.getScope();
            boolean run = checkScope(scope, tyMethod, runtime);
            
            if (run) {
                
                TyRuntime newRuntime = runtime.clone();
                newRuntime.clearVariables();
                newRuntime.setCurrentUsable(this);
                newRuntime.setImports(tyMethod.getImports());
                
                if (tyMethod.isStatic()) {
                    
                    newRuntime.setStaticScope(true);
                    newRuntime.setStaticScopeObject(new TyStaticUsableObject(null, this));
                    
                } else {
                    
                    if (thisObj == TyObject.NONE) {
                        
                        throwInstanceMethodStaticScopeError(method, runtime);
                    }
                    
                    newRuntime.setThis(thisObj);
                    newRuntime.setStaticScope(false);
                }
                
                TyObject result = tyMethod.getProcedure().call(newRuntime, subProcedure, subProcedureRuntime, thisObj, args);
                
                if (newRuntime.isReturning()) {
                    
                    return newRuntime.getReturnObject();
                }
                
                return result;
                
            } else {
                
                throwMethodScopeError(method, scope, runtime);
                
                return TyObject.NONE;
            }
            
        } else if (runtime.getCurrentLocation() != null && checkKernelOrFilePlacementValidity(runtime, thisObj) && FileMethodRegistry.hasMethod(runtime.getCurrentLocation().getFilePath(), method)) {
            
            return FileMethodRegistry.tyInvoke(runtime.getCurrentLocation().getFilePath(), method, runtime, subProcedure, subProcedureRuntime, args);
            
        } else if (getSuperclass() != null) {
            
            return getSuperclass().tyInvoke(origin, method, runtime, subProcedure, subProcedureRuntime, thisObj, args);
            
        } else if (checkKernelOrFilePlacementValidity(runtime, thisObj) && ClassRegistry.getClass(TrinityNatives.Classes.KERNEL).getMethods().containsKey(method)) {
            
            return ClassRegistry.getClass(TrinityNatives.Classes.KERNEL).tyInvoke(origin, method, runtime, subProcedure, subProcedureRuntime, thisObj, args);
            
        } else {
            
            throwMethodNotFoundError(method, origin, runtime);
        }
        
        return TyObject.NONE;
    }
    
    private boolean checkScope(Scope scope, TyMethod method, TyRuntime runtime) {
        
        switch (scope) {
            
            case PUBLIC:
                
                return true;
            
            case MODULE_PROTECTED:
                
                return method.getContainer().getParentModule() == runtime.getCurrentModule();
            
            case PROTECTED:
                
                return runtime.getCurrentUsable() instanceof TyClass && method.getContainer() instanceof TyClass && ((TyClass) runtime.getCurrentUsable()).isInstanceOf((TyClass) method.getContainer());
            
            case PRIVATE:
                
                return method.getContainer() == runtime.getCurrentUsable();
            
            default:
                
                return false;
        }
    }
    
    private boolean checkKernelOrFilePlacementValidity(TyRuntime runtime, TyObject thisObj) {
        
        return (!runtime.isStaticScope() && runtime.isInitialStatement()) || thisObj == TyObject.NONE;
    }
}

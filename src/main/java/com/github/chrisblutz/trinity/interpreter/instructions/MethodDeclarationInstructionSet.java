package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.Parameters;
import com.github.chrisblutz.trinity.interpreter.actions.InterfaceMethodProcedureAction;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;


/**
 * @author Christopher Lutz
 */
public class MethodDeclarationInstructionSet extends InstructionSet {
    
    private String name;
    private boolean isStatic, isSecure;
    private Parameters parameters;
    private ProcedureAction body;
    
    public MethodDeclarationInstructionSet(String name, boolean isStatic, boolean isSecure, Parameters parameters, ProcedureAction body, Location location) {
        
        super(new Instruction[0], location);
        
        this.name = name;
        this.isStatic = isStatic;
        this.isSecure = isSecure;
        this.parameters = parameters;
        this.body = body;
    }
    
    public String getName() {
        
        return name;
    }
    
    public boolean isStatic() {
        
        return isStatic;
    }
    
    public boolean isSecure() {
        
        return isSecure;
    }
    
    public Parameters getParameters() {
        
        return parameters;
    }
    
    public ProcedureAction getBody() {
        
        return body;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        if (runtime.getCurrentUsable() instanceof TyClass && ((TyClass) runtime.getCurrentUsable()).isInterface() && getBody() != TyMethod.DEFAULT_METHOD) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Methods within interfaces cannot have a body.");
            
        } else if (runtime.getCurrentUsable() instanceof TyClass && ((TyClass) runtime.getCurrentUsable()).isInterface() && getName().contentEquals("initialize")) {
            
            Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Interfaces cannot declare an 'initialize' method.");
            
        } else if (runtime.getCurrentUsable() instanceof TyClass && ((TyClass) runtime.getCurrentUsable()).isInterface()) {
            
            body = new InterfaceMethodProcedureAction(getName());
        }
        
        TyProcedure methodProcedure = new TyProcedure(getBody(), getParameters().getMandatoryParameters(), getParameters().getOptionalParameters(), getParameters().getBlockParameter(), getParameters().getOverflowParameter(), true);
        
        TyMethod method = new TyMethod(getName(), isStatic(), false, isSecure(), runtime.getCurrentUsable(), methodProcedure);
        method.setScope(runtime.getCurrentScope());
        method.setImports(runtime.getImports());
        
        runtime.getCurrentUsable().addMethod(method);
        
        return TyObject.NONE;
    }
}

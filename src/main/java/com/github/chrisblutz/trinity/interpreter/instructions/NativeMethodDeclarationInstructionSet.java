package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.Parameters;
import com.github.chrisblutz.trinity.lang.TyMethod;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.procedures.TyProcedure;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeMethodDeclarationInstructionSet extends InstructionSet {
    
    private String name;
    private boolean isStatic, isSecure;
    private Parameters parameters;
    
    public NativeMethodDeclarationInstructionSet(String name, boolean isStatic, boolean isSecure, Parameters parameters, Location location) {
        
        super(new Instruction[0], location);
        
        this.name = name;
        this.isStatic = isStatic;
        this.isSecure = isSecure;
        this.parameters = parameters;
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
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        ProcedureAction body = TrinityNatives.getMethodProcedureAction(runtime.getCurrentUsable().getFullName(), getName());
        
        TyProcedure methodProcedure = new TyProcedure(body, getParameters().getMandatoryParameters(), getParameters().getOptionalParameters(), getParameters().getBlockParameter(), getParameters().getOverflowParameter(), true);
        
        TyMethod method = new TyMethod(getName(), isStatic(), true, isSecure(), runtime.getCurrentUsable(), methodProcedure);
        method.setScope(runtime.getCurrentScope());
        
        runtime.getCurrentUsable().addMethod(method);
        
        return TyObject.NONE;
    }
}

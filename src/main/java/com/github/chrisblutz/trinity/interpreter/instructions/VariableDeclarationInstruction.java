package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyField;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class VariableDeclarationInstruction extends Instruction {
    
    private String name;
    private boolean isNative, isStatic, isConstant;
    private ProcedureAction action;
    
    public VariableDeclarationInstruction(String name, boolean isNative, boolean isStatic, boolean isConstant, ProcedureAction action, Location location) {
        
        super(location);
        
        this.name = name;
        this.isNative = isNative;
        this.isStatic = isStatic;
        this.isConstant = isConstant;
        this.action = action;
    }
    
    public String getName() {
        
        return name;
    }
    
    public boolean isNative() {
        
        return isNative;
    }
    
    public boolean isStatic() {
        
        return isStatic;
    }
    
    public boolean isConstant() {
        
        return isConstant;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        ProcedureAction action = getAction();
        if (isNative()) {
            
            action = TrinityNatives.getFieldProcedureAction(runtime.getCurrentUsable().getFullName(), getName());
            
        } else if (action == null) {
            
            action = (runtime1, thisObj1, args) -> TyObject.NIL;
        }
        
        TyField field = new TyField(getName(), isStatic(), isConstant(), runtime.getCurrentUsable(), action);
        runtime.getCurrentUsable().addField(field, runtime);
        
        return TyObject.NONE;
    }
}

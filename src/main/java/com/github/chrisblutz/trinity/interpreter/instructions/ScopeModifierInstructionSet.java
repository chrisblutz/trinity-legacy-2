package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.scope.Scope;


/**
 * @author Christopher Lutz
 */
public class ScopeModifierInstructionSet extends InstructionSet {
    
    private Scope modifier;
    private ProcedureAction next;
    
    public ScopeModifierInstructionSet(String modifier, ProcedureAction next, Location location) {
        
        super(new Instruction[0], location);
        
        this.modifier = Scope.valueOf(modifier.toUpperCase());
        this.next = next;
    }
    
    public Scope getModifier() {
        
        return modifier;
    }
    
    public ProcedureAction getNext() {
        
        return next;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        if (getNext() != null) {
            
            TyRuntime newRuntime = runtime.cloneWithImports();
            newRuntime.setCurrentScope(getModifier());
            
            TyObject result = getNext().onAction(newRuntime, TyObject.NONE);
            
            newRuntime.disposeInto(runtime);
            
            return result;
        }
        
        return TyObject.NIL;
    }
}

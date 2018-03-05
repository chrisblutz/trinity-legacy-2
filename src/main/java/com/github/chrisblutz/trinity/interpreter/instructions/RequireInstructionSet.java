package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.RequirementManager;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.natives.NativeConversion;


/**
 * @author Christopher Lutz
 */
public class RequireInstructionSet extends InstructionSet {
    
    private InstructionSet expression;
    
    public RequireInstructionSet(InstructionSet expression, Location location) {
        
        super(new Instruction[0], location);
        
        this.expression = expression;
    }
    
    public InstructionSet getExpression() {
        
        return expression;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        TyObject object = getExpression().evaluate(TyObject.NONE, runtime);
        String name = NativeConversion.toString(object, runtime);
        
        RequirementManager.require(name, getLocation().getFilePath());
        
        return TyObject.NONE;
    }
}

package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class RangeCreationInstruction extends Instruction {
    
    private Token divider;
    private InstructionSet beginningValue, endValue;
    
    public RangeCreationInstruction(Token divider, InstructionSet beginningValue, InstructionSet endValue, Location location) {
        
        super(location);
        
        this.divider = divider;
        this.beginningValue = beginningValue;
        this.endValue = endValue;
    }
    
    public Token getDivider() {
        
        return divider;
    }
    
    public InstructionSet getBeginningValue() {
        
        return beginningValue;
    }
    
    public InstructionSet getEndValue() {
        
        return endValue;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        TyObject begin = getBeginningValue().evaluate(TyObject.NONE, runtime);
        TyObject end = getEndValue().evaluate(TyObject.NONE, runtime);
        boolean exclude = getDivider() == Token.TRIPLE_DOT_OP;
        
        return TrinityNatives.newInstance("Trinity.Range", runtime, begin, end, TrinityNatives.getObjectFor(exclude));
    }
}

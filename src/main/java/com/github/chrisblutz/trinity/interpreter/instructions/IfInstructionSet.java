package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class IfInstructionSet extends InstructionSet {
    
    private Token ifToken;
    private InstructionSet expression;
    private ProcedureAction action;
    private IfInstructionSet child = null;
    
    public IfInstructionSet(Token ifToken, InstructionSet expression, ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.ifToken = ifToken;
        this.expression = expression;
        this.action = action;
    }
    
    public Token getIfToken() {
        
        return ifToken;
    }
    
    public InstructionSet getExpression() {
        
        return expression;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    public IfInstructionSet getChild() {
        
        return child;
    }
    
    public void setChild(IfInstructionSet child) {
        
        this.child = child;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        TyRuntime newRuntime = runtime.cloneWithImports();
        
        TyObject result = TyObject.NONE;
        
        if (getIfToken() == Token.IF || getIfToken() == Token.ELSIF) {
            
            boolean expression = TrinityNatives.toBoolean(getExpression().evaluate(TyObject.NONE, newRuntime));
            
            if (expression) {
                
                if (getAction() != null) {
                    
                    result = getAction().onAction(newRuntime, TyObject.NONE);
                }
                
            } else if (getChild() != null) {
                
                result = getChild().evaluate(TyObject.NONE, newRuntime);
            }
            
        } else if (getIfToken() == Token.ELSE && getAction() != null) {
            
            result = getAction().onAction(newRuntime, TyObject.NONE);
        }
        
        newRuntime.disposeInto(runtime);
        
        return result;
    }
}

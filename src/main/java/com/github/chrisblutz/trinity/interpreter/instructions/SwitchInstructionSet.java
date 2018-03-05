package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class SwitchInstructionSet extends InstructionSet {
    
    private Token token;
    private InstructionSet expression;
    private ProcedureAction action;
    private SwitchInstructionSet child = null;
    
    public SwitchInstructionSet(Token token, InstructionSet expression, ProcedureAction action, Location location) {
        
        super(new Instruction[0], location);
        
        this.token = token;
        this.expression = expression;
        this.action = action;
    }
    
    public Token getToken() {
        
        return token;
    }
    
    public InstructionSet getExpression() {
        
        return expression;
    }
    
    public ProcedureAction getAction() {
        
        return action;
    }
    
    public SwitchInstructionSet getChild() {
        
        return child;
    }
    
    public void setChild(SwitchInstructionSet child) {
        
        this.child = child;
    }
    
    @Override
    public TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation(runtime);
        
        boolean chaining = runtime.isSwitchChaining();
        runtime.setSwitchChaining(false);
        TyRuntime newRuntime = runtime.cloneWithImports();
        
        TyObject result = TyObject.NONE;
        
        if (getToken() == Token.SWITCH) {
            
            TyObject expression = getExpression().evaluate(TyObject.NONE, newRuntime);
            newRuntime.setSwitchObject(expression);
            
            if (getChild() != null) {
                
                result = getChild().evaluate(TyObject.NONE, runtime);
            }
            
        } else if (getToken() == Token.CASE) {
            
            TyObject expression = getExpression().evaluate(TyObject.NONE, newRuntime);
            
            if (chaining || NativeConversion.toBoolean(runtime.getSwitchObject().tyInvoke("==", runtime, null, null, expression))) {
                
                newRuntime.setSwitchChaining(true);
                
                if (getAction() != null) {
                    
                    result = getAction().onAction(newRuntime, TyObject.NONE);
                }
                
                if (!newRuntime.isBroken() && !newRuntime.isReturning() && getChild() != null) {
                    
                    result = getChild().evaluate(TyObject.NONE, newRuntime);
                }
                
            } else if (getChild() != null) {
                
                result = getChild().evaluate(TyObject.NONE, newRuntime);
            }
            
        } else if (getToken() == Token.DEFAULT && getAction() != null) {
            
            result = getAction().onAction(newRuntime, TyObject.NONE);
        }
        
        if (newRuntime.isBroken()) {
            
            newRuntime.setBroken(false);
        }
        
        newRuntime.disposeInto(runtime);
        
        return result;
    }
}

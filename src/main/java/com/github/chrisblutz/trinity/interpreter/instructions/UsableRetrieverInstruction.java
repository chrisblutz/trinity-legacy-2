package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;
import com.github.chrisblutz.trinity.natives.NativeStorage;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class UsableRetrieverInstruction extends Instruction {
    
    private Token token;
    
    public UsableRetrieverInstruction(Token token, Location location) {
        
        super(location);
        
        this.token = token;
    }
    
    public Token getToken() {
        
        return token;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        if (thisObj instanceof TyStaticUsableObject) {
            
            TyStaticUsableObject usable = (TyStaticUsableObject) thisObj;
            
            if (token == Token.CLASS && usable.asClass() != null) {
                
                return NativeStorage.getClassObject(usable.asClass());
                
            } else if (token == Token.MODULE && usable.asModule() != null) {
                
                return NativeStorage.getModuleObject(usable.asModule());
            }
        }
        
        Errors.throwError(Errors.Classes.TYPE_ERROR, runtime, "Cannot retrieve a " + token.getLiteral() + " here.");
        return TyObject.NONE;
    }
}

package com.github.chrisblutz.trinity.interpreter.facets;

import com.github.chrisblutz.trinity.interpreter.Keywords;
import com.github.chrisblutz.trinity.interpreter.utils.StringUtils;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyStaticUsableObject;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.parser.tokens.Token;


/**
 * @author Christopher Lutz
 */
public class KeywordFacets {
    
    public static void registerFacets() {
        
        Keywords.register(Token.LITERAL_STRING, (thisObj, token, location, runtime) -> new TyString(token.getContents()));
        Keywords.register(Token.NUMERIC_STRING, (thisObj, token, location, runtime) -> TrinityNatives.wrapNumber(StringUtils.parseStringToDouble(token.getContents())));
        
        Keywords.register(Token.NIL, (thisObj, token, location, runtime) -> TyObject.NIL);
        Keywords.register(Token.TRUE, (thisObj, token, location, runtime) -> TyBoolean.TRUE);
        Keywords.register(Token.FALSE, (thisObj, token, location, runtime) -> TyBoolean.FALSE);
        
        Keywords.register(Token.SUPER, (thisObj, token, location, runtime) -> {
            
            if (runtime.isStaticScope()) {
                
                if (runtime.getCurrentUsable() instanceof TyClass) {
                    
                    TyClass thisClass = (TyClass) runtime.getCurrentUsable();
                    
                    if (thisClass.getSuperclass() != null) {
                        
                        return new TyStaticUsableObject(null, thisClass.getSuperclass());
                        
                    } else {
                        
                        Errors.throwError(Errors.Classes.INHERITANCE_ERROR, runtime, thisClass.getFullName() + " has no superclass.");
                        return TyObject.NONE;
                    }
                    
                } else {
                    
                    Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Cannot access 'super' outside of a class.");
                    return TyObject.NONE;
                }
                
            } else {
                
                TyObject thisPointer = runtime.getThis();
                
                if (thisPointer.getObjectClass().getSuperclass() != null) {
                    
                    thisPointer.incrementSuperLevel();
                    return thisPointer;
                    
                } else {
                    
                    Errors.throwError(Errors.Classes.INHERITANCE_ERROR, runtime, thisPointer.getObjectClass().getFullName() + " has no superclass.");
                    return TyObject.NONE;
                }
            }
        });
        Keywords.register(Token.THIS, (thisObj, token, location, runtime) -> {
            
            if (runtime.isStaticScope() || runtime.getThis() == TyObject.NONE) {
                
                Errors.throwError(Errors.Classes.SCOPE_ERROR, runtime, "Cannot access 'this' in a static context.");
                return TyObject.NONE;
            }
            
            return runtime.getThis();
        });
        
        Keywords.register(Token.BLOCK_CHECK, (thisObj, info, location, runtime) -> TyBoolean.valueFor(runtime.getProcedure() != null));
        
        Keywords.register(Token.__FILE__, (thisObj, info, location, runtime) -> TrinityNatives.getObjectFor(location.getFilePath()));
        Keywords.register(Token.__LINE__, (thisObj, info, location, runtime) -> new TyInt(location.getLineNumber()));
        
        Keywords.register(Token.BREAK, (thisObj, info, location, runtime) -> {
            
            runtime.setBroken(true);
            return TyObject.NONE;
        });
    }
}

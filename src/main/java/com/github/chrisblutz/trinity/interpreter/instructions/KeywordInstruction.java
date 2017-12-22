package com.github.chrisblutz.trinity.interpreter.instructions;

import com.github.chrisblutz.trinity.interpreter.Keywords;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;


/**
 * @author Christopher Lutz
 */
public class KeywordInstruction extends Instruction {
    
    private SourceToken keyword;
    
    public KeywordInstruction(SourceToken keyword, Location location) {
        
        super(location);
        
        this.keyword = keyword;
    }
    
    public SourceToken getKeyword() {
        
        return keyword;
    }
    
    @Override
    protected TyObject evaluate(TyObject thisObj, TyRuntime runtime) {
        
        updateLocation();
        
        return Keywords.getKeyword(getKeyword().getToken()).evaluate(thisObj, getKeyword(), getLocation(), runtime);
    }
}

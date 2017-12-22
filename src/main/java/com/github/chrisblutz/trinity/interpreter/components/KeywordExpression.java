package com.github.chrisblutz.trinity.interpreter.components;

import com.github.chrisblutz.trinity.interpreter.Interpreter;
import com.github.chrisblutz.trinity.interpreter.KeywordExpressions;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.Arrays;


/**
 * @author Christopher Lutz
 */
public abstract class KeywordExpression {
    
    private int componentCount;
    private boolean rigid, addDirect;
    private Token delimiter;
    
    private KeywordExpressionConstraint constraint = null;
    
    protected KeywordExpression(int componentCount, boolean rigid, boolean addDirect, Token delimiter) {
        
        this.componentCount = componentCount;
        this.rigid = rigid;
        this.addDirect = addDirect;
        this.delimiter = delimiter;
    }
    
    public int getComponentCount() {
        
        return componentCount;
    }
    
    public boolean isRigid() {
        
        return rigid;
    }
    
    public boolean isAddDirect() {
        
        return addDirect;
    }
    
    public Token getDelimiter() {
        
        return delimiter;
    }
    
    public boolean hasConstraint() {
        
        return constraint != null;
    }
    
    public KeywordExpressionConstraint getConstraint() {
        
        return constraint;
    }
    
    public void setConstraint(KeywordExpressionConstraint constraint) {
        
        this.constraint = constraint;
    }
    
    public boolean checkConstraint(Block block) {
        
        return !hasConstraint() || Arrays.asList(getConstraint().getTokens()).contains(KeywordExpressions.getPreviousToken(block));
    }
    
    public boolean runConstraint(Block block, InstructionSet set) {
        
        return !hasConstraint() || getConstraint().check(set, KeywordExpressions.getPreviousSet(block));
    }
    
    public abstract InstructionSet interpret(SourceToken token, InstructionSet[] sets, ProcedureAction next, Interpreter interpreter, Location location);
}

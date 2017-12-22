package com.github.chrisblutz.trinity.parser.blocks;

import com.github.chrisblutz.trinity.parser.SourceEntry;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;

import java.util.ArrayList;


/**
 * @author Christopher Lutz
 */
public class Statement extends ArrayList<SourceToken> implements BlockItem {
    
    private SourceEntry sourceEntry;
    private int lineNumber;
    
    public Statement(SourceEntry sourceEntry, int lineNumber) {
        
        this.sourceEntry = sourceEntry;
        this.lineNumber = lineNumber;
    }
    
    public SourceEntry getSourceEntry() {
        
        return sourceEntry;
    }
    
    public int getLineNumber() {
    
        return lineNumber;
    }
    
    @Override
    public String toString(String indent) {
        
        StringBuilder str = new StringBuilder(indent + "Statement [" + getSourceEntry().getFileName() + "]");
        for (SourceToken token : this) {
            
            str.append("\n\t").append(indent).append(token.toString());
        }
        
        return str.toString();
    }
    
    @Override
    public String toString() {
        
        return toString("");
    }
}

package com.github.chrisblutz.trinity.parser.blocks;

import com.github.chrisblutz.trinity.parser.SourceEntry;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class Block extends ArrayList<BlockItem> implements BlockItem {
    
    private SourceEntry sourceEntry;
    private List<SourceToken> header = null;
    
    public Block(SourceEntry sourceEntry) {
        
        this.sourceEntry = sourceEntry;
    }
    
    public SourceEntry getSourceEntry() {
        
        return sourceEntry;
    }
    
    public boolean hasHeader() {
        
        return getHeader() != null;
    }
    
    public List<SourceToken> getHeader() {
        
        return header;
    }
    
    public void setHeader(List<SourceToken> header) {
        
        this.header = header;
    }
    
    @Override
    public String toString(String indent) {
        
        StringBuilder str = new StringBuilder(indent + "Block [" + getSourceEntry().getFileName() + "]");
        
        if (hasHeader()) {
            
            str.append("\n\t").append(indent).append("[HEADER]");
            for (SourceToken token : getHeader()) {
                
                str.append("\n\t\t").append(indent).append(token.toString());
            }
        }
        
        for (BlockItem item : this) {
            
            str.append("\n").append(item.toString(indent + "\t"));
        }
        
        return str.toString();
    }
    
    @Override
    public String toString() {
        
        return toString("");
    }
}

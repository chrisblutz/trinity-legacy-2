package com.github.chrisblutz.trinity.parser.tokens;

import com.github.chrisblutz.trinity.parser.SourceEntry;


/**
 * @author Christopher Lutz
 */
public class SourceToken {
    
    private Token token;
    private int line, index;
    private String contents;
    private SourceEntry entry;
    
    public SourceToken(Token token, int line, int index, String contents, SourceEntry entry) {
        
        this.token = token;
        this.line = line;
        this.index = index;
        this.contents = contents;
        this.entry = entry;
    }
    
    public Token getToken() {
        
        return token;
    }
    
    public void setToken(Token token) {
        
        this.token = token;
    }
    
    public int getLine() {
        
        return line;
    }
    
    public int getIndex() {
        
        return index;
    }
    
    public String getContents() {
        
        return contents;
    }
    
    public SourceEntry getEntry() {
        
        return entry;
    }
    
    @Override
    public String toString() {
        
        return token.toString() + " - " + getContents();
    }
}

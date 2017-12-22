package com.github.chrisblutz.trinity.parser.lines;

import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.ArrayList;


/**
 * @author Christopher Lutz
 */
public class Line extends ArrayList<SourceToken> {
    
    private int lineNumber, leadingSpace = 0;
    
    public Line(int lineNumber) {
        
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        
        return lineNumber;
    }
    
    public int getLeadingSpace() {
        
        return leadingSpace;
    }
    
    public void setLeadingSpace(int leadingSpace) {
        
        this.leadingSpace = leadingSpace;
    }
    
    public boolean containsToken(Token token) {
        
        for (SourceToken sourceToken : this) {
            
            if (sourceToken.getToken() == token) {
                
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString() {
        
        StringBuilder str = new StringBuilder("Line " + getLineNumber());
        
        if (getLeadingSpace() > 0) {
            
            str.append(" (").append(getLeadingSpace()).append(" leading)");
        }
        
        str.append(":");
        
        for (SourceToken token : this) {
            
            str.append("\n\t").append(token.toString());
        }
        
        return str.toString();
    }
}

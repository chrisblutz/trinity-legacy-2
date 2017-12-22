package com.github.chrisblutz.trinity.lang.stack;

/**
 * @author Christopher Lutz
 */
public class StackElement {
    
    private String fileName;
    private int lineNumber;
    
    public StackElement(String fileName, int lineNumber) {
        
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }
    
    public String getFileName() {
        
        return fileName;
    }
    
    public int getLineNumber() {
        
        return lineNumber;
    }
    
    @Override
    public String toString() {
        
        if (getLineNumber() > 0) {
            
            return getFileName() + ":" + getLineNumber();
            
        } else {
            
            return getFileName();
        }
    }
}

package com.github.chrisblutz.trinity.lang.stack;

/**
 * @author Christopher Lutz
 */
public class StackFrame {
    
    private String fileName;
    private int lineNumber;
    private String usable, method;
    
    public StackFrame(String fileName, int lineNumber, String usable, String method) {
        
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.usable = usable;
        this.method = method;
    }
    
    public String getFileName() {
        
        return fileName;
    }
    
    public int getLineNumber() {
        
        return lineNumber;
    }
    
    public String getUsable() {
        
        return usable;
    }
    
    public String getMethod() {
        
        return method;
    }
    
    @Override
    public String toString() {
        
        String str = getFileName();
        
        if (getLineNumber() > 0) {
            
            str += ":" + getLineNumber();
        }
        
        if (getUsable() != null) {
            
            String append = " (" + getUsable();
            
            if (getMethod() != null) {
                
                append += "." + getMethod();
            }
            
            append += ")";
            str += append;
            
        } else if (getMethod() != null) {
            
            str += " (" + getMethod() + ")";
        }
        
        return str;
    }
}

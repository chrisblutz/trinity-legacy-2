package com.github.chrisblutz.trinity.interpreter;

/**
 * @author Christopher Lutz
 */
public class Location {
    
    private static String currentFile = null;
    private static int currentLine = -1;
    
    private String fileName, filePath;
    private int lineNumber;
    
    public Location(String fileName, String filePath, int lineNumber) {
        
        this.fileName = fileName;
        this.filePath = filePath;
        this.lineNumber = lineNumber;
    }
    
    public String getFileName() {
        
        return fileName;
    }
    
    public String getFilePath() {
        
        return filePath;
    }
    
    public int getLineNumber() {
        
        return lineNumber;
    }
    
    @Override
    public String toString() {
        
        return getFileName() + ":" + getLineNumber();
    }
    
    public void makeCurrent() {
        
        currentFile = getFileName();
        currentLine = getLineNumber();
    }
    
    public static String getCurrentFile() {
        
        return currentFile;
    }
    
    public static int getCurrentLine() {
        
        return currentLine;
    }
}

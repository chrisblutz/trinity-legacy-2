package com.github.chrisblutz.trinity.parser.sources;

import com.github.chrisblutz.trinity.parser.SourceEntry;


/**
 * @author Christopher Lutz
 */
public class StringSourceEntry implements SourceEntry {
    
    private String fileName, filePath;
    private int lineNumber;
    private String[] lines;
    
    public StringSourceEntry(String expression, String fileName, String filePath, int lineNumber) {
        
        this.lines = new String[]{expression};
        this.fileName = fileName;
        this.filePath = filePath;
        this.lineNumber = lineNumber;
    }
    
    @Override
    public String getFileName() {
        
        return fileName;
    }
    
    @Override
    public String getFilePath() {
        
        return filePath;
    }
    
    @Override
    public String[] getLines() {
        
        return lines;
    }
    
    @Override
    public int getStartingLine() {
        
        return lineNumber;
    }
}

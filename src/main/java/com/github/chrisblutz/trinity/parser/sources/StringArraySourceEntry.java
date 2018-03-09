package com.github.chrisblutz.trinity.parser.sources;

import com.github.chrisblutz.trinity.parser.SourceEntry;


/**
 * @author Christopher Lutz
 */
public class StringArraySourceEntry implements SourceEntry {
    
    private String[] lines;
    private String name;
    
    public StringArraySourceEntry(String[] lines, String name) {
        
        this.lines = lines;
        this.name = name;
    }
    
    @Override
    public String getFileName() {
        
        return name;
    }
    
    @Override
    public String getFilePath() {
        
        return null;
    }
    
    @Override
    public String[] getLines() {
        
        return lines;
    }
    
    @Override
    public int getStartingLine() {
        
        return 1;
    }
}

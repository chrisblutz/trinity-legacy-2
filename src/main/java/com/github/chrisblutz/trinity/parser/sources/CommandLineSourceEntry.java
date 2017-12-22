package com.github.chrisblutz.trinity.parser.sources;

import com.github.chrisblutz.trinity.parser.SourceEntry;


/**
 * @author Christopher Lutz
 */
public class CommandLineSourceEntry implements SourceEntry {
    
    private String[] lines;
    
    public CommandLineSourceEntry(String[] lines) {
        
        this.lines = lines;
    }
    
    @Override
    public String getFileName() {
        
        return "<stdin>";
    }
    
    @Override
    public String getFilePath() {
        
        return null;
    }
    
    @Override
    public String[] getLines() {
        
        return lines;
    }
}

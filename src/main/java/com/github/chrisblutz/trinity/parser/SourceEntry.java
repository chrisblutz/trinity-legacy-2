package com.github.chrisblutz.trinity.parser;

/**
 * @author Christopher Lutz
 */
public interface SourceEntry {
    
    String getFileName();
    
    String getFilePath();
    
    String[] getLines();
    
    int getStartingLine();
}

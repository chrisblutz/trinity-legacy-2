package com.github.chrisblutz.trinity.parser.lines;

import com.github.chrisblutz.trinity.parser.SourceEntry;

import java.util.ArrayList;


/**
 * @author Christopher Lutz
 */
public class LineSet extends ArrayList<Line> {
    
    private SourceEntry sourceEntry;
    
    public LineSet(SourceEntry sourceEntry) {
        
        this.sourceEntry = sourceEntry;
    }
    
    public SourceEntry getSourceEntry() {
        
        return sourceEntry;
    }
    
    @Override
    public String toString() {
        
        StringBuilder str = new StringBuilder("LineSet - " + sourceEntry.getFileName() + ":");
        
        for (Line line : this) {
            
            str.append("\n").append(line.toString());
        }
        
        return str.toString();
    }
}

package com.github.chrisblutz.trinity.parser.sources;

/**
 * @author Christopher Lutz
 */
public class CommandLineSourceEntry extends StringArraySourceEntry {
    
    public CommandLineSourceEntry(String[] lines) {
        
        super(lines, "<stdin>");
    }
}

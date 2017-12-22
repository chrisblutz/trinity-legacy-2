package com.github.chrisblutz.trinity.parser;

import com.github.chrisblutz.trinity.parser.blocks.BlockItem;

import java.util.List;


/**
 * @author Christopher Lutz
 */
public class BlockParsingResults {
    
    private List<BlockItem> items;
    private int linesSkipped;
    
    public BlockParsingResults(List<BlockItem> items, int linesSkipped){
        
        this.items = items;
        this.linesSkipped = linesSkipped;
    }
    
    public List<BlockItem> getItems() {
        
        return items;
    }
    
    public int getLinesSkipped() {
    
        return linesSkipped;
    }
}

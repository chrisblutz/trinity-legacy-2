package com.github.chrisblutz.trinity.loading;

import com.github.chrisblutz.trinity.interpreter.Interpreter;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.parser.Parser;
import com.github.chrisblutz.trinity.parser.SourceEntry;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.sources.FileSourceEntry;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Christopher Lutz
 */
public class LoadManager {
    
    private static Map<SourceEntry, ProcedureAction> results = new ConcurrentHashMap<>();
    
    public static void load(File file) throws IOException {
        
        load(new FileSourceEntry(file));
    }
    
    public static void load(SourceEntry entry) {
        
        boolean success = LoadManager.loadSourceEntry(entry);
        
        if (success) {
            
            ProcedureAction action = LoadManager.retrieve(entry);
            action.onAction(new TyRuntime(), TyObject.NONE);
        }
    }
    
    public static boolean loadSourceEntry(SourceEntry... entries) {
        
        boolean continueAfterParsing = true;
        for (SourceEntry entry : entries) {
            
            boolean success = doLoad(entry);
            if (!success) {
                
                continueAfterParsing = false;
            }
        }
        
        return continueAfterParsing;
    }
    
    private static boolean doLoad(SourceEntry entry) {
        
        try {
            
            Parser p = new Parser(entry);
            Block block = p.parse();
            
            Interpreter i = new Interpreter(block);
            ProcedureAction action = i.interpret();
            results.put(entry, action);
            
        } catch (Exception e) {
            
            return false;
        }
        
        return true;
    }
    
    public static ProcedureAction retrieve(SourceEntry entry) {
        
        return results.remove(entry);
    }
}

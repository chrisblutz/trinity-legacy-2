package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.loading.LoadManager;
import com.github.chrisblutz.trinity.parser.SourceEntry;
import com.github.chrisblutz.trinity.parser.sources.FileSourceEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class RequirementManager {
    
    private static List<String> loaded = new ArrayList<>();
    
    public static final String WILDCARD = "*";
    
    public static void require(String name, String currentFile) {
        
        if (name.endsWith(WILDCARD)) {
            
            loadAllCurrentLocation(name, currentFile);
            return;
            
        } else if (loadCurrentLocation(name, currentFile)) {
            
            return;
            
        } else if (loadStandardLibraryFile(name)) {
            
            return;
            
        } else if (loadAbsoluteLocation(name)) {
            
            return;
        }
        
        Errors.throwError(Errors.Classes.LOAD_ERROR, "Unable to locate source file named '" + name + "' in default search directories.");
    }
    
    private static void loadAllCurrentLocation(String name, String currentFile) {
        
        if (currentFile != null) {
            
            int lastSeparatorIndex = currentFile.lastIndexOf(File.separatorChar);
            File current = new File(currentFile.substring(0, lastSeparatorIndex >= 0 ? lastSeparatorIndex : currentFile.length()));
            if (name.replace('\\', '/').contains("/")) {
                
                name = name.replace('\\', '/');
                String currentDirectoryAddition = name.substring(0, name.lastIndexOf('/') + 1);
                
                current = new File(current, currentDirectoryAddition);
            }
            
            File[] all = current.listFiles();
            
            if (all != null) {
                
                for (File file : all) {
                    
                    if (file.getName().endsWith(".ty")) {
                        
                        attemptLoad(file);
                    }
                }
            }
        }
    }
    
    private static boolean loadCurrentLocation(String name, String currentFile) {
        
        if (currentFile != null) {
            
            int lastSeparatorIndex = currentFile.lastIndexOf(File.separatorChar);
            
            if (lastSeparatorIndex < 1) {
                
                return false;
            }
            
            File current = new File(currentFile.substring(0, currentFile.lastIndexOf(File.separatorChar)));
            File attempt = new File(current, name + ".ty");
            return attemptLoad(attempt);
            
        } else {
            
            return false;
        }
    }
    
    private static boolean loadStandardLibraryFile(String name) {
        
        File attempt = new File(StandardLibrary.STANDARD_LIBRARY_DIRECTORY, name + ".ty");
        return attemptLoad(attempt);
    }
    
    private static boolean loadAbsoluteLocation(String name) {
        
        File attempt = new File(name + ".ty");
        return attemptLoad(attempt);
    }
    
    private static boolean attemptLoad(File attempt) {
        
        if (attempt.exists()) {
            
            try {
                
                String canonicalPath = attempt.getCanonicalPath();
                if (!loaded.contains(canonicalPath)) {
                    
                    SourceEntry entry = new FileSourceEntry(attempt);
                    ProcedureAction action = load(entry);
                    
                    loaded.add(canonicalPath);
                    
                    if (action != null) {
                        
                        action.onAction(new TyRuntime(), TyObject.NONE);
                    }
                }
                
            } catch (IOException e) {
                
                Errors.throwError(Errors.Classes.LOAD_ERROR, "Unable to load source file " + attempt.getAbsolutePath() + ".");
            }
            
            return true;
            
        } else {
            
            return false;
        }
    }
    
    private static ProcedureAction load(SourceEntry entry) {
        
        boolean success = LoadManager.loadSourceEntry(entry);
        
        if (success) {
            
            return LoadManager.retrieve(entry);
            
        } else {
            
            return null;
        }
    }
}

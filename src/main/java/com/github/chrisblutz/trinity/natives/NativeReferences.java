package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.lang.StandardLibrary;
import com.github.chrisblutz.trinity.utils.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * @author Christopher Lutz
 */
public class NativeReferences {
    
    public static class HintReference {
        
        private String reference, require;
        
        public HintReference(String reference, String require) {
            
            this.reference = reference;
            this.require = require;
        }
        
        public String getReference() {
            
            return reference;
        }
        
        public String getRequire() {
            
            return require;
        }
    }
    
    public static final String HINTS_FILE = "_hints.tyh";
    
    private static boolean loaded = false;
    private static Map<String, List<HintReference>> hintReferenceMap = new HashMap<>();
    
    public static class Classes {
        
        public static final String ARRAY = "Trinity.Array";
        public static final String BOOLEAN = "Trinity.Boolean";
        public static final String CLASS = "Trinity.Class";
        public static final String ERROR = "Trinity.Error";
        public static final String FIELD = "Trinity.Field";
        public static final String FLOAT = "Trinity.Float";
        public static final String INT = "Trinity.Int";
        public static final String KERNEL = "Trinity.Kernel";
        public static final String LONG = "Trinity.Long";
        public static final String MAP = "Trinity.Map";
        public static final String METHOD = "Trinity.Method";
        public static final String MODULE = "Trinity.Module";
        public static final String NATIVE_OUTPUT_STREAM = "Trinity.NativeOutputStream";
        public static final String NUMERIC = "Trinity.Numeric";
        public static final String OBJECT = "Trinity.Object";
        public static final String PROCEDURE = "Trinity.Procedure";
        public static final String STACK_FRAME = "Trinity.StackFrame";
        public static final String STRING = "Trinity.String";
        public static final String SYSTEM = "Trinity.System";
    }
    
    public static void loadHintReferences() {
        
        loaded = true;
        
        File standardLibraryHints = new File(StandardLibrary.STANDARD_LIBRARY_DIRECTORY, HINTS_FILE);
        readHintsFile(standardLibraryHints);
        
        // TODO Support third-party libraries once they are officially supported by Trinity
    }
    
    private static void readHintsFile(File standardLibraryHints) {
        
        try (Scanner sc = new Scanner(standardLibraryHints)) {
            
            while (sc.hasNextLine()) {
                
                String line = sc.nextLine();
                String[] split = line.split("->", 2);
                String[] splitReference = split[0].split(",", 2);
                String[] splitHints = split[1].split(",");
                
                HintReference reference = new HintReference(splitReference[0].trim(), splitReference[1].trim());
                
                for (String hint : splitHints) {
                    
                    String trimmed = hint.trim();
                    if (!hintReferenceMap.containsKey(trimmed)) {
                        
                        hintReferenceMap.put(trimmed, new ArrayList<>());
                    }
                    
                    hintReferenceMap.get(trimmed).add(reference);
                }
            }
            
        } catch (Exception e) {
            
            System.err.println("Could not load hints file at '" + standardLibraryHints.getAbsolutePath() + "'. (" + e.getClass().getSimpleName() + ")");
            
            if (Options.isDebuggingEnabled()) {
                
                e.printStackTrace();
            }
        }
    }
    
    public static List<HintReference> getHintReferencesForName(String name) {
        
        if (!loaded) {
            
            loadHintReferences();
        }
        
        return hintReferenceMap.get(name);
    }
}

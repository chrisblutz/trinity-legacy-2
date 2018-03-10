package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.lang.StandardLibrary;
import com.github.chrisblutz.trinity.loading.LoadManager;
import com.github.chrisblutz.trinity.parser.sources.FileSourceEntry;

import java.io.File;
import java.io.IOException;


/**
 * @author Christopher Lutz
 */
public class NativeReferences {
    
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
    
    public static final String HINTS_FILE = "_hints.tyh";
    
    public static void loadHints() {
        
        File stdHints = new File(StandardLibrary.STANDARD_LIBRARY_DIRECTORY, HINTS_FILE);
        loadHintFile(stdHints);
        
        // TODO Support third-party libraries once they are officially supported by Trinity
    }
    
    private static void loadHintFile(File file) {
        
        try {
            
            FileSourceEntry entry = new FileSourceEntry(file);
            LoadManager.load(entry);
            
        } catch (IOException e) {
            
            System.err.println("Could not load hints file at '" + file.getAbsolutePath() + "'.");
            
            if (Options.isDebuggingEnabled()) {
                
                e.printStackTrace();
            }
        }
    }
}

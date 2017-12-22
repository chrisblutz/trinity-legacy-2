package com.github.chrisblutz.trinity.cli;

/**
 * @author Christopher Lutz
 */
public class Options {
    
    public static final String DEBUG = "debug";
    public static final String PARSE_THREADS = "parseThreads";
    public static final String STRING_PRECISION = "strPrecision";
    public static final String INTERACTIVE_OPTION = "-ish";
    
    private static boolean debug = false, interactive = false;
    private static int parseThreads = 2;
    private static int stringPrecision = 25;
    
    public static boolean isDebuggingEnabled() {
        
        return debug;
    }
    
    static void setDebuggingEnabled(boolean debug) {
        
        Options.debug = debug;
    }
    
    public static boolean isInteractive() {
        
        return interactive;
    }
    
    static void setInteractive(boolean interactive) {
        
        Options.interactive = interactive;
    }
    
    public static int getParseThreads() {
        
        return parseThreads;
    }
    
    static void setParseThreads(int parseThreads) {
        
        if (parseThreads < 1) {
            
            System.err.println("Parse thread number must be at least 1.");
            return;
        }
        
        Options.parseThreads = parseThreads;
    }
    
    public static int getStringPrecision() {
        
        return stringPrecision;
    }
    
    static void setStringPrecision(int stringPrecision) {
        
        if (stringPrecision < 1) {
            
            System.err.println("Parse thread number must at least 1.");
            return;
        }
        
        Options.stringPrecision = stringPrecision;
    }
}

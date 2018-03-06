package com.github.chrisblutz.trinity.cli;

/**
 * @author Christopher Lutz
 */
public class Options {
    
    public static final String DEBUG = "debug";
    public static final String JAVA_STACK_TRACE = "javaStackTrace";
    public static final String STRING_PRECISION = "strPrecision";
    public static final String INTERACTIVE_OPTION = "-ish";
    
    private static boolean debug = false, javaST = false, interactive = false;
    private static int stringPrecision = 25;
    
    public static boolean isDebuggingEnabled() {
        
        return debug;
    }
    
    static void setDebuggingEnabled(boolean debug) {
        
        Options.debug = debug;
    }
    
    public static boolean isJavaStackTraceEnabled() {
        
        return javaST;
    }
    
    static void setJavaStackTraceEnabled(boolean javaStackTrace) {
        
        Options.javaST = javaStackTrace;
    }
    
    public static boolean isInteractive() {
        
        return interactive;
    }
    
    static void setInteractive() {
        
        Options.interactive = true;
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

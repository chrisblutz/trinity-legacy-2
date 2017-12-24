package com.github.chrisblutz.trinity.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class CLI {
    
    private static Map<String, String> systemProperties = new HashMap<>();
    
    public static final String ARGV = "ARGV";
    public static final String SYSTEM_PROPERTY_PREFIX = "-D", INTERPRETER_OPTION_PREFIX = "-X";
    
    private static String fileName;
    private static List<String> arguments = new ArrayList<>();
    
    public static void handleArguments(String[] args) {
        
        boolean encounteredFile = false;
        for (String arg : args) {
            
            if (!encounteredFile && arg.startsWith("-")) {
                
                handleOption(arg);
                
            } else {
                
                encounteredFile = true;
                if (fileName != null) {
                    
                    arguments.add(arg);
                    
                } else {
                    
                    fileName = arg;
                }
            }
        }
    }
    
    private static void handleOption(String option) {
        
        if (option.startsWith(SYSTEM_PROPERTY_PREFIX)) {
            
            String remaining = option.substring(SYSTEM_PROPERTY_PREFIX.length());
            String[] parts = remaining.split("=", 2);
            if (parts.length == 2) {
                
                systemProperties.put(parts[0], parts[1]);
            }
            
        } else if (option.startsWith(INTERPRETER_OPTION_PREFIX)) {
            
            String remaining = option.substring(INTERPRETER_OPTION_PREFIX.length());
            String[] parts = remaining.split("=", 2);
            if (parts.length == 2) {
                
                handleInterpreterOption(parts[0], parts[1]);
            }
            
        } else if (option.contentEquals(Options.INTERACTIVE_OPTION)) {
            
            Options.setInteractive();
        }
    }
    
    private static void handleInterpreterOption(String name, String value) {
        
        switch (name) {
            
            case Options.DEBUG:
                
                try {
                    
                    Options.setDebuggingEnabled(Boolean.parseBoolean(value));
                    
                } catch (Exception e) {
                    
                    reportInvalidInterpreterOptionValue(name, value);
                }
                
                break;
            
            case Options.STRING_PRECISION:
                
                try {
                    
                    Options.setStringPrecision(Integer.parseInt(value));
                    
                } catch (Exception e) {
                    
                    reportInvalidInterpreterOptionValue(name, value);
                }
            
            default:
                
                System.err.println("Unrecognized interpreter option '" + name + "'.");
                break;
        }
    }
    
    private static void reportInvalidInterpreterOptionValue(String name, String value) {
        
        System.err.println("Invalid value '" + value + "' for interpreter option '" + name + "'.");
    }
    
    public static String getFileName() {
        
        return fileName;
    }
    
    public static File getFile() {
        
        if (getFileName() == null) {
            
            return null;
        }
        
        return new File(getFileName());
    }
    
    public static List<String> getArguments() {
        
        return arguments;
    }
    
    public static Map<String, String> getSystemProperties() {
    
        return systemProperties;
    }
}

package com.github.chrisblutz.trinity.info;

import com.github.chrisblutz.trinity.Trinity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author Christopher Lutz
 */
public class TrinityInfo {
    
    private static Properties properties = new Properties();
    
    public static void loadInfo() {
        
        InputStream stream = Trinity.class.getResourceAsStream("/trinity-interpreter.dat");
        
        if (stream == null) {
            
            System.err.println("INFO: Information on the Trinity interpreter is missing.");
            
        } else {
            
            try {
                
                properties.load(stream);
                
            } catch (IOException e) {
                
                System.err.println("INFO: Unable to load information on the Trinity interpreter.");
            }
        }
    }
    
    public static String get(String name) {
        
        return properties.getProperty(name);
    }
    
    public static String getVersionString() {
        
        return get("trinity.name") + " (v" + get("trinity.version") + ")";
    }
}

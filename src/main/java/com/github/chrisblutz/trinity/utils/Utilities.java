package com.github.chrisblutz.trinity.utils;

import com.github.chrisblutz.trinity.Trinity;
import com.github.chrisblutz.trinity.cli.Options;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 * @author Christopher Lutz
 */
public class Utilities {
    
    private static File trinityHome = null;
    
    public static File getTrinityHome() {
        
        if (trinityHome == null) {
            
            try {
                
                trinityHome = new File(new File(Trinity.class.getProtectionDomain().getCodeSource().getLocation().toURI()), "../..");
                // Resolve '../..' out of path
                trinityHome = trinityHome.getCanonicalFile();
                
            } catch (IOException e) {
                
                System.err.println("An error occurred while determining Trinity's home location.");
                
                if (Options.isDebuggingEnabled()) {
                    
                    e.printStackTrace();
                }
                
                Trinity.exit(ExitCodes.TRINITY_HOME_DETERMINATION_ERROR);
                
            } catch (URISyntaxException e) {
                
                System.err.println("Trinity's home location returned a malformed URI.");
                
                if (Options.isDebuggingEnabled()) {
                    
                    e.printStackTrace();
                }
                
                Trinity.exit(ExitCodes.TRINITY_HOME_MALFORMED_URI);
            }
        }
        
        return trinityHome;
    }
    
    public static void checkStandardLibrary() {
        
        if (!new File(getTrinityHome(), "lib/").exists()) {
            
            System.err.println("Trinity's standard library could not be found.");
            
            Trinity.exit(ExitCodes.TRINITY_STANDARD_LIBRARY_MISSING);
        }
    }
}

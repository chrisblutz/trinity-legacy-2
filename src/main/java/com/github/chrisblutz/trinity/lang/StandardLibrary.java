package com.github.chrisblutz.trinity.lang;

import com.github.chrisblutz.trinity.parser.SourceEntry;
import com.github.chrisblutz.trinity.parser.sources.FileSourceEntry;
import com.github.chrisblutz.trinity.utils.Utilities;

import java.io.File;
import java.io.IOException;


/**
 * @author Christopher Lutz
 */
public class StandardLibrary {
    
    public static final File STANDARD_LIBRARY_DIRECTORY = new File(Utilities.getTrinityHome(), "lib/");
    public static final String BOOT_FILE = "_boot.ty";
    
    public static SourceEntry getDefaultEntry() throws IOException {
        
        return new FileSourceEntry(new File(STANDARD_LIBRARY_DIRECTORY, BOOT_FILE));
    }
}

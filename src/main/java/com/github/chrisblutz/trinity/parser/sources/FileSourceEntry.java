package com.github.chrisblutz.trinity.parser.sources;

import com.github.chrisblutz.trinity.parser.SourceEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * @author Christopher Lutz
 */
public class FileSourceEntry implements SourceEntry {
    
    private String fileName, filePath;
    private String[] lines;
    
    public FileSourceEntry(File file) throws IOException {
        
        fileName = file.getName();
        filePath = file.getCanonicalPath();
        
        List<String> lines = new ArrayList<>();
        
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            
            lines.add(sc.nextLine());
        }
        sc.close();
        
        this.lines = lines.toArray(new String[lines.size()]);
    }
    
    @Override
    public String getFileName() {
        
        return fileName;
    }
    
    @Override
    public String getFilePath() {
        
        return filePath;
    }
    
    @Override
    public String[] getLines() {
        
        return lines;
    }
    
    @Override
    public int getStartingLine() {
        
        return 1;
    }
}

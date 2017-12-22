package com.github.chrisblutz.trinity;

import com.github.chrisblutz.trinity.cli.CLI;
import com.github.chrisblutz.trinity.cli.InteractiveSession;
import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.loading.LoadManager;

import java.io.File;
import java.io.IOException;


/**
 * Copyright 2017 Christopher Lutz
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Christopher Lutz
 */
public class Trinity {
    
    public static void main(String[] args) {
        
        Bootstrap.addExceptionHandler();
        
        CLI.handleArguments(args);
        
        Bootstrap.bootstrap();
        
        File file = CLI.getFile();
        if (file != null) {
            
            if (file.exists()) {
                
                try {
                    
                    LoadManager.load(file);
                    
                } catch (IOException e) {
                    
                    System.err.println("An error occurred while loading file '" + CLI.getFileName() + "'.");
                    
                    if (Options.isDebuggingEnabled()) {
                        
                        e.printStackTrace();
                    }
                }
                
            } else {
                
                System.err.println("File '" + CLI.getFileName() + "' not found.");
            }
        }
        
        if (Options.isInteractive()) {
            
            InteractiveSession.launch();
        }
    }
    
    public static void exit(int result) {
        
        System.exit(result);
    }
}

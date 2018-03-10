package com.github.chrisblutz.trinity;

import com.github.chrisblutz.trinity.cli.CLI;
import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.info.TrinityInfo;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.facets.DeclarationFacets;
import com.github.chrisblutz.trinity.interpreter.facets.KeywordExpressionFacets;
import com.github.chrisblutz.trinity.interpreter.facets.KeywordFacets;
import com.github.chrisblutz.trinity.interpreter.facets.OperatorFacets;
import com.github.chrisblutz.trinity.lang.StandardLibrary;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.natives.NativeUtilities;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.lang.variables.VariableManager;
import com.github.chrisblutz.trinity.loading.LoadManager;
import com.github.chrisblutz.trinity.natives.NativeMath;
import com.github.chrisblutz.trinity.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class Bootstrap {
    
    public static void addExceptionHandler() {
        
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            
            try {
                
                Errors.throwUncaughtJavaException(e, Location.getCurrentFile(), Location.getCurrentLine(), t);
                
            } catch (Exception e2) {
                
                // At this point, throw the error and skip straight to the "error occurred" message
                Errors.throwUncaughtJavaException(e, Location.getCurrentFile(), Location.getCurrentLine(), t, true);
            }
            
            Trinity.exit(1);
        });
    }
    
    public static void bootstrap() {
        
        // Load interpreter information
        TrinityInfo.loadInfo();
        
        // Check that standard library exists
        Utilities.checkStandardLibrary();
        
        // Register interpreter facets
        DeclarationFacets.registerFacets();
        KeywordExpressionFacets.registerFacets();
        KeywordFacets.registerFacets();
        OperatorFacets.registerFacets();
        
        // Register native hooks
        NativeUtilities.registerStandardLibraryHooks();
        
        // Register default math hooks
        NativeMath.registerDefaults();
        
        try {
            
            // Load _boot.ty
            LoadManager.load(StandardLibrary.getDefaultEntry());
            
        } catch (IOException e) {
            
            System.err.println("Unable to load boot file (_boot.ty).");
            
            if (Options.isDebuggingEnabled()) {
                
                e.printStackTrace();
            }
        }
        
        // Load CLI arguments into $ARGV
        List<TyObject> argv = new ArrayList<>();
        for (String str : CLI.getArguments()) {
            
            argv.add(new TyString(str));
        }
        VariableManager.setGlobalVariable(CLI.ARGV, new TyArray(argv));
    }
}

package com.github.chrisblutz.trinity.cli;

import com.github.chrisblutz.trinity.info.TrinityInfo;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.loading.LoadManager;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.parser.sources.CommandLineSourceEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * @author Christopher Lutz
 */
public class InteractiveSession {
    
    public static void launch() {
        
        System.out.println(TrinityInfo.getVersionString());
        
        Scanner sc = new Scanner(System.in);
        
        TrinityStack.getCurrentThreadStack().add("<stdin>", -1);
        
        TyRuntime runtime = new TyRuntime();
        List<String> compoundExpression = new ArrayList<>();
        String input;
        while (!(input = read(sc)).contentEquals("exit")) {
            
            if (input.isEmpty()) {
                
                continue;
            }
            
            if (input.endsWith("\\")) {
                
                compoundExpression.add(input.substring(0, input.length() - 1));
                
            } else {
                
                TrinityStack.getCurrentThreadStack().popToSize(1);
                
                String[] lines;
                if (!compoundExpression.isEmpty()) {
                    
                    compoundExpression.add(input);
                    lines = compoundExpression.toArray(new String[compoundExpression.size()]);
                    compoundExpression.clear();
                    
                } else {
                    
                    lines = new String[]{input};
                }
                
                try {
                    
                    CommandLineSourceEntry entry = new CommandLineSourceEntry(lines);
                    boolean success = LoadManager.loadSourceEntry(entry);
                    
                    if (success) {
                        
                        ProcedureAction action = LoadManager.retrieve(entry);
                        TyObject result = action.onAction(runtime, TyObject.NONE);
                        
                        if (result == TyObject.NONE) {
                            
                            result = TyObject.NIL;
                        }
                        
                        String printout = TrinityNatives.toString(result, runtime);
                        
                        flushAll();
                        System.out.println(" => " + printout);
                    }
                    
                } catch (Exception e) {
                    
                    Errors.throwUncaughtJavaException(e, "<stdin>", 1, Thread.currentThread());
                }
            }
        }
        
        sc.close();
    }
    
    private static String read(Scanner stdIn) {
        
        flushAll();
        System.out.print("> ");
        
        try {
            
            return stdIn.nextLine();
            
        } catch (NoSuchElementException e) {
            
            // Block 'No line found' errors
            return "exit";
        }
    }
    
    private static void flushAll() {
        
        System.out.flush();
        System.err.flush();
    }
}

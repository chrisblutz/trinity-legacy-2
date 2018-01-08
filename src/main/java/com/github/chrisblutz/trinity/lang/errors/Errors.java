package com.github.chrisblutz.trinity.lang.errors;

import com.github.chrisblutz.trinity.Trinity;
import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.interpreter.errors.TrinityError;
import com.github.chrisblutz.trinity.interpreter.errors.TrinitySyntaxError;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.stack.StackElement;
import com.github.chrisblutz.trinity.lang.stack.TrinityStack;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.parser.SourceEntry;


/**
 * @author Christopher Lutz
 */
public class Errors {
    
    public final class Classes {
        
        public static final String ARGUMENT_ERROR = "Trinity.Errors.ArgumentError";
        public static final String ARITHMETIC_ERROR = "Trinity.Errors.ArithmeticError";
        public static final String ERROR = TrinityNatives.Classes.ERROR;
        public static final String FORMAT_ERROR = "Trinity.Errors.FormatError";
        public static final String INHERITANCE_ERROR = "Trinity.Errors.InheritanceError";
        public static final String IO_ERROR = "Trinity.Errors.IOError";
        public static final String LOAD_ERROR = "Trinity.Errors.LoadError";
        public static final String NATIVE_ERROR = "Trinity.Errors.NativeError";
        public static final String NOT_FOUND_ERROR = "Trinity.Errors.NotFoundError";
        public static final String SCOPE_ERROR = "Trinity.Errors.ScopeError";
        public static final String STACK_OVERFLOW_ERROR = "Trinity.Errors.StackOverflowError";
        public static final String TYPE_ERROR = "Trinity.Errors.TypeError";
        public static final String UNSUPPORTED_ERROR = "Trinity.Errors.UnsupportedError";
    }
    
    public static void reportSyntaxError(String message, SourceEntry entry) {
        
        System.err.println("SyntaxError: " + message + " (" + entry.getFileName() + ")");
        
        throw new TrinitySyntaxError();
    }
    
    public static void reportSyntaxError(String message, SourceEntry entry, int lineNumber, int index) {
        
        String str = "SyntaxError: " + entry.getFileName() + ":" + lineNumber + ": " + message;
        
        if (index >= 0) {
            
            str += "\n\t" + entry.getLines()[lineNumber - 1];
            str += "\n\t" + padCaret(index);
        }
        
        System.err.println(str);
        
        throw new TrinitySyntaxError();
    }
    
    private static String padCaret(int index) {
        
        StringBuilder str = new StringBuilder("^");
        for (int i = 0; i < index; i++) {
            
            str.insert(0, " ");
        }
        
        return str.toString();
    }
    
    public static void throwError(String errorClass, Object... args) {
        
        throwError(errorClass, new TyRuntime(), args);
    }
    
    public static void throwError(String errorClass, TyRuntime runtime, Object... args) {
        
        TyObject error = constructError(errorClass, runtime, args);
        TrinityNatives.call(TrinityNatives.Classes.KERNEL, "throw", runtime, TyObject.NONE, error);
    }
    
    public static void exit() {
        
        Trinity.exit(1);
    }
    
    public static void throwUnrecoverable(String errorClass, Object... args) {
        
        TyObject error = constructError(errorClass, new TyRuntime(), args);
        throwUncaughtJavaException(new TrinityError(error), null, 0, Thread.currentThread());
    }
    
    private static TyObject constructError(String errorClass, TyRuntime runtime, Object... args) {
        
        TyObject[] tyArgs = new TyObject[args.length];
        for (int i = 0; i < args.length; i++) {
            
            Object o = args[i];
            
            if (o instanceof TyObject) {
                
                tyArgs[i] = (TyObject) o;
                
            } else {
                
                tyArgs[i] = TrinityNatives.getObjectFor(o);
            }
        }
        
        return TrinityNatives.newInstance(errorClass, runtime, tyArgs);
    }
    
    public static void throwUncaughtJavaException(Throwable error, String file, int line, Thread thread) {
        
        throwUncaughtJavaException(error, file, line, thread, false);
    }
    
    public static void throwUncaughtJavaException(Throwable error, String file, int line, Thread thread, boolean skipToDump) {
        
        if (!skipToDump && error instanceof TrinityError) {
            
            TyObject tyError = ((TrinityError) error).getErrorObject();
            String errorMessage = TrinityNatives.cast(TyString.class, tyError.tyInvoke("toString", new TyRuntime(), null, null)).getInternal();
            
            System.err.println(errorMessage);
            
        } else if (!skipToDump && error instanceof StackOverflowError) {
            
            throwUnrecoverable(Classes.STACK_OVERFLOW_ERROR);
            
        } else {
            
            System.err.println("An error occurred in the Trinity interpreter in file '" + file + "' at line " + line + ".");
            
            if (Options.isDebuggingEnabled()) {
                
                System.err.println("\n== Trinity Stack Trace ==\n");
                for (StackElement element : TrinityStack.getThreadStack(thread).getStack()) {
                    
                    System.err.println(element);
                }
                
                System.err.println("\n== Java Stack Trace ==\n");
                error.printStackTrace();
                
            } else {
                
                System.err.println("To view a full trace, enable debugging with the '-Xdebug=true' command-line option.");
            }
        }
    }
}

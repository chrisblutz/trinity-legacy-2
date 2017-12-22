package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.interpreter.utils.FractionUtils;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class NativeNumeric {
    
    private static final String CLASS = TrinityNatives.Classes.NUMERIC;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "+", getAction("+"));
        TrinityNatives.registerMethod(CLASS, "-", getAction("-"));
        TrinityNatives.registerMethod(CLASS, "*", getAction("*"));
        TrinityNatives.registerMethod(CLASS, "/", getAction("/"));
        TrinityNatives.registerMethod(CLASS, "%", getAction("%"));
        TrinityNatives.registerMethod(CLASS, "<<", getAction("<<"));
        TrinityNatives.registerMethod(CLASS, ">>", getAction(">>"));
        TrinityNatives.registerMethod(CLASS, ">>>", getAction(">>>"));
        TrinityNatives.registerMethod(CLASS, "==", (runtime, thisObj, args) -> {
            
            double thisDouble = TrinityNatives.asNumber(thisObj);
            double otherDouble = TrinityNatives.asNumber(runtime.getVariable("a"));
            
            return TyBoolean.valueFor(thisDouble == otherDouble);
        });
        TrinityNatives.registerMethod(CLASS, "<=>", (runtime, thisObj, args) -> {
            
            double thisDouble = TrinityNatives.asNumber(thisObj);
            double otherDouble = TrinityNatives.asNumber(runtime.getVariable("a"));
            
            return new TyInt(Double.compare(thisDouble, otherDouble));
        });
        TrinityNatives.registerMethod(CLASS, "toString", (runtime, thisObj, args) -> {
            
            String string = "";
            if (TrinityNatives.isInstanceOf(thisObj, TrinityNatives.Classes.INT)) {
                
                string = Integer.toString(TrinityNatives.toInt(thisObj));
                
            } else if (TrinityNatives.isInstanceOf(thisObj, TrinityNatives.Classes.LONG)) {
                
                string = Long.toString(TrinityNatives.toLong(thisObj));
                
            } else if (TrinityNatives.isInstanceOf(thisObj, TrinityNatives.Classes.FLOAT)) {
                
                string = Double.toString(TrinityNatives.toFloat(thisObj));
                
            } else {
                
                Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, runtime, "String conversion not defined for " + TrinityNatives.Classes.NUMERIC + " type " + thisObj.getObjectClass().getFullName() + ".");
            }
            
            return new TyString(string);
        });
        TrinityNatives.registerMethod(CLASS, "toHexString", (runtime, thisObj, args) -> {
            
            String string = "";
            if (TrinityNatives.isInstanceOf(thisObj, TrinityNatives.Classes.INT)) {
                
                string = Integer.toHexString(TrinityNatives.toInt(thisObj));
                
            } else if (TrinityNatives.isInstanceOf(thisObj, TrinityNatives.Classes.LONG)) {
                
                string = Long.toHexString(TrinityNatives.toLong(thisObj));
                
            } else if (TrinityNatives.isInstanceOf(thisObj, TrinityNatives.Classes.FLOAT)) {
                
                string = FractionUtils.convertDoubleIntoHexadecimalString(TrinityNatives.toFloat(thisObj), Options.getStringPrecision());
                
            } else {
                
                Errors.throwError(Errors.Classes.UNSUPPORTED_ERROR, runtime, "Hexadecimal string conversion not defined for " + TrinityNatives.Classes.NUMERIC + " type " + thisObj.getObjectClass().getFullName() + ".");
            }
            
            return new TyString(string);
        });
    }
    
    private static Map<String, OperationProcedureAction> actions = new HashMap<>();
    
    private static OperationProcedureAction getAction(String operation) {
        
        if (!actions.containsKey(operation)) {
            
            actions.put(operation, new OperationProcedureAction(operation));
        }
        
        return actions.get(operation);
    }
}

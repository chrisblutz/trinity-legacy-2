package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.interpreter.utils.FractionUtils;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeString {
    
    private static final String CLASS = TrinityNatives.Classes.STRING;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "+", (runtime, thisObj, args) -> {
            
            String thisString = TrinityNatives.cast(TyString.class, thisObj).getInternal();
            
            TyObject object = runtime.getVariable("a");
            String objStr = TrinityNatives.toString(object, runtime);
            
            return new TyString(thisString + objStr);
        });
        TrinityNatives.registerMethod(CLASS, "==", (runtime, thisObj, params) -> {
            
            TyObject object = runtime.getVariable("a");
            
            if (!(object instanceof TyString)) {
                
                return TyBoolean.FALSE;
            }
            
            return TyBoolean.valueFor(TrinityNatives.cast(TyString.class, thisObj).getInternal().contentEquals(TrinityNatives.cast(TyString.class, object).getInternal()));
        });
        TrinityNatives.registerMethod(CLASS, "<=>", (runtime, thisObj, params) -> {
            
            String first = TrinityNatives.toString(thisObj, runtime);
            String second = TrinityNatives.toString(runtime.getVariable("a"), runtime);
            boolean ignoreCase = TrinityNatives.toBoolean(runtime.getVariable("ignoreCase"));
            
            return TrinityNatives.getObjectFor(ignoreCase ? first.compareToIgnoreCase(second) : first.compareTo(second));
        });
        TrinityNatives.registerMethod(CLASS, "chars", (runtime, thisObj, params) -> TrinityNatives.cast(TyString.class, thisObj).getCharacterArray());
        TrinityNatives.registerMethod(CLASS, "length", (runtime, thisObj, args) -> {
            
            String thisString = TrinityNatives.cast(TyString.class, thisObj).getInternal();
            
            return new TyInt(thisString.length());
        });
        TrinityNatives.registerMethod(CLASS, "toUpperCase", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            return new TyString(thisString.toUpperCase());
        });
        TrinityNatives.registerMethod(CLASS, "toLowerCase", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            return new TyString(thisString.toLowerCase());
        });
        TrinityNatives.registerMethod(CLASS, "startsWith", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            String prefix = TrinityNatives.toString(runtime.getVariable("prefix"), runtime);
            return TyBoolean.valueFor(thisString.startsWith(prefix));
        });
        TrinityNatives.registerMethod(CLASS, "endsWith", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            String suffix = TrinityNatives.toString(runtime.getVariable("suffix"), runtime);
            return TyBoolean.valueFor(thisString.endsWith(suffix));
        });
        TrinityNatives.registerMethod(CLASS, "contains", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            String str = TrinityNatives.toString(runtime.getVariable("str"), runtime);
            return TyBoolean.valueFor(thisString.contains(str));
        });
        TrinityNatives.registerMethod(CLASS, "toInt", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            int radix = TrinityNatives.toInt(runtime.getVariable("radix"));
            
            try {
                
                return TrinityNatives.getObjectFor(Integer.parseInt(thisString, radix));
                
            } catch (NumberFormatException e) {
                
                Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Input: '" + thisString + "', Radix: " + radix + ", Expected Type: Trinity.Int");
                return TrinityNatives.getObjectFor(0);
            }
        });
        TrinityNatives.registerMethod(CLASS, "toLong", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            int radix = TrinityNatives.toInt(runtime.getVariable("radix"));
            
            try {
                
                return TrinityNatives.getObjectFor(Long.parseLong(thisString, radix));
                
            } catch (NumberFormatException e) {
                
                Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Input: '" + thisString + "', Radix: " + radix + ", Expected Type: Trinity.Long");
                return TrinityNatives.getObjectFor(0L);
            }
        });
        TrinityNatives.registerMethod(CLASS, "toFloat", (runtime, thisObj, params) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            int radix = TrinityNatives.toInt(runtime.getVariable("radix"));
            
            try {
                
                return TrinityNatives.getObjectFor(FractionUtils.parseDoubleWithRadix(thisString, radix));
                
            } catch (NumberFormatException e) {
                
                Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Input: '" + thisString + "', Radix: " + radix + ", Expected Type: Trinity.Float");
                return TrinityNatives.getObjectFor(0d);
            }
        });
        TrinityNatives.registerMethod(CLASS, "split", (runtime, thisObj, args) -> {
            
            String thisString = TrinityNatives.toString(thisObj, runtime);
            String delimiter = TrinityNatives.toString(runtime.getVariable("delimiter"), runtime);
            int limit = TrinityNatives.toInt(runtime.getVariable("limit"));
            
            return TrinityNatives.getArrayFor(thisString.split(delimiter, limit));
        });
    }
}

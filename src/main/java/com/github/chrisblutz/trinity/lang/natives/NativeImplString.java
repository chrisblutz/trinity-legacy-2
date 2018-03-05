package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.interpreter.utils.FractionUtils;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplString {
    
    private static final String CLASS = NativeReferences.Classes.STRING;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "+", (runtime, thisObj, args) -> {
            
            String thisString = NativeConversion.cast(TyString.class, thisObj).getInternal();
            
            TyObject object = runtime.getVariable("a");
            String objStr = NativeConversion.toString(object, runtime);
            
            return new TyString(thisString + objStr);
        });
        NativeInvocation.registerMethod(CLASS, "==", (runtime, thisObj, params) -> {
            
            TyObject object = runtime.getVariable("a");
            
            if (!(object instanceof TyString)) {
                
                return TyBoolean.FALSE;
            }
            
            return TyBoolean.valueFor(NativeConversion.cast(TyString.class, thisObj).getInternal().contentEquals(NativeConversion.cast(TyString.class, object).getInternal()));
        });
        NativeInvocation.registerMethod(CLASS, "<=>", (runtime, thisObj, params) -> {
            
            String first = NativeConversion.toString(thisObj, runtime);
            String second = NativeConversion.toString(runtime.getVariable("a"), runtime);
            boolean ignoreCase = NativeConversion.toBoolean(runtime.getVariable("ignoreCase"));
            
            return NativeConversion.getObjectFor(ignoreCase ? first.compareToIgnoreCase(second) : first.compareTo(second));
        });
        NativeInvocation.registerMethod(CLASS, "chars", (runtime, thisObj, params) -> NativeConversion.cast(TyString.class, thisObj).getCharacterArray());
        NativeInvocation.registerMethod(CLASS, "length", (runtime, thisObj, args) -> {
            
            String thisString = NativeConversion.cast(TyString.class, thisObj).getInternal();
            
            return new TyInt(thisString.length());
        });
        NativeInvocation.registerMethod(CLASS, "toUpperCase", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            return new TyString(thisString.toUpperCase());
        });
        NativeInvocation.registerMethod(CLASS, "toLowerCase", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            return new TyString(thisString.toLowerCase());
        });
        NativeInvocation.registerMethod(CLASS, "startsWith", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            String prefix = NativeConversion.toString(runtime.getVariable("prefix"), runtime);
            return TyBoolean.valueFor(thisString.startsWith(prefix));
        });
        NativeInvocation.registerMethod(CLASS, "endsWith", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            String suffix = NativeConversion.toString(runtime.getVariable("suffix"), runtime);
            return TyBoolean.valueFor(thisString.endsWith(suffix));
        });
        NativeInvocation.registerMethod(CLASS, "contains", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            String str = NativeConversion.toString(runtime.getVariable("str"), runtime);
            return TyBoolean.valueFor(thisString.contains(str));
        });
        NativeInvocation.registerMethod(CLASS, "toInt", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            int radix = NativeConversion.toInt(runtime.getVariable("radix"));
            
            try {
                
                return NativeConversion.getObjectFor(Integer.parseInt(thisString, radix));
                
            } catch (NumberFormatException e) {
                
                Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Input: '" + thisString + "', Radix: " + radix + ", Expected Type: Trinity.Int");
                return NativeConversion.getObjectFor(0);
            }
        });
        NativeInvocation.registerMethod(CLASS, "toLong", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            int radix = NativeConversion.toInt(runtime.getVariable("radix"));
            
            try {
                
                return NativeConversion.getObjectFor(Long.parseLong(thisString, radix));
                
            } catch (NumberFormatException e) {
                
                Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Input: '" + thisString + "', Radix: " + radix + ", Expected Type: Trinity.Long");
                return NativeConversion.getObjectFor(0L);
            }
        });
        NativeInvocation.registerMethod(CLASS, "toFloat", (runtime, thisObj, params) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            int radix = NativeConversion.toInt(runtime.getVariable("radix"));
            
            try {
                
                return NativeConversion.getObjectFor(FractionUtils.parseDoubleWithRadix(thisString, radix));
                
            } catch (NumberFormatException e) {
                
                Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Input: '" + thisString + "', Radix: " + radix + ", Expected Type: Trinity.Float");
                return NativeConversion.getObjectFor(0d);
            }
        });
        NativeInvocation.registerMethod(CLASS, "split", (runtime, thisObj, args) -> {
            
            String thisString = NativeConversion.toString(thisObj, runtime);
            String delimiter = NativeConversion.toString(runtime.getVariable("delimiter"), runtime);
            int limit = NativeConversion.toInt(runtime.getVariable("limit"));
            
            return NativeConversion.getArrayFor(thisString.split(delimiter, limit));
        });
    }
}

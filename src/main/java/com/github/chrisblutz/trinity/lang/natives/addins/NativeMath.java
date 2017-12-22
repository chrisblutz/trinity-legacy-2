package com.github.chrisblutz.trinity.lang.natives.addins;

import com.github.chrisblutz.trinity.lang.natives.NativeHook;
import com.github.chrisblutz.trinity.natives.NativeStorage;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeMath {
    
    private static final String MODULE = "Trinity.Math";
    
    @NativeHook(MODULE)
    public static void register() {
        
        TrinityNatives.registerField(MODULE, "E", (runtime, thisObj, params) -> NativeStorage.getE());
        TrinityNatives.registerField(MODULE, "PI", (runtime, thisObj, params) -> NativeStorage.getPi());
        
        TrinityNatives.registerMethod(MODULE, "pow", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            double n = TrinityNatives.toFloat(runtime.getVariable("n"));
            
            return TrinityNatives.wrapNumber(Math.pow(x, n));
        });
        TrinityNatives.registerMethod(MODULE, "abs", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            
            return TrinityNatives.wrapNumber(Math.abs(x));
        });
        TrinityNatives.registerMethod(MODULE, "sqrt", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            return TrinityNatives.wrapNumber(Math.sqrt(x));
        });
        TrinityNatives.registerMethod(MODULE, "cbrt", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            
            return TrinityNatives.wrapNumber(Math.cbrt(x));
        });
        TrinityNatives.registerMethod(MODULE, "sin", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.sin(rad));
        });
        TrinityNatives.registerMethod(MODULE, "cos", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.cos(rad));
        });
        TrinityNatives.registerMethod(MODULE, "tan", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.tan(rad));
        });
        TrinityNatives.registerMethod(MODULE, "arcsin", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.asin(rad));
        });
        TrinityNatives.registerMethod(MODULE, "arccos", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.acos(rad));
        });
        TrinityNatives.registerMethod(MODULE, "arctan", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.atan(rad));
        });
        TrinityNatives.registerMethod(MODULE, "toDegrees", (runtime, thisObj, params) -> {
            
            double rad = TrinityNatives.toFloat(runtime.getVariable("rad"));
            
            return TrinityNatives.wrapNumber(Math.toDegrees(rad));
        });
        TrinityNatives.registerMethod(MODULE, "toRadians", (runtime, thisObj, params) -> {
            
            double deg = TrinityNatives.toFloat(runtime.getVariable("deg"));
            
            return TrinityNatives.wrapNumber(Math.toRadians(deg));
        });
        TrinityNatives.registerMethod(MODULE, "log", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            double base = TrinityNatives.toFloat(runtime.getVariable("base"));
            
            double result;
            
            if (base == 10) {
                
                result = Math.log(x);
                
            } else {
                
                result = Math.log(x) / Math.log(base);
            }
            
            return TrinityNatives.wrapNumber(result);
        });
        TrinityNatives.registerMethod(MODULE, "ln", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            
            return TrinityNatives.wrapNumber(Math.log(x));
        });
        TrinityNatives.registerMethod(MODULE, "round", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            
            return TrinityNatives.wrapNumber(Math.round(x));
        });
        TrinityNatives.registerMethod(MODULE, "ceil", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            
            return TrinityNatives.wrapNumber(Math.ceil(x));
        });
        TrinityNatives.registerMethod(MODULE, "floor", (runtime, thisObj, params) -> {
            
            double x = TrinityNatives.toFloat(runtime.getVariable("x"));
            
            return TrinityNatives.wrapNumber(Math.floor(x));
        });
    }
}
package com.github.chrisblutz.trinity.lang.natives.addins;

import com.github.chrisblutz.trinity.lang.natives.NativeHook;
import com.github.chrisblutz.trinity.natives.NativeStorage;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.natives.math.TrinityMath;


/**
 * @author Christopher Lutz
 */
public class NativeMath {
    
    private static final String MODULE = "Trinity.Math";
    
    @NativeHook(MODULE)
    public static void register() {
        
        TrinityNatives.registerField(MODULE, "E", (runtime, thisObj, params) -> NativeStorage.getE());
        TrinityNatives.registerField(MODULE, "PI", (runtime, thisObj, params) -> NativeStorage.getPi());
        
        TrinityNatives.registerMethod(MODULE, "pow", (runtime, thisObj, params) -> TrinityMath.pow(runtime.getVariable("x"), runtime.getVariable("n")));
        TrinityNatives.registerMethod(MODULE, "abs", (runtime, thisObj, params) -> TrinityMath.abs(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "sqrt", (runtime, thisObj, params) -> TrinityMath.sqrt(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "cbrt", (runtime, thisObj, params) -> TrinityMath.cbrt(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "sin", (runtime, thisObj, params) -> TrinityMath.sin(runtime.getVariable("rad")));
        TrinityNatives.registerMethod(MODULE, "cos", (runtime, thisObj, params) -> TrinityMath.cos(runtime.getVariable("rad")));
        TrinityNatives.registerMethod(MODULE, "tan", (runtime, thisObj, params) -> TrinityMath.tan(runtime.getVariable("rad")));
        TrinityNatives.registerMethod(MODULE, "sinh", (runtime, thisObj, params) -> TrinityMath.sinh(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "cosh", (runtime, thisObj, params) -> TrinityMath.cosh(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "tanh", (runtime, thisObj, params) -> TrinityMath.tanh(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "arcsin", (runtime, thisObj, params) -> TrinityMath.arcsin(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "arccos", (runtime, thisObj, params) -> TrinityMath.arccos(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "arctan", (runtime, thisObj, params) -> TrinityMath.arctan(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "arctan2", (runtime, thisObj, params) -> TrinityMath.arctan2(runtime.getVariable("y"), runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "log", (runtime, thisObj, params) -> TrinityMath.log(runtime.getVariable("x"), runtime.getVariable("base")));
        TrinityNatives.registerMethod(MODULE, "ln", (runtime, thisObj, params) -> TrinityMath.ln(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "round", (runtime, thisObj, params) -> TrinityMath.round(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "ceil", (runtime, thisObj, params) -> TrinityMath.ceil(runtime.getVariable("x")));
        TrinityNatives.registerMethod(MODULE, "floor", (runtime, thisObj, params) -> TrinityMath.floor(runtime.getVariable("x")));
    }
}
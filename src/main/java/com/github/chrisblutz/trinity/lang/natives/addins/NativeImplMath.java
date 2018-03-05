package com.github.chrisblutz.trinity.lang.natives.addins;

import com.github.chrisblutz.trinity.lang.natives.NativeHook;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeMath;
import com.github.chrisblutz.trinity.natives.NativeStorage;


/**
 * @author Christopher Lutz
 */
public class NativeImplMath {
    
    private static final String MODULE = "Trinity.Math";
    
    @NativeHook(MODULE)
    public static void register() {
        
        NativeInvocation.registerField(MODULE, "E", (runtime, thisObj, params) -> NativeStorage.getE());
        NativeInvocation.registerField(MODULE, "PI", (runtime, thisObj, params) -> NativeStorage.getPi());
        
        NativeInvocation.registerMethod(MODULE, "pow", (runtime, thisObj, params) -> NativeMath.pow(runtime.getVariable("x"), runtime.getVariable("n")));
        NativeInvocation.registerMethod(MODULE, "abs", (runtime, thisObj, params) -> NativeMath.abs(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "sqrt", (runtime, thisObj, params) -> NativeMath.sqrt(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "cbrt", (runtime, thisObj, params) -> NativeMath.cbrt(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "sin", (runtime, thisObj, params) -> NativeMath.sin(runtime.getVariable("rad")));
        NativeInvocation.registerMethod(MODULE, "cos", (runtime, thisObj, params) -> NativeMath.cos(runtime.getVariable("rad")));
        NativeInvocation.registerMethod(MODULE, "tan", (runtime, thisObj, params) -> NativeMath.tan(runtime.getVariable("rad")));
        NativeInvocation.registerMethod(MODULE, "sinh", (runtime, thisObj, params) -> NativeMath.sinh(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "cosh", (runtime, thisObj, params) -> NativeMath.cosh(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "tanh", (runtime, thisObj, params) -> NativeMath.tanh(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "arcsin", (runtime, thisObj, params) -> NativeMath.arcsin(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "arccos", (runtime, thisObj, params) -> NativeMath.arccos(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "arctan", (runtime, thisObj, params) -> NativeMath.arctan(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "arctan2", (runtime, thisObj, params) -> NativeMath.arctan2(runtime.getVariable("y"), runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "log", (runtime, thisObj, params) -> NativeMath.log(runtime.getVariable("x"), runtime.getVariable("base")));
        NativeInvocation.registerMethod(MODULE, "ln", (runtime, thisObj, params) -> NativeMath.ln(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "round", (runtime, thisObj, params) -> NativeMath.round(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "ceil", (runtime, thisObj, params) -> NativeMath.ceil(runtime.getVariable("x")));
        NativeInvocation.registerMethod(MODULE, "floor", (runtime, thisObj, params) -> NativeMath.floor(runtime.getVariable("x")));
    }
}
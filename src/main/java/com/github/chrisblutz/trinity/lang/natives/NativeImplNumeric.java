package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeMath;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplNumeric {
    
    private static final String CLASS = NativeReferences.Classes.NUMERIC;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "+", (runtime, thisObj, args) -> NativeMath.add(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "-", (runtime, thisObj, args) -> NativeMath.subtract(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "*", (runtime, thisObj, args) -> NativeMath.multiply(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "/", (runtime, thisObj, args) -> NativeMath.divide(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "%", (runtime, thisObj, args) -> NativeMath.modulus(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "<<", (runtime, thisObj, args) -> NativeMath.shiftLeft(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, ">>", (runtime, thisObj, args) -> NativeMath.shiftRight(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, ">>>", (runtime, thisObj, args) -> NativeMath.shiftLogicalRight(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "==", (runtime, thisObj, args) -> NativeMath.equals(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "<=>", (runtime, thisObj, args) -> NativeMath.compare(thisObj, runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "toString", (runtime, thisObj, args) -> NativeMath.toString(thisObj));
        NativeInvocation.registerMethod(CLASS, "toHexString", (runtime, thisObj, args) -> NativeMath.toHexString(thisObj));
    }
}

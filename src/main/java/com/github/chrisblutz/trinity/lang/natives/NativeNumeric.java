package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.natives.math.TrinityMath;


/**
 * @author Christopher Lutz
 */
public class NativeNumeric {
    
    private static final String CLASS = TrinityNatives.Classes.NUMERIC;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "+", (runtime, thisObj, args) -> TrinityMath.add(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "-", (runtime, thisObj, args) -> TrinityMath.subtract(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "*", (runtime, thisObj, args) -> TrinityMath.multiply(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "/", (runtime, thisObj, args) -> TrinityMath.divide(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "%", (runtime, thisObj, args) -> TrinityMath.modulus(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "<<", (runtime, thisObj, args) -> TrinityMath.shiftLeft(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, ">>", (runtime, thisObj, args) -> TrinityMath.shiftRight(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, ">>>", (runtime, thisObj, args) -> TrinityMath.shiftLogicalRight(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "==", (runtime, thisObj, args) -> TrinityMath.equals(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "<=>", (runtime, thisObj, args) -> TrinityMath.compare(thisObj, runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "toString", (runtime, thisObj, args) -> TrinityMath.toString(thisObj));
        TrinityNatives.registerMethod(CLASS, "toHexString", (runtime, thisObj, args) -> TrinityMath.toHexString(thisObj));
    }
}

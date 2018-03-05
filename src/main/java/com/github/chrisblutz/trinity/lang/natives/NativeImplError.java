package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyArray;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;


/**
 * @author Christopher Lutz
 */
public class NativeImplError {

    private static final String CLASS = NativeReferences.Classes.ERROR;

    @NativeHook(CLASS)
    public static void register() {

        NativeInvocation.registerMethod(CLASS, "populateStackTrace", (runtime, thisObj, args) -> {

            TyArray array = NativeConversion.cast(TyArray.class, NativeInvocation.call(NativeReferences.Classes.KERNEL, "caller", runtime, TyObject.NONE));
            for (int i = 0; i < 2 + thisObj.getSuperLevel(); i++) {

                array.getInternal().remove(0);
            }

            return array;
        });
    }
}

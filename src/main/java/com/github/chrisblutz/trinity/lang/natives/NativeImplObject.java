package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyClassObject;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeInvocation;
import com.github.chrisblutz.trinity.natives.NativeReferences;
import com.github.chrisblutz.trinity.natives.NativeStorage;


/**
 * @author Christopher Lutz
 */
public class NativeImplObject {
    
    private static final String CLASS = NativeReferences.Classes.OBJECT;
    
    @NativeHook(CLASS)
    public static void register() {
        
        NativeInvocation.registerMethod(CLASS, "+", NativeUtilities.getDefaultUOEProcedure("+"));
        NativeInvocation.registerMethod(CLASS, "-", NativeUtilities.getDefaultUOEProcedure("-"));
        NativeInvocation.registerMethod(CLASS, "*", NativeUtilities.getDefaultUOEProcedure("*"));
        NativeInvocation.registerMethod(CLASS, "/", NativeUtilities.getDefaultUOEProcedure("/"));
        NativeInvocation.registerMethod(CLASS, "%", NativeUtilities.getDefaultUOEProcedure("%"));
        NativeInvocation.registerMethod(CLASS, "<<", NativeUtilities.getDefaultUOEProcedure("<<"));
        NativeInvocation.registerMethod(CLASS, ">>", NativeUtilities.getDefaultUOEProcedure(">>"));
        NativeInvocation.registerMethod(CLASS, ">>>", NativeUtilities.getDefaultUOEProcedure(">>>"));
        NativeInvocation.registerMethod(CLASS, "==", (runtime, thisObj, args) -> TyBoolean.valueFor(thisObj == runtime.getVariable("a")));
        NativeInvocation.registerMethod(CLASS, "<=>", NativeUtilities.getDefaultUOEProcedure("<=>"));
        NativeInvocation.registerMethod(CLASS, "getClass", (runtime, thisObj, args) -> NativeStorage.getClassObject(thisObj.getObjectClass()));
        NativeInvocation.registerMethod(CLASS, "isInstance", (runtime, thisObj, args) -> {
            
            TyObject typeObject = runtime.getVariable("type");
            TyClass type = NativeConversion.cast(TyClassObject.class, typeObject).getInternal();
            
            if (type != null) {
                
                return TyBoolean.valueFor(thisObj.getObjectClass().isInstanceOf(type));
            }
            
            return TyBoolean.FALSE;
        });
    }
}

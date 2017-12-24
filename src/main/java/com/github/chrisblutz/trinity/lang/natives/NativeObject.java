package com.github.chrisblutz.trinity.lang.natives;

import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyClassObject;
import com.github.chrisblutz.trinity.natives.NativeStorage;
import com.github.chrisblutz.trinity.natives.TrinityNatives;


/**
 * @author Christopher Lutz
 */
public class NativeObject {
    
    private static final String CLASS = TrinityNatives.Classes.OBJECT;
    
    @NativeHook(CLASS)
    public static void register() {
        
        TrinityNatives.registerMethod(CLASS, "+", NativeUtilities.getDefaultUOEProcedure("+"));
        TrinityNatives.registerMethod(CLASS, "-", NativeUtilities.getDefaultUOEProcedure("-"));
        TrinityNatives.registerMethod(CLASS, "*", NativeUtilities.getDefaultUOEProcedure("*"));
        TrinityNatives.registerMethod(CLASS, "/", NativeUtilities.getDefaultUOEProcedure("/"));
        TrinityNatives.registerMethod(CLASS, "%", NativeUtilities.getDefaultUOEProcedure("%"));
        TrinityNatives.registerMethod(CLASS, "<<", NativeUtilities.getDefaultUOEProcedure("<<"));
        TrinityNatives.registerMethod(CLASS, ">>", NativeUtilities.getDefaultUOEProcedure(">>"));
        TrinityNatives.registerMethod(CLASS, ">>>", NativeUtilities.getDefaultUOEProcedure(">>>"));
        TrinityNatives.registerMethod(CLASS, "==", (runtime, thisObj, args) -> TyBoolean.valueFor(thisObj == runtime.getVariable("a")));
        TrinityNatives.registerMethod(CLASS, "<=>", NativeUtilities.getDefaultUOEProcedure("<=>"));
        TrinityNatives.registerMethod(CLASS, "getClass", (runtime, thisObj, args) -> NativeStorage.getClassObject(thisObj.getObjectClass()));
        TrinityNatives.registerMethod(CLASS, "isInstance", (runtime, thisObj, args) -> {
            
            TyObject typeObject = runtime.getVariable("type");
            TyClass type = ((TyClassObject) typeObject).getInternal();
            
            if (type != null) {
                
                return TyBoolean.valueFor(thisObj.getObjectClass().isInstanceOf(type));
            }
            
            return TyBoolean.FALSE;
        });
    }
}

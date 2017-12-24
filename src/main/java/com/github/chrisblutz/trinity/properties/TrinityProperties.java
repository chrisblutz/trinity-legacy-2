package com.github.chrisblutz.trinity.properties;

import com.github.chrisblutz.trinity.cli.CLI;
import com.github.chrisblutz.trinity.info.TrinityInfo;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.TyMap;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.utils.Utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class TrinityProperties {
    
    public static TyObject setProperty(String property, TyObject value) {
        
        return setProperty(TrinityNatives.getObjectFor(property), value);
    }
    
    public static TyObject setProperty(TyObject property, TyObject value) {
        
        return TrinityNatives.call(TrinityNatives.Classes.SYSTEM, "setProperty", new TyRuntime(), TyObject.NONE, property, value);
    }
    
    public static TyObject getProperty(String property) {
        
        return getProperty(TrinityNatives.getObjectFor(property));
    }
    
    public static TyObject getProperty(TyObject property) {
        
        return TrinityNatives.call(TrinityNatives.Classes.SYSTEM, "getProperty", new TyRuntime(), TyObject.NONE, property);
    }
    
    public static TyObject clearProperty(String property) {
        
        return clearProperty(TrinityNatives.getObjectFor(property));
    }
    
    public static TyObject clearProperty(TyObject property) {
        
        return TrinityNatives.call(TrinityNatives.Classes.SYSTEM, "clearProperty", new TyRuntime(), TyObject.NONE, property);
    }
    
    public static TyMap loadProperties() {
        
        Map<String, String> map = new HashMap<>();
        
        map.put("trinity.name", TrinityInfo.get("trinity.name"));
        map.put("trinity.version", TrinityInfo.get("trinity.version"));
        map.put("os.arch", System.getProperty("os.arch"));
        map.put("os.name", System.getProperty("os.name"));
        map.put("os.version", System.getProperty("os.version"));
        map.put("user.dir", System.getProperty("user.dir"));
        map.put("user.home", System.getProperty("user.home"));
        map.put("user.name", System.getProperty("user.name"));
        
        try {
            
            map.put("trinity.home", Utilities.getTrinityHome().getCanonicalPath());
            
        } catch (IOException e) {
            
            Errors.throwError(Errors.Classes.IO_ERROR, "Unable to determine Trinity's home directory.");
        }
        
        map.putAll(CLI.getSystemProperties());
        
        return TrinityNatives.getMapFor(map, TyMap.getFastStorage());
    }
}

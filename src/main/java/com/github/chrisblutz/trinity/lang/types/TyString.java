package com.github.chrisblutz.trinity.lang.types;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.natives.TrinityNatives;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class TyString extends TyObject {
    
    private String internal;
    private TyArray charArray = null;
    
    public TyString(String internal) {
        
        super(ClassRegistry.forName(TrinityNatives.Classes.STRING, true));
        
        this.internal = internal;
    }
    
    public String getInternal() {
        
        return internal;
    }
    
    public TyArray getCharacterArray() {
        
        if (charArray == null) {
            
            List<TyObject> chars = new ArrayList<>();
            
            if (getInternal().length() == 1) {
                
                chars.add(this);
                
            } else {
                
                for (char c : getInternal().toCharArray()) {
                    
                    chars.add(new TyString(Character.toString(c)));
                }
            }
            
            charArray = new TyArray(chars);
        }
        
        return charArray;
    }
}

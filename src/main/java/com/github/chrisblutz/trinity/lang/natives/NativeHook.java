package com.github.chrisblutz.trinity.lang.natives;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author Christopher Lutz
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NativeHook {
    
    String value();
}

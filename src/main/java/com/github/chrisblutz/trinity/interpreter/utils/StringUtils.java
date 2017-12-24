package com.github.chrisblutz.trinity.interpreter.utils;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.parser.Parser;


/**
 * @author Christopher Lutz
 */
public class StringUtils {
    
    public static double parseStringToDouble(String str) {
        
        int radix = 10;
        if (str.matches(Parser.DECIMAL_REGEX)) {
            
            radix = 10;
            
        } else if (str.matches(Parser.HEXADECIMAL_REGEX)) {
            
            radix = 16;
            str = str.substring(2);
            
        } else if (str.matches(Parser.OCTAL_REGEX)) {
            
            radix = 8;
            str = str.substring(2);
            
        } else if (str.matches(Parser.BINARY_REGEX)) {
            
            radix = 2;
            str = str.substring(2);
        }
        
        if (str.endsWith("l") || str.endsWith("L")) {
            
            return Long.parseLong(str.substring(0, str.length() - 1), radix);
            
        } else if (radix < 16 && str.endsWith("f") || str.matches("F")) {
            
            return FractionUtils.parseDoubleWithRadix(str.substring(0, str.length() - 1), radix);
            
        } else if (str.contains(".")) {
            
            return FractionUtils.parseDoubleWithRadix(str, radix);
            
        } else {
            
            try {
                
                return Integer.parseInt(str, radix);
                
            } catch (NumberFormatException e) {
                
                try {
                    
                    return Long.parseLong(str, radix);
                    
                } catch (NumberFormatException e2) {
                    
                    Errors.throwError(Errors.Classes.FORMAT_ERROR, "Input: '" + str + "', Radix: " + radix + ", Expected Type: Trinity.Numeric");
                }
                
                return 0;
            }
        }
    }
}

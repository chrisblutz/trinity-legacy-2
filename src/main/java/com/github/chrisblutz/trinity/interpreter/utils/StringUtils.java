package com.github.chrisblutz.trinity.interpreter.utils;

import com.github.chrisblutz.trinity.interpreter.Interpreter;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.procedures.ProcedureAction;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.parser.Parser;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.sources.StringSourceEntry;


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
        
        if (str.contains(".")) {
            
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
    
    public static TyString parseUnescapedString(String substring, Location location, TyRuntime runtime) {
        
        if (substring.contains("#{")) {
            
            String str = "";
            
            int index;
            while ((index = substring.indexOf("#{")) >= 0) {
                
                str += substring.substring(0, index);
                substring = substring.substring(index);
                
                int i;
                int level = 0;
                for (i = 0; i < substring.length(); i++) {
                    
                    if (substring.charAt(i) == '{') {
                        
                        level++;
                        
                    } else if (substring.charAt(i) == '}') {
                        
                        level--;
                        
                        if (level == 0) {
                            
                            break;
                        }
                    }
                }
                
                if (i < substring.length()) {
                    
                    String expression = substring.substring(2, i);
                    substring = substring.substring(i + 1);
                    
                    StringSourceEntry sourceEntry = new StringSourceEntry(expression, location.getFileName(), location.getFilePath(), location.getLineNumber());
                    Parser parser = new Parser(sourceEntry);
                    Block block = parser.parse();
                    Interpreter interpreter = new Interpreter(block);
                    ProcedureAction action = interpreter.interpret();
                    
                    TyObject object = action.onAction(runtime, TyObject.NONE);
                    
                    str += object == TyObject.NONE ? "" : TrinityNatives.toString(object, runtime);
                    
                } else {
                    
                    Errors.throwError(Errors.Classes.FORMAT_ERROR, runtime, "Embedded expression inside string does not terminate.");
                    break;
                }
            }
            
            str += substring;
            
            return new TyString(str);
            
        } else {
            
            return new TyString(substring);
        }
    }
}

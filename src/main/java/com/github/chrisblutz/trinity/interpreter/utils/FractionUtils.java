package com.github.chrisblutz.trinity.interpreter.utils;

/**
 * @author Christopher Lutz
 */
public class FractionUtils {
    
    public static double parseDoubleWithRadix(String str, int radix) {
        
        if (radix == 10) {
            
            return Double.parseDouble(str);
            
        } else if (!str.contains(".")) {
            
            return parseDoubleWithRadixNoFraction(str, radix);
            
        } else {
            
            double result;
            
            String integer = str.substring(0, str.indexOf('.'));
            result = parseDoubleWithRadixNoFraction(integer, radix);
            
            String fraction = str.substring(str.indexOf('.') + 1);
            char[] fractionCharacters = fraction.toCharArray();
            for (int i = 0; i < fractionCharacters.length; i++) {
                
                int val = Integer.parseInt(Character.toString(fractionCharacters[i]), radix);
                result += (double) val * Math.pow(radix, -(i + 1));
            }
            
            return result;
        }
    }
    
    private static double parseDoubleWithRadixNoFraction(String str, int radix) {
        
        double result = 0;
        
        char[] strCharacters = str.toCharArray();
        for (int i = 0; i < strCharacters.length; i++) {
            
            int val = Integer.parseInt(Character.toString(strCharacters[strCharacters.length - i - 1]), radix);
            result += val * Math.pow(radix, i);
        }
        
        return result;
    }
    
    public static String convertDoubleIntoHexadecimalString(double value, int precision) {
        
        double fractionValue = value % 1;
        int intValue = (int) (value - fractionValue);
        
        String str = Integer.toHexString(intValue);
        
        if (fractionValue != 0) {
            
            str += ".";
            StringBuilder dec = new StringBuilder();
            
            while ((fractionValue *= 16) != 0 && dec.length() < precision) {
                
                double temp = fractionValue % 1;
                intValue = (int) (fractionValue - temp);
                fractionValue = temp;
                
                dec.append(Integer.toHexString(intValue));
            }
            
            str += dec;
        }
        
        return str;
    }
}

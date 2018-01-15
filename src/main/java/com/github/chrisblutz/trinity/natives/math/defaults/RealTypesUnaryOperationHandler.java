package com.github.chrisblutz.trinity.natives.math.defaults;

import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.interpreter.utils.FractionUtils;
import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.natives.math.UnaryOperationHandler;


/**
 * @author Christopher Lutz
 */
public class RealTypesUnaryOperationHandler extends UnaryOperationHandler {
    
    private static RealTypesUnaryOperationHandler instance = new RealTypesUnaryOperationHandler();
    
    private RealTypesUnaryOperationHandler() {
    
    }
    
    @Override
    public TyObject toString(TyObject operand) {
        
        String string = "";
        if (TrinityNatives.isInstanceOf(operand, TrinityNatives.Classes.INT)) {
            
            string = Integer.toString(TrinityNatives.toInt(operand));
            
        } else if (TrinityNatives.isInstanceOf(operand, TrinityNatives.Classes.LONG)) {
            
            string = Long.toString(TrinityNatives.toLong(operand));
            
        } else if (TrinityNatives.isInstanceOf(operand, TrinityNatives.Classes.FLOAT)) {
            
            string = Double.toString(TrinityNatives.toFloat(operand));
        }
        
        return new TyString(string);
    }
    
    @Override
    public TyObject toHexString(TyObject operand) {
        
        String string = "";
        if (TrinityNatives.isInstanceOf(operand, TrinityNatives.Classes.INT)) {
            
            string = Integer.toHexString(TrinityNatives.toInt(operand));
            
        } else if (TrinityNatives.isInstanceOf(operand, TrinityNatives.Classes.LONG)) {
            
            string = Long.toHexString(TrinityNatives.toLong(operand));
            
        } else if (TrinityNatives.isInstanceOf(operand, TrinityNatives.Classes.FLOAT)) {
            
            string = FractionUtils.convertDoubleIntoHexadecimalString(TrinityNatives.toFloat(operand), Options.getStringPrecision());
        }
        
        return new TyString(string);
    }
    
    @Override
    public TyObject bitwiseComplement(TyObject operand) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("~", floatClass);
        }
        
        return TrinityNatives.wrapNumber(~TrinityNatives.toLong(operand));
    }
    
    public static RealTypesUnaryOperationHandler getInstance() {
        
        return instance;
    }
}

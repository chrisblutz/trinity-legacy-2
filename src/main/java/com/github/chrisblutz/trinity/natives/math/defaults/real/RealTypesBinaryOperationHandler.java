package com.github.chrisblutz.trinity.natives.math.defaults.real;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.natives.TrinityNatives;
import com.github.chrisblutz.trinity.natives.math.BinaryOperationHandler;
import com.github.chrisblutz.trinity.natives.math.TrinityMath;


/**
 * @author Christopher Lutz
 */
public class RealTypesBinaryOperationHandler extends BinaryOperationHandler {
    
    private static RealTypesBinaryOperationHandler instance = new RealTypesBinaryOperationHandler();
    
    private RealTypesBinaryOperationHandler() {
    
    }
    
    @Override
    public TyObject add(TyObject operand1, TyObject operand2) {
        
        return TrinityNatives.wrapNumber(toDouble(operand1) + toDouble(operand2));
    }
    
    @Override
    public TyObject subtract(TyObject operand1, TyObject operand2) {
        
        return TrinityNatives.wrapNumber(toDouble(operand1) - toDouble(operand2));
    }
    
    @Override
    public TyObject multiply(TyObject operand1, TyObject operand2) {
        
        return TrinityNatives.wrapNumber(toDouble(operand1) * toDouble(operand2));
    }
    
    @Override
    public TyObject divide(TyObject operand1, TyObject operand2) {
        
        if (TrinityMath.equals(operand2, TrinityMath.ZERO).getInternal()) {
            
            throwDivideByZeroError();
        }
        
        return TrinityNatives.wrapNumber(toDouble(operand1) / toDouble(operand2));
    }
    
    @Override
    public TyObject modulus(TyObject operand1, TyObject operand2) {
        
        if (TrinityMath.equals(operand2, TrinityMath.ZERO).getInternal()) {
            
            throwDivideByZeroError();
        }
        
        double a = toDouble(operand1);
        double b = toDouble(operand2);
        
        return TrinityNatives.wrapNumber(a - (Math.floor(a / b) * b));
    }
    
    @Override
    public TyObject shiftLeft(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("<<", floatClass);
        }
        
        return TrinityNatives.wrapNumber(toLong(operand1) << toLong(operand2));
    }
    
    @Override
    public TyObject shiftRight(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError(">>", floatClass);
        }
        
        return TrinityNatives.wrapNumber(toLong(operand1) >> toLong(operand2));
    }
    
    @Override
    public TyObject shiftLogicalRight(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError(">>>", floatClass);
        }
        
        return TrinityNatives.wrapNumber(toLong(operand1) >>> toLong(operand2));
    }
    
    @Override
    public TyObject bitwiseOr(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("|", floatClass);
        }
        
        return TrinityNatives.wrapNumber(toLong(operand1) | toLong(operand2));
    }
    
    @Override
    public TyObject bitwiseAnd(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("&", floatClass);
        }
        
        return TrinityNatives.wrapNumber(toLong(operand1) & toLong(operand2));
    }
    
    @Override
    public TyObject bitwiseXor(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(TrinityNatives.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("^", floatClass);
        }
        
        return TrinityNatives.wrapNumber(toLong(operand1) ^ toLong(operand2));
    }
    
    @Override
    public TyBoolean equals(TyObject operand1, TyObject operand2) {
        
        return TyBoolean.valueFor(toDouble(operand1) == toDouble(operand2));
    }
    
    @Override
    public TyInt compare(TyObject operand1, TyObject operand2) {
        
        return new TyInt(Double.compare(toDouble(operand1), toDouble(operand2)));
    }
    
    private double toDouble(TyObject object) {
        
        return TrinityNatives.asNumber(object);
    }
    
    private long toLong(TyObject object) {
        
        return TrinityNatives.toLong(object);
    }
    
    public static RealTypesBinaryOperationHandler getInstance() {
        
        return instance;
    }
}

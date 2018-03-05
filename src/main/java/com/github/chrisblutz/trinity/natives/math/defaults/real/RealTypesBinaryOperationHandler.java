package com.github.chrisblutz.trinity.natives.math.defaults.real;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeMath;
import com.github.chrisblutz.trinity.natives.NativeReferences;
import com.github.chrisblutz.trinity.natives.math.BinaryOperationHandler;


/**
 * @author Christopher Lutz
 */
public class RealTypesBinaryOperationHandler extends BinaryOperationHandler {
    
    private static RealTypesBinaryOperationHandler instance = new RealTypesBinaryOperationHandler();
    
    private RealTypesBinaryOperationHandler() {
    
    }
    
    @Override
    public TyObject add(TyObject operand1, TyObject operand2) {
        
        return NativeConversion.wrapNumber(toDouble(operand1) + toDouble(operand2));
    }
    
    @Override
    public TyObject subtract(TyObject operand1, TyObject operand2) {
        
        return NativeConversion.wrapNumber(toDouble(operand1) - toDouble(operand2));
    }
    
    @Override
    public TyObject multiply(TyObject operand1, TyObject operand2) {
        
        return NativeConversion.wrapNumber(toDouble(operand1) * toDouble(operand2));
    }
    
    @Override
    public TyObject divide(TyObject operand1, TyObject operand2) {
        
        if (NativeMath.equals(operand2, NativeMath.ZERO).getInternal()) {
            
            throwDivideByZeroError();
        }
        
        return NativeConversion.wrapNumber(toDouble(operand1) / toDouble(operand2));
    }
    
    @Override
    public TyObject modulus(TyObject operand1, TyObject operand2) {
        
        if (NativeMath.equals(operand2, NativeMath.ZERO).getInternal()) {
            
            throwDivideByZeroError();
        }
        
        double a = toDouble(operand1);
        double b = toDouble(operand2);
        
        return NativeConversion.wrapNumber(a - (Math.floor(a / b) * b));
    }
    
    @Override
    public TyObject shiftLeft(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("<<", floatClass);
        }
        
        return NativeConversion.wrapNumber(toLong(operand1) << toLong(operand2));
    }
    
    @Override
    public TyObject shiftRight(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError(">>", floatClass);
        }
        
        return NativeConversion.wrapNumber(toLong(operand1) >> toLong(operand2));
    }
    
    @Override
    public TyObject shiftLogicalRight(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError(">>>", floatClass);
        }
        
        return NativeConversion.wrapNumber(toLong(operand1) >>> toLong(operand2));
    }
    
    @Override
    public TyObject bitwiseOr(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("|", floatClass);
        }
        
        return NativeConversion.wrapNumber(toLong(operand1) | toLong(operand2));
    }
    
    @Override
    public TyObject bitwiseAnd(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("&", floatClass);
        }
        
        return NativeConversion.wrapNumber(toLong(operand1) & toLong(operand2));
    }
    
    @Override
    public TyObject bitwiseXor(TyObject operand1, TyObject operand2) {
        
        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand1.getObjectClass().isInstanceOf(floatClass) || operand2.getObjectClass().isInstanceOf(floatClass)) {
            
            throwOperationUnsupportedError("^", floatClass);
        }
        
        return NativeConversion.wrapNumber(toLong(operand1) ^ toLong(operand2));
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
        
        return NativeConversion.asNumber(object);
    }
    
    private long toLong(TyObject object) {
        
        return NativeConversion.toLong(object);
    }
    
    public static RealTypesBinaryOperationHandler getInstance() {
        
        return instance;
    }
}

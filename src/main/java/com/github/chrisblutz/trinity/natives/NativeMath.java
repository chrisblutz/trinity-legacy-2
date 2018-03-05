package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyBoolean;
import com.github.chrisblutz.trinity.lang.types.TyInt;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.math.*;
import com.github.chrisblutz.trinity.natives.math.defaults.real.RealTypesBinaryMathMethodHandler;
import com.github.chrisblutz.trinity.natives.math.defaults.real.RealTypesBinaryOperationHandler;
import com.github.chrisblutz.trinity.natives.math.defaults.real.RealTypesUnaryMathMethodHandler;
import com.github.chrisblutz.trinity.natives.math.defaults.real.RealTypesUnaryOperationHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Christopher Lutz
 */
public class NativeMath {

    public static final MathType REAL_TYPES = new MathType(ClassRegistry.forName(NativeReferences.Classes.INT, true), ClassRegistry.forName(NativeReferences.Classes.LONG, true), ClassRegistry.forName(NativeReferences.Classes.FLOAT, true));

    public static final TyInt ZERO = new TyInt(0);

    private static Map<MathType, Map<MathType, BinaryOperationHandler>> binaryOperationHandlers = new HashMap<>();
    private static Map<MathType, UnaryOperationHandler> unaryOperationHandlers = new HashMap<>();
    private static Map<MathType, Map<MathType, BinaryMathMethodHandler>> binaryMathMethodHandlers = new HashMap<>();
    private static Map<MathType, UnaryMathMethodHandler> unaryMathMethodHandlers = new HashMap<>();

    public static void registerBinaryOperationHandler(MathType operand1Type, MathType operand2Type, BinaryOperationHandler handler) {

        if (!binaryOperationHandlers.containsKey(operand1Type)) {

            binaryOperationHandlers.put(operand1Type, new HashMap<>());
        }

        binaryOperationHandlers.get(operand1Type).put(operand2Type, handler);
    }

    public static void registerUnaryOperationHandler(MathType operandType, UnaryOperationHandler handler) {

        unaryOperationHandlers.put(operandType, handler);
    }

    public static void registerBinaryMathMethodHandler(MathType operand1Type, MathType operand2Type, BinaryMathMethodHandler handler) {

        if (!binaryMathMethodHandlers.containsKey(operand1Type)) {

            binaryMathMethodHandlers.put(operand1Type, new HashMap<>());
        }

        binaryMathMethodHandlers.get(operand1Type).put(operand2Type, handler);
    }

    public static void registerUnaryMathMethodHandler(MathType operandType, UnaryMathMethodHandler handler) {

        unaryMathMethodHandlers.put(operandType, handler);
    }

    public static void registerDefaults() {

        // Real types
        registerBinaryOperationHandler(REAL_TYPES, REAL_TYPES, RealTypesBinaryOperationHandler.getInstance());
        registerUnaryOperationHandler(REAL_TYPES, RealTypesUnaryOperationHandler.getInstance());
        registerBinaryMathMethodHandler(REAL_TYPES, REAL_TYPES, RealTypesBinaryMathMethodHandler.getInstance());
        registerUnaryMathMethodHandler(REAL_TYPES, RealTypesUnaryMathMethodHandler.getInstance());
    }

    private static BinaryOperationHandler getBinaryOperationHandler(TyObject operand1, TyObject operand2) {

        MathType operand1Type = MathType.forType(operand1.getObjectClass());
        MathType operand2Type = MathType.forType(operand2.getObjectClass());

        if (binaryOperationHandlers.containsKey(operand1Type) && binaryOperationHandlers.get(operand1Type).containsKey(operand2Type)) {

            return binaryOperationHandlers.get(operand1Type).get(operand2Type);

        } else {

            return DefaultBinaryOperationHandler.getInstance();
        }
    }

    private static UnaryOperationHandler getUnaryOperationHandler(TyObject operand) {

        MathType operandType = MathType.forType(operand.getObjectClass());

        if (unaryOperationHandlers.containsKey(operandType)) {

            return unaryOperationHandlers.get(operandType);

        } else {

            return DefaultUnaryOperationHandler.getInstance();
        }
    }

    private static BinaryMathMethodHandler getBinaryMathMethodHandler(TyObject operand1, TyObject operand2) {

        MathType operand1Type = MathType.forType(operand1.getObjectClass());
        MathType operand2Type = MathType.forType(operand2.getObjectClass());

        if (binaryMathMethodHandlers.containsKey(operand1Type) && binaryMathMethodHandlers.get(operand1Type).containsKey(operand2Type)) {

            return binaryMathMethodHandlers.get(operand1Type).get(operand2Type);

        } else {

            return DefaultBinaryMathMethodHandler.getInstance();
        }
    }

    private static UnaryMathMethodHandler getUnaryMathMethodHandler(TyObject operand) {

        MathType operandType = MathType.forType(operand.getObjectClass());

        if (unaryMathMethodHandlers.containsKey(operandType)) {

            return unaryMathMethodHandlers.get(operandType);

        } else {

            return DefaultUnaryMathMethodHandler.getInstance();
        }
    }

    public static TyObject add(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).add(operand1, operand2);
    }

    public static TyObject subtract(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).subtract(operand1, operand2);
    }

    public static TyObject multiply(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).multiply(operand1, operand2);
    }

    public static TyObject divide(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).divide(operand1, operand2);
    }

    public static TyObject modulus(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).modulus(operand1, operand2);
    }

    public static TyObject shiftLeft(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).shiftLeft(operand1, operand2);
    }

    public static TyObject shiftRight(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).shiftRight(operand1, operand2);
    }

    public static TyObject shiftLogicalRight(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).shiftLogicalRight(operand1, operand2);
    }

    public static TyObject bitwiseOr(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).bitwiseOr(operand1, operand2);
    }

    public static TyObject bitwiseAnd(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).bitwiseAnd(operand1, operand2);
    }

    public static TyObject bitwiseXor(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).bitwiseXor(operand1, operand2);
    }

    public static TyBoolean equals(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).equals(operand1, operand2);
    }

    public static TyInt compare(TyObject operand1, TyObject operand2) {

        return getBinaryOperationHandler(operand1, operand2).compare(operand1, operand2);
    }

    public static TyString toString(TyObject operand) {

        return getUnaryOperationHandler(operand).toString(operand);
    }

    public static TyString toHexString(TyObject operand) {

        return getUnaryOperationHandler(operand).toHexString(operand);
    }

    public static TyObject bitwiseComplement(TyObject operand) {

        return getUnaryOperationHandler(operand).bitwiseComplement(operand);
    }

    public static TyObject pow(TyObject operand, TyObject exponent) {

        return getBinaryMathMethodHandler(operand, exponent).pow(operand, exponent);
    }

    public static TyObject log(TyObject operand, TyObject base) {

        return getBinaryMathMethodHandler(operand, base).log(operand, base);
    }

    public static TyObject arctan2(TyObject y, TyObject x) {

        return getBinaryMathMethodHandler(y, x).arctan2(y, x);
    }

    public static TyObject abs(TyObject operand) {

        return getUnaryMathMethodHandler(operand).abs(operand);
    }

    public static TyObject sqrt(TyObject operand) {

        return getUnaryMathMethodHandler(operand).sqrt(operand);
    }

    public static TyObject cbrt(TyObject operand) {

        return getUnaryMathMethodHandler(operand).cbrt(operand);
    }

    public static TyObject sin(TyObject operand) {

        return getUnaryMathMethodHandler(operand).sin(operand);
    }

    public static TyObject cos(TyObject operand) {

        return getUnaryMathMethodHandler(operand).cos(operand);
    }

    public static TyObject tan(TyObject operand) {

        return getUnaryMathMethodHandler(operand).tan(operand);
    }

    public static TyObject sinh(TyObject operand) {

        return getUnaryMathMethodHandler(operand).sinh(operand);
    }

    public static TyObject cosh(TyObject operand) {

        return getUnaryMathMethodHandler(operand).cosh(operand);
    }

    public static TyObject tanh(TyObject operand) {

        return getUnaryMathMethodHandler(operand).tanh(operand);
    }

    public static TyObject arcsin(TyObject operand) {

        return getUnaryMathMethodHandler(operand).arcsin(operand);
    }

    public static TyObject arccos(TyObject operand) {

        return getUnaryMathMethodHandler(operand).arccos(operand);
    }

    public static TyObject arctan(TyObject operand) {

        return getUnaryMathMethodHandler(operand).arctan(operand);
    }

    public static TyObject ln(TyObject operand) {

        return getUnaryMathMethodHandler(operand).ln(operand);
    }

    public static TyObject round(TyObject operand) {

        return getUnaryMathMethodHandler(operand).round(operand);
    }

    public static TyObject ceil(TyObject operand) {

        return getUnaryMathMethodHandler(operand).ceil(operand);
    }

    public static TyObject floor(TyObject operand) {

        return getUnaryMathMethodHandler(operand).floor(operand);
    }
}

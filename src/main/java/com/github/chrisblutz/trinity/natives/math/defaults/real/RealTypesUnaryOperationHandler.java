package com.github.chrisblutz.trinity.natives.math.defaults.real;

import com.github.chrisblutz.trinity.cli.Options;
import com.github.chrisblutz.trinity.interpreter.utils.FractionUtils;
import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyClass;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.types.TyString;
import com.github.chrisblutz.trinity.natives.NativeConversion;
import com.github.chrisblutz.trinity.natives.NativeReferences;
import com.github.chrisblutz.trinity.natives.math.UnaryOperationHandler;


/**
 * @author Christopher Lutz
 */
public class RealTypesUnaryOperationHandler extends UnaryOperationHandler {

    private static RealTypesUnaryOperationHandler instance = new RealTypesUnaryOperationHandler();

    private RealTypesUnaryOperationHandler() {

    }

    @Override
    public TyString toString(TyObject operand) {

        String string = "";
        if (NativeConversion.isInstanceOf(operand, NativeReferences.Classes.INT)) {

            string = Integer.toString(NativeConversion.toInt(operand));

        } else if (NativeConversion.isInstanceOf(operand, NativeReferences.Classes.LONG)) {

            string = Long.toString(NativeConversion.toLong(operand));

        } else if (NativeConversion.isInstanceOf(operand, NativeReferences.Classes.FLOAT)) {

            string = Double.toString(NativeConversion.toFloat(operand));
        }

        return new TyString(string);
    }

    @Override
    public TyString toHexString(TyObject operand) {

        String string = "";
        if (NativeConversion.isInstanceOf(operand, NativeReferences.Classes.INT)) {

            string = Integer.toHexString(NativeConversion.toInt(operand));

        } else if (NativeConversion.isInstanceOf(operand, NativeReferences.Classes.LONG)) {

            string = Long.toHexString(NativeConversion.toLong(operand));

        } else if (NativeConversion.isInstanceOf(operand, NativeReferences.Classes.FLOAT)) {

            string = FractionUtils.convertDoubleIntoHexadecimalString(NativeConversion.toFloat(operand), Options.getStringPrecision());
        }

        return new TyString(string);
    }

    @Override
    public TyObject bitwiseComplement(TyObject operand) {

        TyClass floatClass = ClassRegistry.getClass(NativeReferences.Classes.FLOAT);
        if (operand.getObjectClass().isInstanceOf(floatClass)) {

            throwOperationUnsupportedError("~", floatClass);
        }

        return NativeConversion.wrapNumber(~NativeConversion.toLong(operand));
    }

    public static RealTypesUnaryOperationHandler getInstance() {

        return instance;
    }
}

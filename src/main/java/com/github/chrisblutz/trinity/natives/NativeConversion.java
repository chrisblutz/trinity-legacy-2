package com.github.chrisblutz.trinity.natives;

import com.github.chrisblutz.trinity.interpreter.utils.StringUtils;
import com.github.chrisblutz.trinity.lang.ClassRegistry;
import com.github.chrisblutz.trinity.lang.TyObject;
import com.github.chrisblutz.trinity.lang.TyRuntime;
import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.lang.types.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author Christopher
 */
public class NativeConversion {

    public static TyObject getObjectFor(Object obj) {

        if (obj == null) {

            return TyObject.NIL;

        } else if (obj instanceof Integer) {

            return new TyInt((Integer) obj);

        } else if (obj instanceof Float) {

            return new TyFloat((Float) obj);

        } else if (obj instanceof Double) {

            return new TyFloat((Double) obj);

        } else if (obj instanceof Long) {

            return new TyLong((Long) obj);

        } else if (obj instanceof String) {

            return new TyString((String) obj);

        } else if (obj instanceof Boolean) {

            if ((Boolean) obj) {

                return TyBoolean.TRUE;

            } else {

                return TyBoolean.FALSE;
            }

        } else if (obj.getClass().isArray()) {

            return getArrayFor((Object[]) obj);
        }

        Errors.throwError(Errors.Classes.NATIVE_ERROR, "Trinity does not have native type-conversion utilities for " + obj.getClass() + ".");

        return TyObject.NIL;
    }

    public static TyArray getArrayFor(Object[] arr) {

        List<TyObject> objects = new ArrayList<>();

        for (Object o : arr) {

            objects.add(getObjectFor(o));
        }

        return new TyArray(objects);
    }

    public static TyArray getArrayFor(Collection<?> collection) {

        List<TyObject> objects = new ArrayList<>();

        for (Object o : collection) {

            objects.add(getObjectFor(o));
        }

        return new TyArray(objects);
    }

    public static TyMap getMapFor(Map<?, ?> map, int storageType) {

        Map<TyObject, TyObject> objects = TyMap.getMapForStorageType(storageType);

        for (Object o : map.keySet()) {

            objects.put(getObjectFor(o), getObjectFor(map.get(o)));
        }

        return new TyMap(objects, storageType);
    }

    public static TyObject wrapNumber(double d) {

        if (d % 1 == 0) {

            if (d > Integer.MAX_VALUE || d < Integer.MIN_VALUE) {

                return new TyLong((long) d);

            } else {

                return new TyInt((int) d);
            }

        } else {

            return new TyFloat(d);
        }
    }

    public static <T extends TyObject> T cast(Class<T> desiredClass, TyObject object) {

        if (desiredClass.isInstance(object)) {

            return desiredClass.cast(object);

        } else {

            Errors.throwError(Errors.Classes.TYPE_ERROR, "Unexpected value of type " + object.getObjectClass().getFullName() + " found.");

            // This will throw an error, but the line above throws an error for
            // the interpreter to catch, so the program will never reach this point
            return desiredClass.cast(object);
        }
    }

    public static double asNumber(TyObject tyObject) {

        if (tyObject instanceof TyInt) {

            return ((TyInt) tyObject).getInternal();

        } else if (tyObject instanceof TyLong) {

            return ((TyLong) tyObject).getInternal();

        } else if (tyObject instanceof TyFloat) {

            return ((TyFloat) tyObject).getInternal();

        } else if (tyObject instanceof TyString) {

            return StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());

        } else {

            Errors.throwError(Errors.Classes.TYPE_ERROR, "Expected value of type " + NativeReferences.Classes.NUMERIC + ", found " + tyObject.getObjectClass().getFullName() + ".");
        }

        return 0;
    }

    public static int toInt(TyObject tyObject) {

        if (tyObject instanceof TyLong) {

            return Math.toIntExact(((TyLong) tyObject).getInternal());

        } else if (tyObject instanceof TyFloat) {

            return (int) ((TyFloat) tyObject).getInternal();

        } else if (tyObject instanceof TyString) {

            return (int) StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());
        }

        return cast(TyInt.class, tyObject).getInternal();
    }

    public static long toLong(TyObject tyObject) {

        if (tyObject instanceof TyInt) {

            return ((TyInt) tyObject).getInternal();

        } else if (tyObject instanceof TyFloat) {

            return (long) ((TyFloat) tyObject).getInternal();

        } else if (tyObject instanceof TyString) {

            return (long) StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());

        }

        return cast(TyLong.class, tyObject).getInternal();
    }

    public static double toFloat(TyObject tyObject) {

        if (tyObject instanceof TyInt) {

            return ((TyInt) tyObject).getInternal();

        } else if (tyObject instanceof TyLong) {

            return ((TyLong) tyObject).getInternal();

        } else if (tyObject instanceof TyString) {

            return StringUtils.parseStringToDouble(((TyString) tyObject).getInternal());

        }

        return NativeConversion.cast(TyFloat.class, tyObject).getInternal();
    }

    public static String toString(TyObject tyObject, TyRuntime runtime) {

        if (tyObject instanceof TyString) {

            return ((TyString) tyObject).getInternal();

        } else {

            TyString tyString = cast(TyString.class, tyObject.tyInvoke("toString", runtime, null, null));
            return tyString.getInternal();
        }
    }

    public static boolean toBoolean(TyObject object) {

        if (object == TyObject.NIL || object == TyObject.NONE) {

            return true;

        } else if (object instanceof TyBoolean) {

            return ((TyBoolean) object).getInternal();

        } else if (object instanceof TyInt) {

            return ((TyInt) object).getInternal() != 0;

        } else if (object instanceof TyLong) {

            return ((TyLong) object).getInternal() != 0;

        } else if (object instanceof TyFloat) {

            return ((TyFloat) object).getInternal() != 0;

        } else {

            return true;
        }
    }

    public static boolean isInstanceOf(TyObject object, String className) {

        return object.getObjectClass().isInstanceOf(ClassRegistry.getClass(className));
    }
}

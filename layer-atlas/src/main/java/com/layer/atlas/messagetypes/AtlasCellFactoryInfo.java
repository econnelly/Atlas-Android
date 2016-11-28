package com.layer.atlas.messagetypes;

import android.content.Context;

import com.layer.atlas.annotations.IsType;
import com.layer.atlas.annotations.PreviewText;
import com.layer.atlas.messagetypes.generic.GenericCellFactory;
import com.layer.sdk.messaging.Message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AtlasCellFactoryInfo {
    private final Class<? extends AtlasCellFactory> mCellFactoryClass;
    private final Method isTypeMethod;
    private final Method getPreviewTextMethod;

    private enum MethodType {
        ISTYPE, PREVIEWTEXT
    }

    private AtlasCellFactoryInfo(Class<? extends AtlasCellFactory> mCellFactoryClass, Method isTypeMethod, Method getPreviewTextMethod) {
        this.mCellFactoryClass = mCellFactoryClass;
        this.isTypeMethod = isTypeMethod;
        this.getPreviewTextMethod = getPreviewTextMethod;
    }

    public static AtlasCellFactoryInfo create(Class<? extends AtlasCellFactory> c) {
        Method[] allMethods = c.getDeclaredMethods();
        Method isTypeMethod = null;
        Method previewTextMethod = null;
        if (allMethods != null) {
            for (Method method : allMethods) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (method.isAnnotationPresent(IsType.class) && isValidMethod(paramTypes, MethodType.ISTYPE)) {
                    isTypeMethod = method;
                } else if (method.isAnnotationPresent(PreviewText.class) && isValidMethod(paramTypes, MethodType.PREVIEWTEXT)) {
                    previewTextMethod = method;
                }
            }
        }

        if (isTypeMethod == null && previewTextMethod == null) {
            throw new RuntimeException(c.getSimpleName() + " needs public static methods annotated with @IsType and @PreviewText");
        } else if (isTypeMethod == null) {
            throw new RuntimeException(c.getSimpleName() + " needs a public static method annotated with @IsType that takes one Message argument");
        } else if (previewTextMethod == null) {
            throw new RuntimeException(c.getSimpleName() + " needs a public static method annotated with @PreviewText that takes one Context argument and one Message argument");
        }

        return new AtlasCellFactoryInfo(c, isTypeMethod, previewTextMethod);
    }

    private static boolean isValidMethod(Class<?>[] paramTypes, MethodType methodType) {
        if (paramTypes == null) {
            return false;
        } else if (methodType == MethodType.ISTYPE) {
            return paramTypes.length == 1 && paramTypes[0] == Message.class;
        } else if (methodType == MethodType.PREVIEWTEXT) {
            return paramTypes.length == 2 && paramTypes[0] == Context.class && paramTypes[1] == Message.class;
        }

        return false;
    }

    public Class<? extends AtlasCellFactory> getCellFactoryClass() {
        return mCellFactoryClass;
    }

    public boolean isType(Message message) {
        if (isTypeMethod != null) {
            try {
                return (boolean) isTypeMethod.invoke(null, message);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return false;

    }

    public String getPreviewText(Context context, Message message) {
        if (getPreviewTextMethod != null) {
            try {
                return (String) getPreviewTextMethod.invoke(null, context, message);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return GenericCellFactory.getPreview(context, message);
    }
}

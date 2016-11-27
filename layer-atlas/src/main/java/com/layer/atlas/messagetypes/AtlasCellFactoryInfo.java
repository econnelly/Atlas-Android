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
                if (method.isAnnotationPresent(IsType.class)) {
                    isTypeMethod = method;
                } else if (method.isAnnotationPresent(PreviewText.class)) {
                    previewTextMethod = method;
                }
            }
        }

        return new AtlasCellFactoryInfo(c, isTypeMethod, previewTextMethod);
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

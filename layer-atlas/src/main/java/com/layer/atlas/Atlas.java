package com.layer.atlas;

import android.content.Context;

import com.layer.atlas.messagetypes.AtlasCellFactory;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;

public class Atlas {
    private static Configuration sConfiguration;

    public static void initialize(Configuration configuration) {
        sConfiguration = configuration;
    }

    public static String getPreviewText(Context context, Message message) {

        for (AtlasCellFactory atlasCellFactoryInfo : sConfiguration.getCellFactories()) {
            if (atlasCellFactoryInfo.isType(message)) {
                return atlasCellFactoryInfo.getPreviewText(context, message);
            }
        }

        return getGenericPreviewText(message);
    }

    public static String getGenericPreviewText(Message message) {
        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        b.append("[");
        for (MessagePart part : message.getMessageParts()) {
            if (!isFirst) b.append(", ");
            isFirst = false;
            b.append(part.getSize()).append("-byte ").append(part.getMimeType());
        }
        b.append("]");
        return b.toString();
    }
}

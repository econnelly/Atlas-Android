package com.layer.atlas;

import android.content.Context;

import com.layer.atlas.messagetypes.AtlasCellFactory;
import com.layer.atlas.messagetypes.AtlasCellFactoryInfo;
import com.layer.sdk.messaging.Message;

import java.util.ArrayList;
import java.util.List;

public class Atlas {
    private static Configuration sConfiguration;

    public static void initialize(Configuration configuration) {
        sConfiguration = configuration;
    }

    public static String getPreviewText(Context context, Message message) {

        for (AtlasCellFactoryInfo atlasCellFactoryInfo : sConfiguration.getCellFactories()) {
            if (atlasCellFactoryInfo.isType(message)) {
                return atlasCellFactoryInfo.getPreviewText(context, message);
            }
        }

        return null;
    }

    public static List<AtlasCellFactory> getCellFactoryInstances() {
        List<AtlasCellFactory> factoryList = new ArrayList<>();
        for (AtlasCellFactoryInfo atlasCellFactoryInfo : sConfiguration.getCellFactories()) {
            try {
                AtlasCellFactory instance = atlasCellFactoryInfo.getCellFactoryClass().newInstance();
                factoryList.add(instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return factoryList;
    }
}

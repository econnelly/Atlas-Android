package com.layer.atlas;

import com.layer.atlas.messagetypes.AtlasCellFactory;
import com.layer.atlas.messagetypes.AtlasCellFactoryInfo;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private ArrayList<AtlasCellFactoryInfo> mCellFactories;

    private Configuration() {

    }

    public List<AtlasCellFactoryInfo> getCellFactories() {
        return mCellFactories;
    }

    public static class Builder {

        private ArrayList<AtlasCellFactoryInfo> mCellFactories;

        public Builder() {}

        public Builder addCellFactory(Class<? extends AtlasCellFactory> factory) {
            if (mCellFactories == null) {
                mCellFactories = new ArrayList<>();
            }

            mCellFactories.add(AtlasCellFactoryInfo.create(factory));

            return this;
        }

        public Builder addCellFactories(Class<? extends AtlasCellFactory> ... factories) {
            if (mCellFactories == null) {
                mCellFactories = new ArrayList<>();
            }

            for (Class<? extends AtlasCellFactory> factory : factories) {
                mCellFactories.add(AtlasCellFactoryInfo.create(factory));
            }

            return this;
        }

        public Configuration build() {
            Configuration configuration = new Configuration();
            configuration.mCellFactories = new ArrayList<>(mCellFactories);

            return configuration;
        }
    }
}

package com.layer.atlas;

import com.layer.atlas.messagetypes.AtlasCellFactory;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private ArrayList<AtlasCellFactory> mCellFactories;

    private Configuration() {

    }

    public List<AtlasCellFactory> getCellFactories() {
        return mCellFactories;
    }

    public static class Builder {

        private ArrayList<AtlasCellFactory> mCellFactories;

        public Builder() {}

        public Builder addCellFactory(AtlasCellFactory factory) {
            if (mCellFactories == null) {
                mCellFactories = new ArrayList<>();
            }

            mCellFactories.add(factory);

            return this;
        }

        public Builder addCellFactories(AtlasCellFactory ... factories) {
            if (mCellFactories == null) {
                mCellFactories = new ArrayList<>();
            }

            for (AtlasCellFactory factory : factories) {
                mCellFactories.add(factory);
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

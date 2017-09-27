package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Short franchise info.
 */

@AutoValue
public abstract class GameFranchiseInfoShort {
    public static TypeAdapter<GameFranchiseInfoShort> typeAdapter(Gson gson) {
        return new AutoValue_GameFranchiseInfoShort.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GameFranchiseInfoShort.Builder();
    }

    public abstract int id();

    @Nullable
    public abstract String name();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(int id);

        public abstract Builder name(String name);

        public abstract GameFranchiseInfoShort build();
    }
}
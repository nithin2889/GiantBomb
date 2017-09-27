package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Short platform info, used in some server responses
 */

@AutoValue
public abstract class GamePlatformInfoShort {
    public static TypeAdapter<GamePlatformInfoShort> typeAdapter(Gson gson) {
        return new AutoValue_GamePlatformInfoShort.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GamePlatformInfoShort.Builder();
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract GamePlatformInfoShort build();
    }
}
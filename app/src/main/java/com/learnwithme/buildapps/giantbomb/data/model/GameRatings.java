package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by Nithin on 01/09/2017.
 */

@AutoValue
public abstract class GameRatings {
    public static TypeAdapter<GameRatings> typeAdapter(Gson gson) {
        return new AutoValue_GameRatings.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GameRatings.Builder();
    }

    @Nullable
    public abstract String api_detail_url();

    public abstract long id();

    @Nullable
    public abstract String name();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder api_detail_url(String api_detail_url);

        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract GameRatings build();
    }
}
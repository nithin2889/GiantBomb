package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Short game info, used in some server responses
 */

@AutoValue
public abstract class GameInfoListShort {
    public static TypeAdapter<GameInfoListShort> typeAdapter(Gson gson) {
        return new AutoValue_GameInfoListShort.GsonTypeAdapter(gson);
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    public abstract int number_of_user_reviews();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder number_of_user_reviews(int number_of_user_reviews);

        public abstract GameInfoListShort build();
    }
}
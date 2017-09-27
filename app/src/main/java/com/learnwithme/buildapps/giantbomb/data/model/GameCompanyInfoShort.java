package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Short company info.
 */

@AutoValue
public abstract class GameCompanyInfoShort {
    public static TypeAdapter<GameCompanyInfoShort> typeAdapter(Gson gson) {
        return new AutoValue_GameCompanyInfoShort.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GameCompanyInfoShort.Builder();
    }

    public abstract int id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String deck();

    @Nullable
    public abstract String description();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(int id);

        public abstract Builder name(String name);

        public abstract Builder deck(String deck);

        public abstract Builder description(String description);

        public abstract GameCompanyInfoShort build();
    }
}
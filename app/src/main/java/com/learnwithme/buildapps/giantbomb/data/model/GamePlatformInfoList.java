package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Short platform info block, used in main games list
 */

@AutoValue
public abstract class GamePlatformInfoList {
    public static TypeAdapter<GamePlatformInfoList> typeAdapter(Gson gson) {
        return new AutoValue_GamePlatformInfoList.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GamePlatformInfoList.Builder();
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String original_price();

    @Nullable
    public abstract String release_date();

    @Nullable
    public abstract GameCompanyInfoShort company();

    @Nullable
    public abstract GameImages image();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder original_price(String original_price);

        public abstract Builder release_date(String release_date);

        public abstract Builder company(GameCompanyInfoShort company);

        public abstract Builder image(GameImages image);

        public abstract GamePlatformInfoList build();
    }
}
package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Short game info block, used in main games list
 */

@AutoValue
public abstract class GameInfoList {
    public static TypeAdapter<GameInfoList> typeAdapter(Gson gson) {
        return new AutoValue_GameInfoList.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GameInfoList.Builder();
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String aliases();

    @Nullable
    public abstract String api_detail_url();

    @Nullable
    public abstract String date_added();

    @Nullable
    public abstract String date_last_updated();

    @Nullable
    public abstract GameImages image();

    @Nullable
    public abstract String original_release_date();

    /*@Nullable
    public abstract List<GameRatings> original_game_rating();

    @Nullable
    public abstract List<GamePlatformInfoShort> platforms();*/

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder aliases(String aliases);

        public abstract Builder api_detail_url(String api_detail_url);

        public abstract Builder date_added(String date_added);

        public abstract Builder date_last_updated(String date_last_updated);

        public abstract Builder image(GameImages image);

        public abstract Builder original_release_date(String original_release_date);

        /*public abstract Builder original_game_rating(List<GameRatings> original_game_rating);

        public abstract Builder platforms(List<GamePlatformInfoShort> platforms);*/

        public abstract GameInfoList build();
    }
}
package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Full game info.
 * <p>
 * Sample request:
 * /game
 * /3030-11731
 * ?api_key=API_KEY
 * &format=json
 * <p>
 * 3030 - constant record type, 11731 - data_ref_id
 */

@AutoValue
public abstract class GameInfo {
    public static TypeAdapter<GameInfo> typeAdapter(Gson gson) {
        return new AutoValue_GameInfo.GsonTypeAdapter(gson);
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String aliases();

    public abstract int number_of_user_reviews();

    @Nullable
    public abstract String api_detail_url();

    @Nullable
    public abstract String date_added();

    @Nullable
    public abstract String date_last_updated();

    @Nullable
    public abstract String deck();

    @Nullable
    public abstract String description();

    @Nullable
    public abstract String expected_release_day();

    @Nullable
    public abstract String expected_release_month();

    @Nullable
    public abstract String expected_release_quarter();

    @Nullable
    public abstract String expected_release_year();

    @Nullable
    public abstract String original_release_date();

    @Nullable
    public abstract String site_detail_url();

    @Nullable
    public abstract GameImages image();

    @Nullable
    public abstract List<GameRatings> original_game_rating();

    @Nullable
    public abstract List<GamePlatformInfoShort> platforms();

    @Nullable
    public abstract List<GameCharacterInfoShort> characters();
}
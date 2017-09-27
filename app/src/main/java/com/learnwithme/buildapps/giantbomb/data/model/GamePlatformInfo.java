package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Full platform info.
 * <p>
 * Sample request:
 * /platform
 * /3045-145
 * ?api_key=API_KEY
 * &format=json
 * <p>
 * 3045 - constant record type, 145 - data_ref_id
 */

@AutoValue
public abstract class GamePlatformInfo {
    public static TypeAdapter<GamePlatformInfo> typeAdapter(Gson gson) {
        return new AutoValue_GamePlatformInfo.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GamePlatformInfo.Builder();
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String abbreviation();

    @Nullable
    public abstract String date_added();

    @Nullable
    public abstract String date_last_updated();

    @Nullable
    public abstract String deck();

    @Nullable
    public abstract String description();

    @Nullable
    public abstract GameImages image();

    @Nullable
    public abstract String original_price();

    @Nullable
    public abstract String release_date();

    @Nullable
    public abstract GameCompanyInfoShort company();

    @Nullable
    public abstract String api_detail_url();

    @Nullable
    public abstract String install_base();

    public abstract boolean online_support();

    @Nullable
    public abstract String site_detail_url();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(long id);

        public abstract Builder name(String name);

        public abstract Builder abbreviation(String abbreviation);

        public abstract Builder date_added(String date_added);

        public abstract Builder date_last_updated(String date_last_updated);

        public abstract Builder deck(String deck);

        public abstract Builder description(String description);

        public abstract Builder image(GameImages image);

        public abstract Builder original_price(String original_price);

        public abstract Builder release_date(String release_date);

        public abstract Builder api_detail_url(String api_detail_url);

        public abstract Builder company(GameCompanyInfoShort company);

        public abstract Builder install_base(String install_base);

        public abstract Builder online_support(boolean online_support);

        public abstract Builder site_detail_url(String site_detail_url);

        public abstract GamePlatformInfo build();
    }
}
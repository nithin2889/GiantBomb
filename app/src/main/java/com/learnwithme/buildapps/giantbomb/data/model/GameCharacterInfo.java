package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Full character info:
 * <p>
 * Sample request:
 * /character
 * /3005-21137
 * ?api_key=API_KEY
 * &format=json
 * <p>
 * 3005 - constant record type, 21137 - data_ref_id
 */

@AutoValue
public abstract class GameCharacterInfo {
    public static TypeAdapter<GameCharacterInfo> typeAdapter(Gson gson) {
        return new AutoValue_GameCharacterInfo.GsonTypeAdapter(gson);
    }

    @Nullable
    public abstract String aliases();

    @Nullable
    public abstract String api_detail_url();

    @Nullable
    public abstract String birthday();

    @Nullable
    public abstract String date_added();

    @Nullable
    public abstract String date_last_updated();

    @Nullable
    public abstract String deck();

    @Nullable
    public abstract String description();

    @Nullable
    public abstract List<GameFranchiseInfoShort> franchises();

    @Nullable
    public abstract String gender();

    public abstract long id();

    @Nullable
    public abstract GameImages image();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String real_name();
}
package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

/**
 * Short character info block, used in characters list
 */

@AutoValue
public abstract class GameCharacterInfoList {
    public static TypeAdapter<GameCharacterInfoList> typeAdapter(Gson gson) {
        return new AutoValue_GameCharacterInfoList.GsonTypeAdapter(gson);
    }

    public abstract long id();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract GameImages image();

    @Nullable
    public abstract List<GameFranchiseInfoShort> franchises();

    @Nullable
    public abstract String real_name();
}
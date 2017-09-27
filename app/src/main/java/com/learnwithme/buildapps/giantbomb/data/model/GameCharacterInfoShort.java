package com.learnwithme.buildapps.giantbomb.data.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Short character info
 */

@AutoValue
public abstract class GameCharacterInfoShort {
    public static TypeAdapter<GameCharacterInfoShort> typeAdapter(Gson gson) {
        return new AutoValue_GameCharacterInfoShort.GsonTypeAdapter(gson);
    }

    public abstract long id();

    @Nullable
    public abstract String name();
}
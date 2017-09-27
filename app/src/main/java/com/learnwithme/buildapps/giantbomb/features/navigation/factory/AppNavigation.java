package com.learnwithme.buildapps.giantbomb.features.navigation.factory;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AppNavigation {
    public static final int GAMES = 0;
    public static final int PLATFORMS = 1;
    public static final int CHARACTERS = 2;
    public static final int COLLECTION = 3;
    public static final int TRACKER = 4;
    public static final int SETTINGS = 5;

    @Retention(RetentionPolicy.CLASS)
    @IntDef({GAMES, PLATFORMS, CHARACTERS, COLLECTION, TRACKER, SETTINGS})
    public @interface Section {

    }
}
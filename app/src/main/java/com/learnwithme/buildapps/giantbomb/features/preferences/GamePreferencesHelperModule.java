package com.learnwithme.buildapps.giantbomb.features.preferences;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GamePreferencesHelperModule {

    @Provides
    @Singleton
    GamePreferencesHelper providePreferencesHelper(Context context) {
        return new GamePreferencesHelper(context);
    }
}
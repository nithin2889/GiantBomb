package com.learnwithme.buildapps.giantbomb.data.source.local.di.modules;

import android.content.Context;

import com.learnwithme.buildapps.giantbomb.data.source.local.GameDbHelper;
import com.learnwithme.buildapps.giantbomb.data.source.local.LocalDataScope;

import dagger.Module;
import dagger.Provides;

@Module
public class GameDbHelperModule {
    @Provides
    @LocalDataScope
    GameDbHelper provideGameDbHelper(Context context) {
        return new GameDbHelper(context);
    }
}
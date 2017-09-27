package com.learnwithme.buildapps.giantbomb.data.source.local.di.modules;

import android.content.ContentResolver;
import android.content.Context;

import com.learnwithme.buildapps.giantbomb.data.source.local.GameLocalDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.local.LocalDataScope;

import dagger.Module;
import dagger.Provides;

@Module
public class GameLocalDataModule {

    @Provides
    @LocalDataScope
    GameLocalDataHelper provideGameLocalDataHelper(ContentResolver resolver) {
        return new GameLocalDataHelper(resolver);
    }

    @Provides
    @LocalDataScope
    ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }
}
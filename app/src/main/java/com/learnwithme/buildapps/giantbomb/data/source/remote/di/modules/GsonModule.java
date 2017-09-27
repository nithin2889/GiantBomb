package com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.learnwithme.buildapps.giantbomb.data.source.remote.RemoteDataScope;
import com.learnwithme.buildapps.giantbomb.utils.custom.MyGsonAdapterFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {
    @Provides
    @NonNull
    @RemoteDataScope
    Gson provideGson() {
        return new GsonBuilder()
            .registerTypeAdapterFactory(MyGsonAdapterFactory.create())
            .create();
    }
}
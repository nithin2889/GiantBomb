package com.learnwithme.buildapps.giantbomb;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class GiantBombAppModule {
    @NonNull
    private final GiantBombApp app;

    GiantBombAppModule(@NonNull GiantBombApp app) {
        this.app = app;
    }

    @Provides
    @NonNull
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @NonNull
    @Singleton
    FirebaseAnalytics provideFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }
}
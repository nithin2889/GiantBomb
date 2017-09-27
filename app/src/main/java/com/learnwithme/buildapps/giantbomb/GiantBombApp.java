package com.learnwithme.buildapps.giantbomb;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class GiantBombApp extends Application {
    private static GiantBombAppComponent giantBombAppComponent;

    public static GiantBombAppComponent getAppComponent() {
        return giantBombAppComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        if (giantBombAppComponent == null) {
            giantBombAppComponent = DaggerGiantBombAppComponent.builder()
                    .giantBombAppModule(new GiantBombAppModule(this))
                    .build();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // We should not init our app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code below.

        if (BuildConfig.DEBUG) {
            Timber.uprootAll();
            Timber.plant(new Timber.DebugTree());

            Stetho.initializeWithDefaults(this);
        }
    }
}
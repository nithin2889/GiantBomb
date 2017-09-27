package com.learnwithme.buildapps.giantbomb.features.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GameAuthenticatorService extends Service {
    private GameAunthenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new GameAunthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
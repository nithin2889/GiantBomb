package com.learnwithme.buildapps.giantbomb.features.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GameSyncService extends Service {
    private static final Object syncAdapterLock = new Object();
    private static GameSyncAdapter syncAdapter;

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock) {
            if(syncAdapter == null) {
                syncAdapter = new GameSyncAdapter(getApplicationContext());
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
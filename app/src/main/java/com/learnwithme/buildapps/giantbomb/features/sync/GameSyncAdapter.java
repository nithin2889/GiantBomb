package com.learnwithme.buildapps.giantbomb.features.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameLocalDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.features.preferences.GamePreferencesHelper;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.NotificationUtils;

import java.util.Set;

import javax.inject.Inject;

import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class GameSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String ACTION_DATA_UPDATED =
            "com.learnwithme.buildapps.giantbomb.ACTION_DATA_UPDATED";

    @Inject
    GameLocalDataHelper localDataHelper;

    @Inject
    GamePreferencesHelper preferencesHelper;

    @Inject
    GameRemoteDataHelper remoteDataHelper;

    GameSyncAdapter(Context context) {
        super(context, true);

        GiantBombApp
            .getAppComponent()
            .plusRemoteComponent(new GameRemoteDataModule())
            .plusLocalComponent(new GameLocalDataModule())
            .inject(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Timber.d("Scheduled sync started...");

        String date = DateTextUtils.getTodayDateString();

        remoteDataHelper
            .getGamesListByDate(date)
            .subscribe(
                // onSuccess
                list -> {
                    localDataHelper.removeAllTodayGamesFromDb();
                    localDataHelper.saveTodayGamesToDb(list);
                    preferencesHelper.setLastSyncDate(date);
                    preferencesHelper.clearDisplayedPlatformsIdList();
                    sendDataUpdatedBroadcast();
                    checkForTrackedPlatformsUpdates();
                    Timber.d("Scheduled sync completed.");
                },
                // onError
                throwable -> Timber.d("Scheduled sync error!")
            );
    }

    private void checkForTrackedPlatformsUpdates() {
        Set<Long> trackedPlatforms = localDataHelper.getTrackedPlatformsIdsFromDb();
        Set<Long> todayPlatforms = localDataHelper.getTodayPlatformsIdsFromDb();
        Set<String> alreadyDisplayedPlatforms = preferencesHelper.getDisplayedPlatformsIdList();

        StringBuilder notificationText = new StringBuilder();

        for (Long trackedPlatformId : trackedPlatforms) {
            // Tracked platform detected
            if (todayPlatforms.contains(trackedPlatformId)) {
                // Platform notification was already displayed, continue
                if (alreadyDisplayedPlatforms.contains(String.valueOf(trackedPlatformId))) {
                    continue;
                }

                // Add platform name to notification
                String platformName = localDataHelper.getTrackedPlatformNameById(trackedPlatformId);
                notificationText.append(platformName);
                notificationText.append(", ");

                // Mark platform as displayed
                preferencesHelper.saveDisplayedPlatformId(trackedPlatformId);
            }
        }

        // If notification text is not empty, display it
        if (notificationText.length() > 0) {
            NotificationUtils.notifyUserAboutUpdate(
                    getContext(),
                    notificationText.deleteCharAt(notificationText.length() - 2).toString());
        }
    }

    private void sendDataUpdatedBroadcast() {
        Context context = getContext();
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED).setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}
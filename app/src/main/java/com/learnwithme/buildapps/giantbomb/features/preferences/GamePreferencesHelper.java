package com.learnwithme.buildapps.giantbomb.features.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;

import java.util.HashSet;
import java.util.Set;

public class GamePreferencesHelper {
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public GamePreferencesHelper(Context context) {
        this.context = context;

        sharedPreferences = context
            .getSharedPreferences(context.getString(R.string.prefs_file_name),
                    Context.MODE_PRIVATE);
    }

    public void markGamesViewAsShowcased() {
        sharedPreferences
            .edit()
            .putBoolean(context.getString(R.string.prefs_games_showcased_key), true)
            .apply();
    }

    public boolean wasGamesViewAsShowcased() {
        return sharedPreferences
            .getBoolean(context.getString(R.string.prefs_games_showcased_key), false);
    }

    public String getSyncPeriod() {
        return sharedPreferences
            .getString(context.getString(R.string.prefs_sync_period_key), "24");
    }

    public Set<String> getDisplayedPlatformsIdList() {
        return sharedPreferences
            .getStringSet(context.getString(R.string.prefs_displayed_notifications), new HashSet<>());
    }

    private String getLastSyncDate() {
        return sharedPreferences.getString(context.getString(R.string.prefs_last_sync_date), "");
    }

    public void setLastSyncDate(String date) {
        sharedPreferences
            .edit()
            .putString(context.getString(R.string.prefs_last_sync_date), date)
            .apply();
    }

    public void saveDisplayedPlatformId(long id) {
        Set<String> alreadySavedIds = getDisplayedPlatformsIdList();
        alreadySavedIds.add(String.valueOf(id));

        sharedPreferences
            .edit()
            .putStringSet(context.getString(R.string.prefs_displayed_notifications),
                    alreadySavedIds)
            .apply();
    }

    public void clearDisplayedPlatformsIdList() {

        if (isLastSyncWasToday()) {
            return;
        }

        sharedPreferences
            .edit()
            .putStringSet(context.getString(R.string.prefs_displayed_notifications),
                    new HashSet<>())
            .apply();
    }

    private boolean isLastSyncWasToday() {
        String lastSyncDate = getLastSyncDate();
        String today = DateTextUtils.getTodayDateString();

        return lastSyncDate.equals(today);
    }
}
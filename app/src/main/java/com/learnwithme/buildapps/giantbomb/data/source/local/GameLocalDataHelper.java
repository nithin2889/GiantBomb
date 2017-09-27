package com.learnwithme.buildapps.giantbomb.data.source.local;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;
import com.learnwithme.buildapps.giantbomb.utils.ContentUtils;

import static com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.*;
import static com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.GameEntry;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Single;

public class GameLocalDataHelper {

    private final ContentResolver contentResolver;

    @Inject
    public GameLocalDataHelper(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void saveTodayGamesToDb(@NonNull List<GameInfoList> games) {
        for (GameInfoList game : games) {
            contentResolver.insert(
                    GameEntry.CONTENT_URI_TODAY_GAMES,
                    ContentUtils.gameInfoToContentValues(game));
        }
    }

    public Single<List<GameInfoList>> getTodayGamesFromDb() {
        return Single.create(e -> {

            Cursor query = contentResolver
                    .query(GameEntry.CONTENT_URI_TODAY_GAMES, null, null, null, null);

            if (query != null) {
                List<GameInfoList> list = ContentUtils.gameInfoFromCursor(query);
                query.close();
                e.onSuccess(list);
            }
        });
    }

    public Single<List<GameInfoList>> getSavedGamesFromDb() {
        return Single.create(e -> {

            Cursor query = contentResolver
                    .query(GameEntry.CONTENT_URI_SAVED_GAMES,
                            null,
                            null,
                            null,
                            GameEntry.COLUMN_GAME_ID);

            if (query != null) {
                List<GameInfoList> list = ContentUtils.gameInfoFromCursor(query);
                query.close();
                e.onSuccess(list);
            }
        });
    }

    public void removeAllTodayGamesFromDb() {
        contentResolver.delete(GameEntry.CONTENT_URI_TODAY_GAMES, null, null);
    }

    public boolean isGameBookmarked(long gameId) {
        boolean result = false;

        Cursor cursor = contentResolver.query(
                GameEntry.CONTENT_URI_SAVED_GAMES,
                null,
                GameEntry.COLUMN_GAME_ID + " = ?",
                new String[]{String.valueOf(gameId)},
                null);

        if (cursor != null) {
            result = cursor.getCount() > 0;
            cursor.close();
        }
        return result;
    }

    public void insertSavedGameToDb(@NonNull GameInfoList game) {
        contentResolver.insert(
            GameEntry.CONTENT_URI_SAVED_GAMES,
                ContentUtils.gameInfoToContentValues(game));
    }

    public void removeSavedGameFromDb(long gameId) {
        Uri deletionUri = ContentUtils
                .buildDetailsUri(GameEntry.CONTENT_URI_SAVED_GAMES, gameId);
        contentResolver.delete(deletionUri, null, null);
    }

    public Set<Long> getTodayPlatformsIdsFromDb() {
        Set<Long> ids = null;

        Cursor query = contentResolver.query(
                GameEntry.CONTENT_URI_TODAY_GAMES,
                new String[]{GameEntry.COLUMN_GAME_PLATFORM_ID},
                null,
                null,
                null);

        if (query != null) {
            ids = ContentUtils.getIdsFromCursor(query);
            query.close();
        }
        return ids;
    }

    public Set<Long> getTrackedPlatformsIdsFromDb() {
        Set<Long> ids = null;

        Cursor query = contentResolver.query(
                TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS,
                new String[]{TrackedPlatformEntry.COLUMN_PLATFORM_ID},
                null,
                null,
                null);

        if (query != null) {
            ids = ContentUtils.getIdsFromCursor(query);
            query.close();
        }
        return ids;
    }

    public String getTrackedPlatformNameById(long platformId) {
        String result = "";

        Cursor cursor = contentResolver.query(
                TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS,
                new String[]{TrackedPlatformEntry.COLUMN_PLATFORM_NAME},
                TrackedPlatformEntry.COLUMN_PLATFORM_ID + " = ?",
                new String[]{String.valueOf(platformId)},
                null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(0);
            cursor.close();
        }
        return result;
    }

    public boolean isPlatformTracked(long platformId) {
        boolean result = false;

        Cursor cursor = contentResolver.query(
                TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS,
                null,
                TrackedPlatformEntry.COLUMN_PLATFORM_ID + " = ?",
                new String[]{String.valueOf(platformId)},
                null);

        if (cursor != null) {
            result = cursor.getCount() > 0;
            cursor.close();
        }
        return result;
    }

    public void saveTrackedPlatformToDb(@NonNull GamePlatformInfoList platform) {
        contentResolver.insert(
                TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS,
                ContentUtils.platformInfoToContentValues(platform));
    }

    public void removeTrackedPlatformFromDb(long platformId) {
        Uri deletionUri = ContentUtils
                .buildDetailsUri(TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS, platformId);

        contentResolver.delete(deletionUri, null, null);
    }
}
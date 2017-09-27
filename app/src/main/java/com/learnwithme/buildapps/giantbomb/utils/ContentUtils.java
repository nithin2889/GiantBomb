package com.learnwithme.buildapps.giantbomb.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

import static com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.*;
import static com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.GameEntry;

public class ContentUtils {
    public static GameInfoList shortenGameInfo(GameInfo game) {
        return GameInfoList.builder()
            .id(game.id())
            .name(game.name())
            .aliases(game.aliases())
            .api_detail_url(game.api_detail_url())
            .date_added(game.date_added())
            .date_last_updated(game.date_last_updated())
            .original_release_date(game.original_release_date())
            .image(game.image())
            .build();
    }

    public static GamePlatformInfoList shortenPlatformInfo(GamePlatformInfo platform) {
        return GamePlatformInfoList.builder()
            .id(platform.id())
            .name(platform.name())
            .original_price(platform.original_price())
            .release_date(platform.release_date())
            .company(platform.company())
            .image(platform.image())
            .build();
    }

    public static ContentValues gameInfoToContentValues(@NonNull GameInfoList game) {
        ContentValues values = new ContentValues();

        values.put(GameEntry.COLUMN_GAME_ID, game.id());
        values.put(GameEntry.COLUMN_GAME_NAME, game.name());
        values.put(GameEntry.COLUMN_GAME_ALIASES, game.aliases());
        values.put(GameEntry.COLUMN_GAME_API_DETAIL_URL, game.api_detail_url());
        values.put(GameEntry.COLUMN_GAME_DATE_ADDED, game.date_added());
        values.put(GameEntry.COLUMN_GAME_DATE_LAST_UPDATED, game.date_last_updated());
        values.put(GameEntry.COLUMN_GAME_ORIGINAL_RELEASE_DATE, game.original_release_date());
        values.put(GameEntry.COLUMN_GAME_SMALL_IMAGE, game.image().small_url());
        values.put(GameEntry.COLUMN_GAME_MEDIUM_IMAGE, game.image().medium_url());
        values.put(GameEntry.COLUMN_GAME_HD_IMAGE, game.image().super_url());

        return values;
    }

    public static Set<Long> getIdsFromCursor(Cursor cursor) {
        Set<Long> result = new HashSet<>(cursor.getCount());
        cursor.moveToPosition(-1);

        while(cursor.moveToNext()) {
            long id = cursor.getLong(0);
            result.add(id);
        }
        return result;
    }

    public static List<GameInfoList> gameInfoFromCursor(Cursor cursor) {
        List<GameInfoList> games = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToPosition(-1);

            while(cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_ID));
                String aliases = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_ALIASES));
                String apiDetailUrl = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_API_DETAIL_URL));
                String dateAdded = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_DATE_ADDED));
                String dateLastUpdated = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_DATE_LAST_UPDATED));
                String originalReleaseDate = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_ORIGINAL_RELEASE_DATE));
                String smallImage = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_SMALL_IMAGE));
                String mediumImage = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_MEDIUM_IMAGE));
                String hdImage = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_HD_IMAGE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(GameEntry.COLUMN_GAME_NAME));

                GameInfoList game = GameInfoList.builder()
                    .id(id)
                    .name(name)
                    .aliases(aliases)
                    .api_detail_url(apiDetailUrl)
                    .date_added(dateAdded)
                    .date_last_updated(dateLastUpdated)
                    .original_release_date(originalReleaseDate)
                    .image(
                        GameImages.builder()
                            .icon_url("")
                            .medium_url(mediumImage)
                            .screen_url("")
                            .small_url(smallImage)
                            .super_url(hdImage)
                            .thumb_url("")
                            .tiny_url("")
                            .build())
                    .build();

                games.add(game);
            }
        }
        return games;
    }

    public static ContentValues platformInfoToContentValues(@NonNull GamePlatformInfoList platform) {
        ContentValues values = new ContentValues();

        Timber.d("platform company: ", platform.company());
        Timber.d("platform company name: ", platform.company().name());

        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_ID, platform.id());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_NAME, platform.name());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_ORIGINAL_PRICE, platform.original_price());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_RELEASE_DATE, platform.release_date());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_COMPANY_NAME, platform.company().name());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_SMALL_IMAGE, platform.image().small_url());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_MEDIUM_IMAGE, platform.image().medium_url());
        values.put(TrackedPlatformEntry.COLUMN_PLATFORM_HD_IMAGE, platform.image().super_url());

        return values;
    }

    public static Uri buildDetailsUri(Uri baseUri, long recordId) {
        return baseUri.buildUpon()
                .appendPath(String.valueOf(recordId))
                .build();
    }
}
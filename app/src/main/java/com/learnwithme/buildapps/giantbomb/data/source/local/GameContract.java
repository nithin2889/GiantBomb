package com.learnwithme.buildapps.giantbomb.data.source.local;

import android.net.Uri;
import android.provider.BaseColumns;

@SuppressWarnings("WeakerAccess")
public class GameContract {

    public static final String CONTENT_AUTHORITY = "com.learnwithme.buildapps.giantbomb";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Data paths
    static final String PATH_TODAY_GAMES = "today_games";
    static final String PATH_SAVED_GAMES = "saved_games";
    static final String PATH_TRACKED_PLATFORMS = "tracked_platforms";

    // Game info record
    public static final class GameEntry implements BaseColumns {
        public static final Uri CONTENT_URI_TODAY_GAMES = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TODAY_GAMES)
                .build();

        public static final String COLUMN_GAME_ID = "game_id";
        public static final String COLUMN_GAME_NAME = "game_name";
        public static final String COLUMN_GAME_ALIASES = "game_aliases";
        public static final String COLUMN_GAME_API_DETAIL_URL = "api_detail_url";
        public static final String COLUMN_GAME_DATE_ADDED = "date_added";
        public static final String COLUMN_GAME_DATE_LAST_UPDATED = "date_last_updated";
        public static final String COLUMN_GAME_ORIGINAL_RELEASE_DATE = "original_release_date";
        public static final String COLUMN_GAME_SMALL_IMAGE = "small_url";
        public static final String COLUMN_GAME_MEDIUM_IMAGE = "medium_url";
        public static final String COLUMN_GAME_HD_IMAGE = "super_url";
        public static final String COLUMN_GAME_RATING_ID = "game_rating_id";
        public static final String COLUMN_GAME_RATING_NAME = "game_rating_name";
        public static final String COLUMN_GAME_PLATFORM_ID = "platform_id";
        public static final String COLUMN_GAME_PLATFORM_NAME = "platform_name";

        public static final String TABLE_NAME_TODAY_GAMES = "today_games";

        // Saved game record (same columns)
        public static final String TABLE_NAME_SAVED_GAMES = "saved_games";

        public static final Uri CONTENT_URI_SAVED_GAMES = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_SAVED_GAMES)
            .build();
    }

    // Tracked platform info record
    public static final class TrackedPlatformEntry implements BaseColumns {
        public static final Uri CONTENT_URI_TRACKED_PLATFORMS = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRACKED_PLATFORMS)
                .build();

        public static final String COLUMN_PLATFORM_ID = "platform_id";
        public static final String COLUMN_PLATFORM_NAME = "platform_name";
        public static final String COLUMN_PLATFORM_ORIGINAL_PRICE = "original_price";
        public static final String COLUMN_PLATFORM_RELEASE_DATE = "release_date";
        public static final String COLUMN_PLATFORM_COMPANY_NAME = "platform_company_name";
        public static final String COLUMN_PLATFORM_SMALL_IMAGE = "small_url";
        public static final String COLUMN_PLATFORM_MEDIUM_IMAGE = "medium_url";
        public static final String COLUMN_PLATFORM_HD_IMAGE = "super_url";

        public static final String TABLE_NAME_TRACKED_PLATFORMS = "tracked_platforms";
    }
}
package com.learnwithme.buildapps.giantbomb.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.GameEntry;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.TrackedPlatformEntry;

public class GameDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "giantbomb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String LONG_TYPE = " BIGINT";
    private static final String SEPARATOR = ",";

    private static final String SQL_CREATE_TODAY_GAMES_TABLE =
        "CREATE TABLE " + GameEntry.TABLE_NAME_TODAY_GAMES + " (" +
            GameEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
            GameEntry.COLUMN_GAME_ID + LONG_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_NAME + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_ALIASES + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_API_DETAIL_URL + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_DATE_ADDED + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_DATE_LAST_UPDATED + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_ORIGINAL_RELEASE_DATE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_SMALL_IMAGE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_MEDIUM_IMAGE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_HD_IMAGE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_RATING_ID + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_RATING_NAME + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_PLATFORM_ID + LONG_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_PLATFORM_NAME + TEXT_TYPE + SEPARATOR +
            " UNIQUE (" + GameEntry.COLUMN_GAME_ID + ") ON CONFLICT REPLACE);";

    private static final String SQL_CREATE_SAVED_GAMES_TABLE =
        "CREATE TABLE " + GameEntry.TABLE_NAME_SAVED_GAMES + " (" +
            GameEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
            GameEntry.COLUMN_GAME_ID + LONG_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_NAME + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_ALIASES + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_API_DETAIL_URL + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_DATE_ADDED + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_DATE_LAST_UPDATED + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_ORIGINAL_RELEASE_DATE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_SMALL_IMAGE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_MEDIUM_IMAGE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_HD_IMAGE + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_RATING_ID + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_RATING_NAME + TEXT_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_PLATFORM_ID + LONG_TYPE + SEPARATOR +
            GameEntry.COLUMN_GAME_PLATFORM_NAME + TEXT_TYPE + SEPARATOR +
            " UNIQUE (" + GameEntry.COLUMN_GAME_ID + ") ON CONFLICT REPLACE);";

    private static final String SQL_CREATE_SAVED_PLATFORMS_TABLE =
        "CREATE TABLE " + TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS + " (" +
                TrackedPlatformEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                TrackedPlatformEntry.COLUMN_PLATFORM_ID + LONG_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_NAME + TEXT_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_ORIGINAL_PRICE + TEXT_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_RELEASE_DATE + TEXT_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_COMPANY_NAME + TEXT_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_SMALL_IMAGE + TEXT_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_MEDIUM_IMAGE + TEXT_TYPE + SEPARATOR +
                TrackedPlatformEntry.COLUMN_PLATFORM_HD_IMAGE + TEXT_TYPE + SEPARATOR +
            " UNIQUE (" + TrackedPlatformEntry.COLUMN_PLATFORM_ID + ") ON CONFLICT REPLACE);";

    public GameDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODAY_GAMES_TABLE);
        db.execSQL(SQL_CREATE_SAVED_GAMES_TABLE);
        db.execSQL(SQL_CREATE_SAVED_PLATFORMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS);
        db.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME_SAVED_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME_TODAY_GAMES);
        onCreate(db);
    }
}
package com.learnwithme.buildapps.giantbomb.data.source.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameDbHelperModule;

import javax.inject.Inject;

import static com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.*;

public class GameProvider extends ContentProvider {

    public static final int CODE_TODAY_GAMES = 100;
    public static final int CODE_SAVED_GAMES = 200;
    public static final int CODE_SAVED_GAMES_WITH_ID = 201;
    public static final int CODE_TRACKED_PLATFORMS = 300;
    public static final int CODE_TRACKED_PLATFORMS_WITH_ID = 301;

    private static final UriMatcher uriMatcher = buildMatcher();

    @Inject
    GameDbHelper gameDbHelper;

    public static UriMatcher buildMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_TODAY_GAMES, CODE_TODAY_GAMES);
        matcher.addURI(authority, PATH_SAVED_GAMES, CODE_SAVED_GAMES);
        matcher.addURI(authority, PATH_SAVED_GAMES + "/#", CODE_SAVED_GAMES_WITH_ID);
        matcher.addURI(authority, PATH_TRACKED_PLATFORMS, CODE_TRACKED_PLATFORMS);
        matcher.addURI(authority, PATH_TRACKED_PLATFORMS + "/#", CODE_TRACKED_PLATFORMS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        GiantBombApp
            .getAppComponent()
            .plusDbHelperComponent(new GameDbHelperModule())
            .inject(this);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor;
        String recordId;

        switch(uriMatcher.match(uri)) {
            case CODE_TODAY_GAMES:
                cursor = gameDbHelper.getReadableDatabase().query(
                        GameEntry.TABLE_NAME_TODAY_GAMES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_SAVED_GAMES:
                cursor = gameDbHelper.getReadableDatabase().query(
                        GameEntry.TABLE_NAME_SAVED_GAMES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_TRACKED_PLATFORMS:
                cursor = gameDbHelper.getReadableDatabase().query(
                        TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_SAVED_GAMES_WITH_ID:
                recordId = uri.getLastPathSegment();
                cursor = gameDbHelper.getReadableDatabase().query(
                        GameEntry.TABLE_NAME_SAVED_GAMES,
                        projection,
                        GameEntry.COLUMN_GAME_ID + " = ?",
                        new String[] {recordId},
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_TRACKED_PLATFORMS_WITH_ID:
                recordId = uri.getLastPathSegment();
                cursor = gameDbHelper.getReadableDatabase().query(
                        TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS,
                        projection,
                        TrackedPlatformEntry.COLUMN_PLATFORM_ID+ " = ?",
                        new String[]{recordId},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri (query): " + uri);
        }
        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = gameDbHelper.getWritableDatabase();
        Uri returnUri;
        long ids;

        switch (uriMatcher.match(uri)) {

            case CODE_TODAY_GAMES:
                ids = db.insert(GameEntry.TABLE_NAME_TODAY_GAMES, null, values);
                if(ids > 0) {
                    returnUri = ContentUris
                            .withAppendedId(GameEntry.CONTENT_URI_TODAY_GAMES, ids);
                } else {
                    throw new SQLException("Failed to insert a row into " + uri);
                }
                break;

            case CODE_SAVED_GAMES:
                ids = db.insert(GameEntry.TABLE_NAME_SAVED_GAMES, null, values);
                if(ids > 0) {
                    returnUri = ContentUris
                            .withAppendedId(GameEntry.CONTENT_URI_SAVED_GAMES, ids);
                } else {
                    throw new SQLException("Failed to insert a row into " + uri);
                }
                break;

            case CODE_TRACKED_PLATFORMS:
                ids = db.insert(TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS, null, values);
                if(ids > 0) {
                    returnUri = ContentUris
                        .withAppendedId(TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS, ids);
                } else {
                    throw new SQLException("Failed to insert a row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri (insert): " + uri);
        }
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = gameDbHelper.getWritableDatabase();
        int rowsInserted = 0;

        switch (uriMatcher.match(uri)) {

            case CODE_TODAY_GAMES:
                db.beginTransaction();
                try {
                    for(ContentValues value : values) {
                        long ids = db.insert(GameEntry.TABLE_NAME_TODAY_GAMES, null, value);
                        if(ids != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_SAVED_GAMES:
                db.beginTransaction();
                try {
                    for(ContentValues value : values) {
                        long ids = db.insert(GameEntry.TABLE_NAME_SAVED_GAMES, null, value);
                        if(ids != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            case CODE_TRACKED_PLATFORMS:
                db.beginTransaction();
                try {
                    for(ContentValues value : values) {
                        long ids = db.insert(TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS, null, value);
                        if(ids != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0) {
                    // noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = gameDbHelper.getWritableDatabase();
        int rowsDeleted;
        String recordId;

        if(null == selection) {
            selection = "1";
        }

        switch (uriMatcher.match(uri)) {
            case CODE_TODAY_GAMES:
                rowsDeleted = db.delete(
                        GameEntry.TABLE_NAME_TODAY_GAMES,
                        selection,
                        selectionArgs);
                Log.d("TODAY_GAMES URI ", uri.toString());
                break;

            case CODE_SAVED_GAMES:
                rowsDeleted = db.delete(
                        GameEntry.TABLE_NAME_SAVED_GAMES,
                        selection,
                        selectionArgs);
                Log.d("SAVED_GAMES URI ", uri.toString());
                break;

            case CODE_TRACKED_PLATFORMS:
                rowsDeleted = db.delete(
                        TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS,
                        selection,
                        selectionArgs);
                Log.d("SAVED_PLATFORM URI ", uri.toString());
                break;

            case CODE_SAVED_GAMES_WITH_ID:
                recordId = uri.getLastPathSegment();
                rowsDeleted = db.delete(
                        GameEntry.TABLE_NAME_SAVED_GAMES,
                        GameEntry.COLUMN_GAME_ID + " = ?",
                        new String[]{recordId});
                Log.d("SAVED_GAME_ID URI ", uri.toString());
                break;

            case CODE_TRACKED_PLATFORMS_WITH_ID:
                recordId = uri.getLastPathSegment();
                rowsDeleted = db.delete(
                        TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS,
                        TrackedPlatformEntry.COLUMN_PLATFORM_ID + " = ?",
                        new String[]{recordId});
                Log.d("SAVED_PLATFORMS_ID URI ", uri.toString());
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri (delete): " + uri);
        }

        if (rowsDeleted != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("update method not implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("getType method not implemented");
    }
}
package com.learnwithme.buildapps.giantbomb;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.TrackedPlatformEntry;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.GameEntry;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameDbHelper;
import com.learnwithme.buildapps.giantbomb.utils.ContentUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GameDbHelperTest {
    private GameDbHelper gameDbHelper;
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        getTargetContext().deleteDatabase(GameDbHelper.DATABASE_NAME);
        gameDbHelper = new GameDbHelper(getTargetContext());
        database = gameDbHelper.getWritableDatabase();
    }

    @Test
    public void testDatabaseTablesCreation() {
        final HashSet<String> tables = new HashSet<>();
        tables.add(GameEntry.TABLE_NAME_TODAY_GAMES);
        tables.add(GameEntry.TABLE_NAME_SAVED_GAMES);
        tables.add(TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS);

        SQLiteDatabase database = gameDbHelper.getReadableDatabase();
        assertEquals(true, database.isOpen());

        Cursor queryCursor = database
                .rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                queryCursor.moveToFirst());

        do {
            tables.remove(queryCursor.getString(0));
        } while (queryCursor.moveToNext());

        assertTrue("Error: Your database was created without all necessary tables",
                tables.isEmpty());

        queryCursor.close();
    }

    @Test
    public void testTodayGamesTableColumnsCreation() {
        Cursor queryCursor = database
                .rawQuery("PRAGMA table_info(" + GameEntry.TABLE_NAME_TODAY_GAMES + ")", null);
        assertTrue("Error: Unable to query the database for table information.",
                queryCursor.moveToFirst());

        final HashSet<String> todayGamesTableColumns = new HashSet<>();
        todayGamesTableColumns.add(GameEntry._ID);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_ID);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_NAME);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_ALIASES);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_API_DETAIL_URL);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_DATE_ADDED);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_DATE_LAST_UPDATED);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_ORIGINAL_RELEASE_DATE);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_SMALL_IMAGE);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_MEDIUM_IMAGE);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_HD_IMAGE);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_RATING_ID);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_RATING_NAME);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_PLATFORM_ID);
        todayGamesTableColumns.add(GameEntry.COLUMN_GAME_PLATFORM_NAME);

        int columnNameIndex = queryCursor.getColumnIndex("name");

        do {
            String columnName = queryCursor.getString(columnNameIndex);
            todayGamesTableColumns.remove(columnName);
        } while (queryCursor.moveToNext());

        assertTrue("Error: The table doesn't contain all required columns",
                todayGamesTableColumns.isEmpty());

        queryCursor.close();
    }

    @Test
    public void testSavedGamesTableColumnsCreation() {
        Cursor queryCursor = database
                .rawQuery("PRAGMA table_info(" + GameEntry.TABLE_NAME_SAVED_GAMES + ")", null);
        assertTrue("Error: Unable to query the database for table information.",
                queryCursor.moveToFirst());

        final HashSet<String> savedGamesTableColumns = new HashSet<>();
        savedGamesTableColumns.add(GameEntry._ID);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_ID);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_NAME);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_ALIASES);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_API_DETAIL_URL);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_DATE_ADDED);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_DATE_LAST_UPDATED);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_ORIGINAL_RELEASE_DATE);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_SMALL_IMAGE);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_MEDIUM_IMAGE);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_HD_IMAGE);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_RATING_ID);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_RATING_NAME);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_PLATFORM_ID);
        savedGamesTableColumns.add(GameEntry.COLUMN_GAME_PLATFORM_NAME);

        int columnNameIndex = queryCursor.getColumnIndex("name");

        do {
            String columnName = queryCursor.getString(columnNameIndex);
            savedGamesTableColumns.remove(columnName);
        } while (queryCursor.moveToNext());

        assertTrue("Error: The table doesn't contain all required columns",
                savedGamesTableColumns.isEmpty());

        queryCursor.close();
    }

    @Test
    public void testTrackedPlatformsTableColumnsCreation() {
        Cursor queryCursor = database
                .rawQuery("PRAGMA table_info(" + TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS + ")", null);
        assertTrue("Error: Unable to query the database for table information.",
                queryCursor.moveToFirst());

        final HashSet<String> trackedPlatformsTableColumns = new HashSet<>();
        trackedPlatformsTableColumns.add(TrackedPlatformEntry._ID);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_ID);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_NAME);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_ORIGINAL_PRICE);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_RELEASE_DATE);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_COMPANY_NAME);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_SMALL_IMAGE);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_MEDIUM_IMAGE);
        trackedPlatformsTableColumns.add(TrackedPlatformEntry.COLUMN_PLATFORM_HD_IMAGE);

        int columnNameIndex = queryCursor.getColumnIndex("name");

        do {
            String columnName = queryCursor.getString(columnNameIndex);
            trackedPlatformsTableColumns.remove(columnName);
        } while (queryCursor.moveToNext());

        assertTrue("Error: The table doesn't contain all required columns",
                trackedPlatformsTableColumns.isEmpty());

        queryCursor.close();
    }

    @Test
    public void testInsertTodayGameRecord() {
        ContentValues testValues = ContentUtils.gameInfoToContentValues(TestUtils.getDummyGameInfo());
        long inserted = database.insert(GameEntry.TABLE_NAME_TODAY_GAMES, null, testValues);
        assertTrue(inserted != -1);

        Cursor queryCursor = database.query(
                GameEntry.TABLE_NAME_TODAY_GAMES,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from the query", queryCursor.moveToFirst());

        TestUtils.validateCurrentRecord("Error: Today's game query validation failed",
                queryCursor, testValues);

        queryCursor.close();
    }

    @Test
    public void testInsertSavedGameRecord() {
        ContentValues testValues = ContentUtils.gameInfoToContentValues(TestUtils.getDummyGameInfo());
        long inserted = database.insert(GameEntry.TABLE_NAME_SAVED_GAMES, null, testValues);
        assertTrue(inserted != -1);

        Cursor queryCursor = database.query(
                GameEntry.TABLE_NAME_SAVED_GAMES,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from the query", queryCursor.moveToFirst());

        TestUtils.validateCurrentRecord("Error: Saved game query validation failed",
                queryCursor, testValues);

        queryCursor.close();
    }

    @Test
    public void testInsertTrackedPlatformsRecord() {
        ContentValues testValues = ContentUtils.platformInfoToContentValues(TestUtils.getDummyPlatformInfo());
        long inserted = database
                .insert(TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS, null, testValues);
        assertTrue(inserted != -1);

        Cursor queryCursor = database.query(
                TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No records returned from the query", queryCursor.moveToFirst());

        TestUtils.validateCurrentRecord("Error: Tracked platform query validation failed",
                queryCursor, testValues);

        queryCursor.close();
    }

    @After
    public void cleanUp() {
        gameDbHelper.close();
        database.close();
    }
}
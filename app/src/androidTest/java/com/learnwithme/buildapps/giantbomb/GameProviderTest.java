package com.learnwithme.buildapps.giantbomb;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.GameEntry;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.TrackedPlatformEntry;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameDbHelper;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameProvider;
import com.learnwithme.buildapps.giantbomb.utils.ContentUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class GameProviderTest {
    private static final Uri TEST_TODAY_GAMES = GameEntry.CONTENT_URI_TODAY_GAMES;
    private static final Uri TEST_SAVED_GAMES = GameEntry.CONTENT_URI_SAVED_GAMES;
    private static final Uri TEST_TRACKED_PLATFORMS = TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS;

    private static final long TEST_GAME_ID = 11731;
    private static final long TEST_PLATFORM_ID = 22;

    private static final Uri TEST_SAVED_GAMES_WITH_ID = ContentUtils
            .buildDetailsUri(TEST_SAVED_GAMES, TEST_GAME_ID);

    private static final Uri TEST_TRACKED_PLATFORMS_WITH_ID = ContentUtils
            .buildDetailsUri(TEST_TRACKED_PLATFORMS, TEST_PLATFORM_ID);

    private UriMatcher testMatcher;
    private Context context;
    private ContentResolver contentResolver;

    @Before
    public void setUp() {
        testMatcher = GameProvider.buildMatcher();
        context = getTargetContext();
        contentResolver = context.getContentResolver();
    }

    @Test
    public void testUriMatcher() {
        assertEquals("Error: Today's games uri was matched incorrectly.",
                testMatcher.match(TEST_TODAY_GAMES),
                GameProvider.CODE_TODAY_GAMES);

        assertEquals("Error: Saved games uri was matched incorrectly.",
                testMatcher.match(TEST_SAVED_GAMES),
                GameProvider.CODE_SAVED_GAMES);

        assertEquals("Error: Tracked platforms uri was matched incorrectly.",
                testMatcher.match(TEST_TRACKED_PLATFORMS),
                GameProvider.CODE_TRACKED_PLATFORMS);

        assertEquals("Error: Saved game by id uri was matched incorrectly.",
                testMatcher.match(TEST_SAVED_GAMES_WITH_ID),
                GameProvider.CODE_SAVED_GAMES_WITH_ID);

        assertEquals("Error: Tracked platform by id uri was matched incorrectly.",
                testMatcher.match(TEST_TRACKED_PLATFORMS_WITH_ID),
                GameProvider.CODE_TRACKED_PLATFORMS_WITH_ID);
    }

    @Test
    public void testProviderRegistration() {
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        ComponentName component = new ComponentName(packageName, GameProvider.class.getName());

        try {
            ProviderInfo providerInfo = packageManager.getProviderInfo(component, 0);
            assertEquals("Error: Detected GameProvider authority: " + providerInfo.authority +
                            ", required GameProvider authority: " + GameContract.CONTENT_AUTHORITY,
                    providerInfo.authority, GameContract.CONTENT_AUTHORITY);
        } catch(PackageManager.NameNotFoundException e) {
            assertTrue("Error: GameProvider not registered at " + context.getPackageName(), false);
        }
    }

    @Test
    public void testTodayGamesQuery() {
        GameDbHelper gameDbHelper = new GameDbHelper(context);
        SQLiteDatabase database = gameDbHelper.getWritableDatabase();
        ContentValues testValues = ContentUtils.gameInfoToContentValues(TestUtils.getDummyGameInfo());

        // Insert content via database
        database.insert(GameEntry.TABLE_NAME_TODAY_GAMES, null, testValues);
        database.close();

        // Query content via provider
        Cursor queryCursor = contentResolver.query(
                GameEntry.CONTENT_URI_TODAY_GAMES,
                null,
                null,
                null,
                null);

        TestUtils.validateCursor("testTodayGamesQuery", queryCursor, testValues);
    }

    @Test
    public void testSavedGamesQuery() {
        GameDbHelper gameDbHelper = new GameDbHelper(context);
        SQLiteDatabase database = gameDbHelper.getWritableDatabase();
        ContentValues testValues = ContentUtils
                .gameInfoToContentValues(TestUtils.getDummyGameInfo());

        // Insert content via database
        database.insert(GameEntry.TABLE_NAME_SAVED_GAMES, null, testValues);
        database.close();

        // Query content via provider
        Cursor queryCursor = contentResolver.query(
                GameEntry.CONTENT_URI_SAVED_GAMES,
                null,
                null,
                null,
                null);

        TestUtils.validateCursor("testSavedGamesQuery", queryCursor, testValues);
    }

    @Test
    public void testTrackedPlatformsQuery() {
        GameDbHelper gameDbHelper = new GameDbHelper(context);
        SQLiteDatabase database = gameDbHelper.getWritableDatabase();
        ContentValues testValues = ContentUtils
            .platformInfoToContentValues(TestUtils.getDummyPlatformInfo());

        // Insert content via database
        database.insert(TrackedPlatformEntry.TABLE_NAME_TRACKED_PLATFORMS, null, testValues);
        database.close();

        // Query content via provider
        Cursor queryCursor = contentResolver.query(
                TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS,
                null,
                null,
                null,
                null);

        TestUtils.validateCursor("testTrackedPlatformsQuery", queryCursor, testValues);
    }

    @Test
    public void testRecordsDeletion() {
        deleteAllRecordsViaProvider();

        Cursor todayGames = contentResolver
                .query(GameEntry.CONTENT_URI_TODAY_GAMES, null, null, null, null);
        assertEquals("Error: Records were not deleted from today games table", 0,
                todayGames.getCount());
        todayGames.close();

        Cursor savedGames = contentResolver
                .query(GameEntry.CONTENT_URI_SAVED_GAMES, null, null, null, null);
        assertEquals("Error: Records were not deleted from saved games table", 0,
                savedGames.getCount());
        savedGames.close();

        Cursor trackedPlatforms = contentResolver
                .query(TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS, null, null, null, null);
        assertEquals("Error: Records were not deleted from tracked platforms table", 0,
                trackedPlatforms.getCount());
        trackedPlatforms.close();
    }

    @Test
    public void testTodayGamesInsert() {
        deleteAllRecordsViaProvider();

        ContentValues testValues = ContentUtils.gameInfoToContentValues(TestUtils.getDummyGameInfo());

        // Insert content via provider
        contentResolver.insert(GameEntry.CONTENT_URI_TODAY_GAMES, testValues);

        // Query content via provider
        Cursor queryCursor = contentResolver.query(
                GameEntry.CONTENT_URI_TODAY_GAMES,
                null,
                null,
                null,
                null);

        TestUtils.validateCursor("testTodayGamesInsert", queryCursor, testValues);
    }

    @Test
    public void testSavedGamesInsert() {
        deleteAllRecordsViaProvider();

        ContentValues testValues = ContentUtils
                .gameInfoToContentValues(TestUtils.getDummyGameInfo());

        // Insert content via provider
        contentResolver.insert(GameEntry.CONTENT_URI_SAVED_GAMES, testValues);

        // Query content via provider
        Cursor queryCursor = contentResolver.query(
                GameEntry.CONTENT_URI_SAVED_GAMES,
                null,
                null,
                null,
                null);

        TestUtils.validateCursor("testSavedGamesInsert", queryCursor, testValues);
    }

    @Test
    public void testTrackedPlatformsInsert() {
        deleteAllRecordsViaProvider();

        ContentValues testValues = ContentUtils
                .platformInfoToContentValues(TestUtils.getDummyPlatformInfo());

        // Insert content via provider
        contentResolver.insert(TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS, testValues);

        // Query content via provider
        Cursor queryCursor = contentResolver.query(
                TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS,
                null,
                null,
                null,
                null);

        TestUtils.validateCursor("testTrackedPlatformsInsert", queryCursor, testValues);
    }

    @After
    public void cleanUp() {
        deleteAllRecordsViaProvider();
    }

    private void deleteAllRecordsViaProvider() {
        contentResolver.delete(GameEntry.CONTENT_URI_TODAY_GAMES, null, null);
        contentResolver.delete(GameEntry.CONTENT_URI_SAVED_GAMES, null, null);
        contentResolver.delete(TrackedPlatformEntry.CONTENT_URI_TRACKED_PLATFORMS, null, null);
    }
}
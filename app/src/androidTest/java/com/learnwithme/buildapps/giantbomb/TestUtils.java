package com.learnwithme.buildapps.giantbomb;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.learnwithme.buildapps.giantbomb.data.model.GameCompanyInfoShort;
import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TestUtils {
    static GameInfoList getDummyGameInfo() {
        return GameInfoList.builder()
            .id(11731)
            .name("name")
            .aliases("aliases")
            .api_detail_url("api_detail_url")
            .date_added("date_added")
            .date_last_updated("date_last_updated")
            .original_release_date("1997-08-31")
            .image(
                GameImages.builder()
                    .icon_url("icon_url")
                    .medium_url("medium_url")
                    .screen_url("screen_url")
                    .small_url("small_url")
                    .super_url("super_url")
                    .thumb_url("thumb_url")
                    .tiny_url("tiny_url")
                    .build())
            .build();
    }

    static GamePlatformInfoList getDummyPlatformInfo() {
        return GamePlatformInfoList.builder()
            .id(1)
            .name("Test game")
            .original_price("original_price")
            .release_date("release_date")
            .company(GameCompanyInfoShort.builder()
                .id(1483)
                .name("name")
                .deck("deck")
                .build())
            .image(
                GameImages.builder()
                    .icon_url("icon_url")
                    .medium_url("medium_url")
                    .screen_url("screen_url")
                    .small_url("small_url")
                    .super_url("super_url")
                    .thumb_url("thumb_url")
                    .tiny_url("tiny_url")
                    .build())
            .build();
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor,
                                      ContentValues expectedValues) {
        Set<Entry<String, Object>> valueSet = expectedValues.valueSet();
        Log.d("valueSet", valueSet.toString());
        for(Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            Log.d("columnName: ", columnName);
            int idx = valueCursor.getColumnIndex(columnName);
            Log.d("columnIndex: ", String.valueOf(idx));
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            Log.d("expectedValue: ", expectedValue);
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
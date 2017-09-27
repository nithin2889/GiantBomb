package com.learnwithme.buildapps.giantbomb;

import com.learnwithme.buildapps.giantbomb.utils.GameTextUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameTextUtilsTest {
    private static final String GAME_NAME = "Nuclear Strike";
    private static final String FORMATTED_NAME_SHORT = "Nuclear Strike";

    @Test
    public void testGetFormattedGameDetailsTitle() {
        assertEquals("getFormattedGameTitle method returned incorrect string!",
                FORMATTED_NAME_SHORT,
                GameTextUtils.getFormattedGameTitle(GAME_NAME));
    }
}
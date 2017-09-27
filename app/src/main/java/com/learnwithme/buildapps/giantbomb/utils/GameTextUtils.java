package com.learnwithme.buildapps.giantbomb.utils;

import java.util.Locale;

public class GameTextUtils {
    // Can be used in future enhancements
    public static String getFormattedGameName(String game, String platform, int number) {
        String name;

        if (game != null) {
            name = String.format(Locale.US, "%s #%d - %s", platform, number, game);
        } else {
            name = String.format(Locale.US, "%s #%d", platform, number);
        }
        return name;
    }

    public static String getFormattedGameTitle(String name) {
        return String.format(Locale.US, "%s", name);
    }
}
package com.learnwithme.buildapps.giantbomb.features.navigation.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.learnwithme.buildapps.giantbomb.features.characterslist.CharactersFragment;
import com.learnwithme.buildapps.giantbomb.features.characterslist.CharactersFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.gameslist.GamesFragment;
import com.learnwithme.buildapps.giantbomb.features.gameslist.GamesFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.gamesmanager.SavedGamesFragment;
import com.learnwithme.buildapps.giantbomb.features.gamesmanager.SavedGamesFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.platformslist.PlatformsFragment;
import com.learnwithme.buildapps.giantbomb.features.platformslist.PlatformsFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.platformstracker.PlatformsTrackerFragment;
import com.learnwithme.buildapps.giantbomb.features.platformstracker.PlatformsTrackerFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.preferences.GamePreferencesFragment;
import com.learnwithme.buildapps.giantbomb.features.preferences.GamePreferencesFragmentBuilder;

public class NavigationFragmentsFactory {

    public static Fragment getFragment(FragmentManager manager, @AppNavigation.Section int type) {
        Fragment fragment = manager.findFragmentByTag(getFragmentTag(type));

        if (fragment != null) {
            return fragment;
        }

        switch (type) {
            case AppNavigation.GAMES:
                return new GamesFragmentBuilder().build();
            case AppNavigation.PLATFORMS:
                return new PlatformsFragmentBuilder().build();
            case AppNavigation.CHARACTERS:
                return new CharactersFragmentBuilder().build();
            case AppNavigation.COLLECTION:
                return new SavedGamesFragmentBuilder().build();
            case AppNavigation.TRACKER:
                return new PlatformsTrackerFragmentBuilder().build();
            case AppNavigation.SETTINGS:
                return new GamePreferencesFragmentBuilder().build();
            default:
                return null;
        }
    }

    public static String getFragmentTag(@AppNavigation.Section int type) {
        switch (type) {
            case AppNavigation.GAMES:
                return GamesFragment.class.getSimpleName();
            case AppNavigation.PLATFORMS:
                return PlatformsFragment.class.getSimpleName();
            case AppNavigation.CHARACTERS:
                return CharactersFragment.class.getSimpleName();
            case AppNavigation.COLLECTION:
                return SavedGamesFragment.class.getSimpleName();
            case AppNavigation.TRACKER:
                return PlatformsTrackerFragment.class.getSimpleName();
            case AppNavigation.SETTINGS:
                return GamePreferencesFragment.class.getSimpleName();
            default:
                return "";
        }
    }
}
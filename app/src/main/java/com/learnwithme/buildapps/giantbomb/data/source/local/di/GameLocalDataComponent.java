package com.learnwithme.buildapps.giantbomb.data.source.local.di;

import com.learnwithme.buildapps.giantbomb.data.source.local.LocalDataScope;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.features.gamesdetails.GameDetailsComponent;
import com.learnwithme.buildapps.giantbomb.features.gameslist.GamesComponent;
import com.learnwithme.buildapps.giantbomb.features.gamesmanager.SavedGamesComponent;
import com.learnwithme.buildapps.giantbomb.features.platformdetails.PlatformDetailsComponent;
import com.learnwithme.buildapps.giantbomb.features.sync.GameSyncAdapter;

import dagger.Subcomponent;

/**
 * Local data component, provides local data helper and preferences injections,
 * depends on remote data component.
 */

@LocalDataScope
@Subcomponent(modules = {GameLocalDataModule.class})
public interface GameLocalDataComponent {
    GamesComponent plusGamesComponent();

    GameDetailsComponent plusGameDetailsComponent();

    PlatformDetailsComponent plusPlatformDetailsComponent();

    SavedGamesComponent plusSavedGamesComponent();

    void inject(GameSyncAdapter adapter);
}
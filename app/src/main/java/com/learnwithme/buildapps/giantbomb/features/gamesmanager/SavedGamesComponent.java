package com.learnwithme.buildapps.giantbomb.features.gamesmanager;

import dagger.Subcomponent;

@SavedGameScope
@Subcomponent
public interface SavedGamesComponent {
    SavedGamesPresenter presenter();

    void inject(SavedGamesFragment fragment);
}
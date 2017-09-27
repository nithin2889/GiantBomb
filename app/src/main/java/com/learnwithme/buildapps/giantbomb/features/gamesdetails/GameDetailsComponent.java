package com.learnwithme.buildapps.giantbomb.features.gamesdetails;

import dagger.Subcomponent;

@GameDetailsScope
@Subcomponent
public interface GameDetailsComponent {
    GameDetailsPresenter presenter();

    void inject(GameDetailsFragment fragment);
}
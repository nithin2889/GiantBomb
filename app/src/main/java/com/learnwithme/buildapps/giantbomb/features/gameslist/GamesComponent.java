package com.learnwithme.buildapps.giantbomb.features.gameslist;

import dagger.Subcomponent;

@GameScope
@Subcomponent
public interface GamesComponent {
    GamesPresenter presenter();

    void inject(GamesFragment fragment);
}
package com.learnwithme.buildapps.giantbomb.features.characterslist;

import dagger.Subcomponent;

@CharacterScope
@Subcomponent
public interface CharactersComponent {
    CharactersPresenter presenter();

    void inject(CharactersFragment fragment);
}
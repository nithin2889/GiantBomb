package com.learnwithme.buildapps.giantbomb.features.characterdetails;

import dagger.Subcomponent;

@CharacterDetailsScope
@Subcomponent
public interface CharacterDetailsComponent {

    CharacterDetailsPresenter presenter();

    void inject(CharacterDetailsFragment fragment);
}
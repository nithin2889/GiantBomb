package com.learnwithme.buildapps.giantbomb.data.source.remote.di;

import com.learnwithme.buildapps.giantbomb.data.source.local.di.GameLocalDataComponent;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.data.source.remote.RemoteDataScope;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.features.characterdetails.CharacterDetailsComponent;
import com.learnwithme.buildapps.giantbomb.features.characterslist.CharactersComponent;
import com.learnwithme.buildapps.giantbomb.features.platformslist.PlatformsComponent;

import dagger.Subcomponent;

/**
 * Remote data component. Provides remote data helper injection, depends on App component.
 */

@RemoteDataScope
@Subcomponent(modules = {GameRemoteDataModule.class})
public interface GameRemoteDataComponent {
    GameLocalDataComponent plusLocalComponent(GameLocalDataModule module);

    PlatformsComponent plusPlatformsComponent();

    CharactersComponent plusCharactersComponent();

    CharacterDetailsComponent plusCharacterDetailsComponent();
}
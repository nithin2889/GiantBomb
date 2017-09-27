package com.learnwithme.buildapps.giantbomb;

import com.learnwithme.buildapps.giantbomb.data.source.local.di.GameDbHelperComponent;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.GameWidgetComponent;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameDbHelperModule;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.GameRemoteDataComponent;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.features.navigation.NavigationActivity;
import com.learnwithme.buildapps.giantbomb.features.preferences.GamePreferencesHelperModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GiantBombAppModule.class, GamePreferencesHelperModule.class})
public interface GiantBombAppComponent {

    GameDbHelperComponent plusDbHelperComponent(GameDbHelperModule module);

    GameRemoteDataComponent plusRemoteComponent(GameRemoteDataModule module);

    GameWidgetComponent plusWidgetComponent();

    void inject(NavigationActivity activity);
}
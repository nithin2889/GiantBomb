package com.learnwithme.buildapps.giantbomb.data.source.local.di;

import com.learnwithme.buildapps.giantbomb.data.source.local.LocalDataScope;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.features.widget.GameWidgetFactory;

import dagger.Subcomponent;

@LocalDataScope
@Subcomponent(modules = {GameLocalDataModule.class})
public interface GameWidgetComponent {
    void inject(GameWidgetFactory factory);
}
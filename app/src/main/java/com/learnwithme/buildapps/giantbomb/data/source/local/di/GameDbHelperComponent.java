package com.learnwithme.buildapps.giantbomb.data.source.local.di;

import com.learnwithme.buildapps.giantbomb.data.source.local.GameProvider;
import com.learnwithme.buildapps.giantbomb.data.source.local.LocalDataScope;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameDbHelperModule;

import dagger.Subcomponent;

/**
 * Separate component for Content Provider injection.
 */

@LocalDataScope
@Subcomponent(modules = {GameDbHelperModule.class})
public interface GameDbHelperComponent {
    void inject(GameProvider provider);
}
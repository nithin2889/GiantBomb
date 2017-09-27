package com.learnwithme.buildapps.giantbomb.features.platformslist;

import dagger.Subcomponent;

@PlatformScope
@Subcomponent
public interface PlatformsComponent {
    PlatformsPresenter presenter();

    void inject(PlatformsFragment fragment);
}
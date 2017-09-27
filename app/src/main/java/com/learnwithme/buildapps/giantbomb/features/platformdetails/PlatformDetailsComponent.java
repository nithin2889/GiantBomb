package com.learnwithme.buildapps.giantbomb.features.platformdetails;

import dagger.Subcomponent;

@PlatformDetailsScope
@Subcomponent
public interface PlatformDetailsComponent {
    PlatformDetailsPresenter presenter();

    void inject(PlatformDetailsFragment fragment);
}
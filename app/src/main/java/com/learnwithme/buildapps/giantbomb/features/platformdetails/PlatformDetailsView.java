package com.learnwithme.buildapps.giantbomb.features.platformdetails;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfo;

public interface PlatformDetailsView extends MvpLceView<GamePlatformInfo> {
    void markAsTracked();

    void unmarkAsTracked();

    void onTrackingClick();
}
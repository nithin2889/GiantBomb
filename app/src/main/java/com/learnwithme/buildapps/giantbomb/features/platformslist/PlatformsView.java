package com.learnwithme.buildapps.giantbomb.features.platformslist;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;

import java.util.List;

interface PlatformsView extends MvpLceView<List<GamePlatformInfoList>> {
    void loadDataByName(String name);

    void showInitialView(boolean show);

    void showEmptyView(boolean show);

    void setTitle(String title);
}
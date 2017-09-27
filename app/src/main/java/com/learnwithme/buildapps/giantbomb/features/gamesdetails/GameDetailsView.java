package com.learnwithme.buildapps.giantbomb.features.gamesdetails;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfo;

interface GameDetailsView extends MvpLceView<GameInfo> {
    void markAsBookmark();

    void unmarkAsBookmark();

    void onBookmarkClick();
}
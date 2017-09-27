package com.learnwithme.buildapps.giantbomb.features.gamesmanager;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;

import java.util.List;

interface SavedGamesView extends MvpLceView<List<GameInfoList>> {
    void setTitle(String title);

    void showEmptyView(boolean show);

    void loadDataFiltered(String filter);
}
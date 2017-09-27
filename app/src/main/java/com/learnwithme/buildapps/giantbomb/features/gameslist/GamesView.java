package com.learnwithme.buildapps.giantbomb.features.gameslist;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;

import java.util.List;

interface GamesView extends MvpLceView<List<GameInfoList>> {
    void setTitle(String title);

    void showEmptyView(boolean show);

    void showErrorToast(String message);

    void chooseDateAndLoadData();

    void loadDataForChosenDate(String date);

    void loadDataFiltered(String filter);
}
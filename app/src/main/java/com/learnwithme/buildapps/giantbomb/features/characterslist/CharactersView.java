package com.learnwithme.buildapps.giantbomb.features.characterslist;

import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfoList;

import java.util.List;

interface CharactersView extends MvpLceView<List<GameCharacterInfoList>> {
    void loadDataByName(String name);

    void showInitialView(boolean show);

    void showEmptyView(boolean show);

    void setTitle(String title);
}
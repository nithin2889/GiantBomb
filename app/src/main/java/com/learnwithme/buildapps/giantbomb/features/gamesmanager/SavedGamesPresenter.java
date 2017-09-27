package com.learnwithme.buildapps.giantbomb.features.gamesmanager;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoShort;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameLocalDataHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class SavedGamesPresenter extends MvpBasePresenter<SavedGamesView> {
    private final GameLocalDataHelper localDataHelper;

    @Inject
    SavedGamesPresenter(GameLocalDataHelper localDataHelper) {
        this.localDataHelper = localDataHelper;
    }

    void loadOwnedIssues() {
        localDataHelper
            .getSavedGamesFromDb()
            .subscribe(getObserver());
    }

    void loadSavedGamesFilteredByName(String name) {
        localDataHelper
            .getSavedGamesFromDb()
            .flatMapObservable(Observable::fromIterable)
            .filter(game -> game.name() != null)
            .filter(game -> game.name().contains(name))
            .toList()
            .subscribe(getObserver());
    }

    private SingleObserver<List<GameInfoList>> getObserver() {
        return new SingleObserver<List<GameInfoList>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                getView().showEmptyView(false);
                getView().showLoading(true);
            }

            @Override
            public void onSuccess(@NonNull List<GameInfoList> list) {
                if (isViewAttached()) {
                    if (list.size() > 0) {
                        // Display content
                        Timber.d("Displaying content...");
                        getView().setData(list);
                        getView().showContent();
                    } else {
                        // Display empty view
                        Timber.d("Displaying empty view...");
                        getView().showLoading(false);
                        getView().showEmptyView(true);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Data loading error: " + e.getMessage());
                if (isViewAttached()) {
                    Timber.d("Displaying error view...");
                    getView().showError(e, false);
                }
            }
        };
    }
}
package com.learnwithme.buildapps.giantbomb.features.gamesdetails;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfo;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameLocalDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;
import com.learnwithme.buildapps.giantbomb.utils.ContentUtils;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class GameDetailsPresenter extends MvpBasePresenter<GameDetailsView> {

    final GameLocalDataHelper localDataHelper;
    final GameRemoteDataHelper remoteDataHelper;

    @Inject
    GameDetailsPresenter(GameLocalDataHelper localDataHelper,
                         GameRemoteDataHelper remoteDataHelper) {
        this.localDataHelper = localDataHelper;
        this.remoteDataHelper = remoteDataHelper;
    }

    void setUpBookmarkIconState(long gameId) {
        if (isViewAttached()) {
            if (isCurrentGameBookmarked(gameId)) {
                getView().markAsBookmark();
            } else {
                getView().unmarkAsBookmark();
            }
        }
    }

    boolean isCurrentGameBookmarked(long gameId) {
        return localDataHelper.isGameBookmarked(gameId);
    }

    void bookmarkGame(GameInfo game) {
        localDataHelper.insertSavedGameToDb(ContentUtils.shortenGameInfo(game));
    }

    void removeBookmark(long gameId) {
        localDataHelper.removeSavedGameFromDb(gameId);
    }

    void loadGameDetails(long gameId) {
        remoteDataHelper
                .getGameDetailsById(gameId)
                .subscribe(getGameDetailsObserver());
    }

    private SingleObserver<GameInfo> getGameDetailsObserver() {
        return new SingleObserver<GameInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("Game details loading started...");
                getView().showLoading(true);
            }

            @Override
            public void onSuccess(@NonNull GameInfo gameInfo) {
                Timber.d("Game details loading completed.");
                if (isViewAttached()) {
                    getView().showLoading(false);
                    getView().setData(gameInfo);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Game details loading error: " + e.getMessage());
                getView().showError(e, false);
            }
        };
    }
}
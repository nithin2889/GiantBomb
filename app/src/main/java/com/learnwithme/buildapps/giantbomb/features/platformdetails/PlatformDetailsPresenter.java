package com.learnwithme.buildapps.giantbomb.features.platformdetails;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfo;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameLocalDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;
import com.learnwithme.buildapps.giantbomb.utils.ContentUtils;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class PlatformDetailsPresenter extends MvpBasePresenter<PlatformDetailsView> {
    private final GameLocalDataHelper localDataHelper;
    private final GameRemoteDataHelper remoteDataHelper;

    @Inject
    PlatformDetailsPresenter(
            GameLocalDataHelper localDataHelper,
            GameRemoteDataHelper remoteDataHelper) {
        this.localDataHelper = localDataHelper;
        this.remoteDataHelper = remoteDataHelper;
    }

    boolean ifTargetGameSaved(long gameId) {
        return localDataHelper.isGameBookmarked(gameId);
    }

    void setUpTrackIconState(long platformId) {
        if (isViewAttached()) {
            if (isCurrentPlatformTracked(platformId)) {
                getView().markAsTracked();
            } else {
                getView().unmarkAsTracked();
            }
        }
    }

    boolean isCurrentPlatformTracked(long platformId) {
        return localDataHelper.isPlatformTracked(platformId);
    }

    void trackPlatform(GamePlatformInfo platform) {
        localDataHelper.saveTrackedPlatformToDb(ContentUtils.shortenPlatformInfo(platform));
    }

    void removeTracking(long platformId) {
        localDataHelper.removeTrackedPlatformFromDb(platformId);
    }

    void loadPlatformDetails(long platformId) {
        remoteDataHelper
                .getPlatformDetailsById(platformId)
                .subscribe(getPlatformDetailsObserver());
    }

    private SingleObserver<GamePlatformInfo> getPlatformDetailsObserver() {
        return new SingleObserver<GamePlatformInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("Platform details loading started...");
                getView().showLoading(true);
            }

            @Override
            public void onSuccess(@NonNull GamePlatformInfo gamePlatformInfo) {
                Timber.d("Platform details loading completed.");
                if (isViewAttached()) {
                    getView().showLoading(false);
                    getView().setData(gamePlatformInfo);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Platform details loading error: " + e.getMessage());
                getView().showError(e, false);
            }
        };
    }
}
package com.learnwithme.buildapps.giantbomb.features.platformslist;

import android.os.Bundle;

import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class PlatformsPresenter extends MvpBasePresenter<PlatformsView> {

    private final FirebaseAnalytics firebaseAnalytics;
    private final GameRemoteDataHelper remoteDataHelper;

    @Inject
    PlatformsPresenter(FirebaseAnalytics firebaseAnalytics,
                      GameRemoteDataHelper remoteDataHelper) {
        this.firebaseAnalytics = firebaseAnalytics;
        this.remoteDataHelper = remoteDataHelper;
    }

    void loadPlatformsData(String platformName) {
        Timber.d("Load platforms by name: " + platformName);
        remoteDataHelper
                .getPlatformsListByName(platformName)
                .subscribe(getObserver());
    }

    void logPlatformSearchEvent(String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "platform");
        firebaseAnalytics.logEvent(AppMeasurement.Event.SEARCH, bundle);
    }

    private SingleObserver<List<GamePlatformInfoList>> getObserver() {
        return new SingleObserver<List<GamePlatformInfoList>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("Platforms data loading started...");
                if (isViewAttached()) {
                    Timber.d("Displaying loading view...");
                    getView().showEmptyView(false);
                    getView().showInitialView(false);
                    getView().showLoading(true);
                }
            }

            @Override
            public void onSuccess(@NonNull List<GamePlatformInfoList> list) {
                if (isViewAttached()) {
                    if (list.size() > 0) {
                        // Display content
                        Timber.d("Displaying content...");
                        getView().setData(list);
                        getView().showContent();
                    } else {
                        // Display empty view
                        Timber.d("Displaying empty view...");
                        getView().showEmptyView(true);
                        getView().showLoading(false);
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Platforms data loading error: " + e.getMessage());
                if (isViewAttached()) {
                    Timber.d("Displaying error view...");
                    getView().showError(e, false);
                    getView().showLoading(false);
                }
            }
        };
    }
}
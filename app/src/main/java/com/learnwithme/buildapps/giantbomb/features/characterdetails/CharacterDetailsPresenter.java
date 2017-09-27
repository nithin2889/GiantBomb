package com.learnwithme.buildapps.giantbomb.features.characterdetails;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfo;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class CharacterDetailsPresenter extends MvpBasePresenter<CharacterDetailsView> {
    private final GameRemoteDataHelper remoteDataHelper;

    @Inject
    CharacterDetailsPresenter(GameRemoteDataHelper remoteDataHelper) {
        this.remoteDataHelper = remoteDataHelper;
    }

    void loadCharacterDetails(long characterId) {
        remoteDataHelper
                .getCharacterDetailsById(characterId)
                .subscribe(getCharacterDetailsObserver());
    }

    private SingleObserver<GameCharacterInfo> getCharacterDetailsObserver() {
        return new SingleObserver<GameCharacterInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("Character details loading started...");
                getView().showLoading(true);
            }

            @Override
            public void onSuccess(@NonNull GameCharacterInfo gameCharacterInfo) {
                Timber.d("Character details loading completed.");
                if (isViewAttached()) {
                    getView().showLoading(false);
                    getView().setData(gameCharacterInfo);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Timber.d("Character details loading error: " + e.getMessage());
                getView().showError(e, false);
            }
        };
    }
}
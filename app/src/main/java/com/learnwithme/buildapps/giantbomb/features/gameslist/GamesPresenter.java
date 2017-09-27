package com.learnwithme.buildapps.giantbomb.features.gameslist;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoShort;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameLocalDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;
import com.learnwithme.buildapps.giantbomb.features.preferences.GamePreferencesHelper;
import com.learnwithme.buildapps.giantbomb.features.sync.GameSyncManager;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.NetworkUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class GamesPresenter extends MvpBasePresenter<GamesView> {

    final GamePreferencesHelper gamePreferencesHelper;
    final GameLocalDataHelper localDataHelper;
    final GameRemoteDataHelper remoteDataHelper;
    final Context context;

    @Inject
    public GamesPresenter(
            GamePreferencesHelper gamePreferencesHelper,
            GameLocalDataHelper localDataHelper,
            GameRemoteDataHelper remoteDataHelper,
            Context context) {
        this.gamePreferencesHelper = gamePreferencesHelper;
        this.localDataHelper = localDataHelper;
        this.remoteDataHelper = remoteDataHelper;
        this.context = context;
    }

    public boolean shouldNotDisplayShowcases() {
        return gamePreferencesHelper.wasGamesViewAsShowcased();
    }

    public void showcaseWasDisplayed() {
        gamePreferencesHelper.markGamesViewAsShowcased();
    }

    public void loadTodayGames(boolean pullToRefresh) {
        if(pullToRefresh) {
            // Sync data with apps sync manager
            Timber.d("Loading and sync started");
            loadTodayGamesFromServer();
        } else {
            // Load and display games from db
            Timber.d("Loading games from db started");
            loadTodayGamesFromDb();
        }
    }

    public void loadTodayGamesFromServer() {
        if(NetworkUtils.isNetworkConnected(context)) {
            GameSyncManager.syncImmediately();
        } else {
            Timber.d("Network unavailable");
            if(isViewAttached()) {
                getView().showLoading(false);
                getView().showContent();
                getView().showErrorToast(context.getString(R.string.error_data_not_available_short));
            }
        }
    }

    public void loadTodayGamesFromDb() {
        localDataHelper
                .getTodayGamesFromDb()
                .subscribe(getObserver());
    }

    public void loadGamesByDate(String date) {
        Timber.d("Load games by date: " + date);
        remoteDataHelper
                .getGamesListByDate(date)
                .subscribe(getObserverFiltered(date, true));
    }

    public void loadGamesByName(String name) {
        Timber.d("Load games by name: " + name);
        remoteDataHelper
            .getGamesListByName(name)
            .subscribe(getObserverFilteredByName(name));
    }

    private SingleObserver<List<GameInfoList>> getObserver() {
        return new SingleObserver<List<GameInfoList>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("Data loading started...");
                if (isViewAttached()) {
                    getView().showEmptyView(false);
                    getView().showLoading(true);
                }
            }

            @Override
            public void onSuccess(@NonNull List<GameInfoList> gameInfoLists) {
                if (isViewAttached()) {
                    getView().setTitle(DateTextUtils.getFormattedDateToday());

                    if (gameInfoLists.size() > 0) {
                        // Display content
                        Timber.d("Displaying content...");
                        getView().setData(gameInfoLists);
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

    private SingleObserver<List<GameInfoList>> getObserverFiltered(String str, boolean isDate) {
        return new SingleObserver<List<GameInfoList>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("games data loading started...");
                if (isViewAttached()) {
                    getView().showEmptyView(false);
                    getView().showLoading(true);
                }
            }

            @Override
            public void onSuccess(@NonNull List<GameInfoList> gameInfoLists) {
                if (isViewAttached()) {
                    String title = isDate ? DateTextUtils.getFormattedDate(str, "MMM d, yyyy") : str;
                    getView().setTitle(title);

                    if (gameInfoLists.size() > 0) {
                        // Display content
                        Timber.d("Displaying content...");
                        getView().setData(gameInfoLists);
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

    private SingleObserver<List<GameInfoList>> getObserverFilteredByName(String name) {
        return new SingleObserver<List<GameInfoList>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Timber.d("games data loading started...");
                if (isViewAttached()) {
                    getView().showEmptyView(false);
                    getView().showLoading(true);
                }
            }

            @Override
            public void onSuccess(@NonNull List<GameInfoList> gameInfoLists) {
                if (isViewAttached()) {
                    getView().setTitle(name);

                    if (gameInfoLists.size() > 0) {
                        // Display content
                        Timber.d("Displaying content...");
                        getView().setData(gameInfoLists);
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
package com.learnwithme.buildapps.giantbomb.features.gameslist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.android.state.State;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.base.BaseLceFragment;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.features.gamesdetails.GameDetailsActivity;
import com.learnwithme.buildapps.giantbomb.features.gamesdetails.GameDetailsFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.navigation.NavigationActivity;
import com.learnwithme.buildapps.giantbomb.features.sync.GameSyncAdapter;
import com.learnwithme.buildapps.giantbomb.utils.FragmentUtils;
import com.learnwithme.buildapps.giantbomb.utils.ViewUtils;
import com.learnwithme.buildapps.giantbomb.utils.custom.ToolbarActionItemTarget;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class GamesFragment extends
        BaseLceFragment<SwipeRefreshLayout, List<GameInfoList>, GamesView, GamesPresenter>
        implements GamesView, SwipeRefreshLayout.OnRefreshListener {
    @BindString(R.string.error_data_not_available)
    String errorNoInternetText;
    @BindString(R.string.msg_no_games_today)
    String emptyViewText;
    @BindString(R.string.games_title_format)
    String titleFormatString;
    @BindInt(R.integer.grid_columns_count)
    int gridColumnsCount;
    @BindBool(R.bool.is_tablet_layout)
    boolean twoPaneMode;

    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.contentView)
    RecyclerView contentView;
    @State
    String title;
    @State
    String chosenDate;
    @State
    String searchQuery;
    private boolean pendingStartupAnimation;
    private GamesComponent gamesComponent;
    private GamesAdapter adapter;
    private UpdatingBroadcastReceiver updatingBroadcastReceiver;
    private IntentFilter updatingIntentFilter;

    private Menu currentMenu;

    // --- FRAGMENTS LIFECYCLE ---

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        if(savedInstanceState == null) {
            pendingStartupAnimation = true;
        }

        refreshLayout.setOnRefreshListener(this);

        updatingBroadcastReceiver = new UpdatingBroadcastReceiver();
        updatingIntentFilter = new IntentFilter();
        updatingIntentFilter.addAction(GameSyncAdapter.ACTION_DATA_UPDATED);

        adapter = new GamesAdapter(gameId -> {
            if(twoPaneMode) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = new GameDetailsFragmentBuilder(gameId).build();

                FragmentUtils.replaceFragmentIn(
                        manager, fragment, R.id.content_frame, "GameDetailsFragment", true);
            } else {
                startActivity(GameDetailsActivity.prepareIntent(getContext(), gameId));
            }
        });
        adapter.setHasStableIds(true);

        StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

        contentView.setLayoutManager(manager);
        contentView.setHasFixedSize(true);
        contentView.setAdapter(adapter);

        setHasOptionsMenu(true);

        if(isNotNullOrEmpty(searchQuery)) {
            loadDataFiltered(searchQuery);
        } else if(isNotNullOrEmpty(chosenDate)) {
            loadDataForChosenDate(chosenDate);
        } else if(savedInstanceState != null) {
            loadData(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updatingBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updatingBroadcastReceiver, updatingIntentFilter);
    }

    // --- OPTIONS MENU ---

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_games_list, menu);

        currentMenu = menu;

        ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_search, R.color.material_color_white);
        ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_choose_date, R.color.material_color_white);

        setUpSearchItem(menu);

        if(isNotNullOrEmpty(searchQuery)) {
            showClearQueryMenuItem(true);
        } else {
            showClearQueryMenuItem(false);
        }

        if(pendingStartupAnimation) {
            hideToolbar();
            pendingStartupAnimation = false;
            startToolbarAnimation();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_choose_date:
                chooseDateAndLoadData();
                break;
            case R.id.action_clear_search_query:
                showClearQueryMenuItem(false);
                searchQuery = "";
                if(isNotNullOrEmpty(chosenDate)) {
                    loadDataForChosenDate(chosenDate);
                } else {
                    loadData(false);
                }
                break;
        }
        return true;
    }

    // --- BASE LCE FRAGMENT ---

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_games;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String actualMessage = e.getMessage();
        if (actualMessage.contains("Unable to resolve host") ||
                actualMessage.contains("timeout")) {
            return errorNoInternetText;
        }
        return e.getMessage();
    }

    @NonNull
    @Override
    public GamesPresenter createPresenter() {
        return gamesComponent.presenter();
    }

    @Override
    protected void onToolbarAnimationEnd() {
        showcaseToolbarItems();
    }

    @Override
    protected void injectDependencies() {
        gamesComponent = GiantBombApp.getAppComponent()
            .plusRemoteComponent(new GameRemoteDataModule())
            .plusLocalComponent(new GameLocalDataModule())
            .plusGamesComponent();
        gamesComponent.inject(this);
    }

    // --- MVP VIEW STATE ---

    @Override
    public List<GameInfoList> getData() {
        return adapter == null ? null : adapter.getGames();
    }

    @Override
    public void setData(List<GameInfoList> data) {
        adapter.setGames(data);
        adapter.notifyDataSetChanged();
    }

    // --- MVP VIEW ---

    @NonNull
    @Override
    public LceViewState<List<GameInfoList>, GamesView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void setTitle(String date) {
        title = String.format(Locale.US, titleFormatString, date);
        updateTitle();
    }

    @Override
    public void showEmptyView(boolean show) {
        refreshLayout.setRefreshing(false);
        if(show) {
            emptyView.setText(emptyViewText);
            emptyView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showContent() {
        super.showContent();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        refreshLayout.setRefreshing(false);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
        refreshLayout.setRefreshing(pullToRefresh);

        if(!pullToRefresh) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void chooseDateAndLoadData() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
            (view, year, monthOfYear, dayOfMonth) -> {
                chosenDate = String.format(Locale.US, "%d-%02d-%02d", year,
                        monthOfYear + 1, dayOfMonth);
                loadDataForChosenDate(chosenDate);
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH));

        dpd.setAccentColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @Override
    public void loadDataForChosenDate(String date) {
        presenter.loadGamesByDate(date);
    }

    @Override
    public void loadDataFiltered(String filter) {
        presenter.loadGamesByName(filter);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        chosenDate = "";
        presenter.loadTodayGames(pullToRefresh);
    }

    // --- SWIPE TO REFRESH LAYOUT ---
    @Override
    public void onRefresh() {
        loadData(true);
    }

    // --- MISC UTILITY FUNCTIONS ---
    private void setUpSearchItem(Menu menu) {
        // Find items
        MaterialSearchView searchView = ButterKnife.findById(getActivity(), R.id.search_view);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        // Tweaks
        searchView.setVoiceSearch(false);
        searchView.setMenuItem(menuItem);

        // Listeners
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;

                if (searchQuery.length() > 0) {
                    showClearQueryMenuItem(true);
                    loadDataFiltered(searchQuery);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void updateTitle() {
        ActionBar supportActionBar = ((NavigationActivity) getActivity()).getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    void showcaseToolbarItems() {
        if (presenter.shouldNotDisplayShowcases()) {
            return;
        }

        Toolbar toolbar = ButterKnife.findById(getActivity(), R.id.toolbar);

        if (toolbar != null) {
            // Show first showcase
            new ShowcaseView.Builder(getActivity())
                .setTarget(new ToolbarActionItemTarget(toolbar, R.id.action_choose_date))
                .withMaterialShowcase()
                .hideOnTouchOutside()
                .setStyle(R.style.ShowCaseTealPopup)
                .setContentTitle(R.string.showcase_games_title)
                .setContentText(R.string.showcase_games_datepicker)
                .setShowcaseEventListener(new SimpleShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                    super.onShowcaseViewHide(showcaseView);
                    // Show second showcase
                    new ShowcaseView.Builder(getActivity())
                        .setTarget(new ToolbarActionItemTarget(toolbar, R.id.action_search))
                        .withMaterialShowcase()
                        .hideOnTouchOutside()
                        .setStyle(R.style.ShowCaseDarkYellowPopup)
                        .setContentTitle(R.string.showcase_games_title)
                        .setContentText(R.string.showcase_games_search)
                        .build()
                        .show();
                    }
                })
                .build()
                .show();
            presenter.showcaseWasDisplayed();
        }
    }

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    void showClearQueryMenuItem(boolean show) {
        currentMenu.findItem(R.id.action_choose_date).setVisible(!show);
        currentMenu.findItem(R.id.action_search).setVisible(!show);
        currentMenu.findItem(R.id.action_clear_search_query).setVisible(show);
    }

    private class UpdatingBroadcastReceiver extends BroadcastReceiver {
        UpdatingBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(GameSyncAdapter.ACTION_DATA_UPDATED.equals(action)) {
                Timber.d("Action received: " + action);
                presenter.loadTodayGamesFromDb();
            }
        }
    }
}
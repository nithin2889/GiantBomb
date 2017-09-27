package com.learnwithme.buildapps.giantbomb.features.gamesmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.evernote.android.state.State;
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
import com.learnwithme.buildapps.giantbomb.utils.FragmentUtils;
import com.learnwithme.buildapps.giantbomb.utils.ViewUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;
import java.util.Locale;

import butterknife.BindBool;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class SavedGamesFragment extends
        BaseLceFragment<RecyclerView, List<GameInfoList>, SavedGamesView, SavedGamesPresenter>
        implements SavedGamesView {

    @BindString(R.string.msg_no_games_owned)
    String emptyViewText;
    @BindString(R.string.games_title_format)
    String titleFormatString;
    @BindInt(R.integer.grid_columns_count)
    int gridColumnsCount;
    @BindString(R.string.navigation_collection)
    String gameCollectionText;
    @BindBool(R.bool.is_tablet_layout)
    boolean twoPaneMode;

    @BindView(R.id.emptyView)
    TextView emptyView;
    @BindView(R.id.contentView)
    RecyclerView contentView;

    @State
    String title;

    @State
    String searchQuery;

    private SavedGamesComponent savedGamesComponent;
    private SavedGamesAdapter adapter;
    private Menu currentMenu;
    private boolean pendingStartupAnimation;

    // --- FRAGMENTS LIFECYCLE ---
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        if(savedInstanceState == null) {
            pendingStartupAnimation = true;
        }

        adapter = new SavedGamesAdapter(gameId -> {
            if(twoPaneMode) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = new GameDetailsFragmentBuilder(gameId).build();

                FragmentUtils.replaceFragmentIn(
                        manager, fragment, R.id.content_frame, "GameDetailsFragment", true);
            } else {
                startActivity(GameDetailsActivity.prepareIntent(getContext(), gameId));
            }
        });

        StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(gridColumnsCount, StaggeredGridLayoutManager.VERTICAL);

        contentView.setLayoutManager(manager);
        contentView.setHasFixedSize(true);
        contentView.setAdapter(adapter);

        setHasOptionsMenu(true);

        if(isNotNullOrEmpty(searchQuery)) {
            loadDataFiltered(searchQuery);
        } else if(savedInstanceState != null) {
            loadData(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(false);
    }

    // --- OPTIONS MENU ---

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_saved_games_list, menu);

        currentMenu = menu;

        ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_search, R.color.material_color_white);
        ViewUtils.tintMenuIcon(getContext(), menu, R.id.action_clear_search_query,
                R.color.material_color_white);

        setUpSearchItem(menu);

        if (isNotNullOrEmpty(searchQuery)) {
            showClearQueryMenuItem(true);
        } else {
            showClearQueryMenuItem(false);
        }

        if (pendingStartupAnimation) {
            hideToolbar();
            pendingStartupAnimation = false;
            startToolbarAnimation();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_search_query:
                showClearQueryMenuItem(false);
                loadData(false);
                break;
        }
        return true;
    }

    // --- BASE LCE FRAGMENT ---

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_saved_games;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public SavedGamesPresenter createPresenter() {
        return savedGamesComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        savedGamesComponent = GiantBombApp.getAppComponent()
                .plusRemoteComponent(new GameRemoteDataModule())
                .plusLocalComponent(new GameLocalDataModule())
                .plusSavedGamesComponent();
        savedGamesComponent.inject(this);
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
    public LceViewState<List<GameInfoList>, SavedGamesView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void setTitle(String date) {
        title = String.format(Locale.US, titleFormatString, date);
        updateTitle();
    }

    @Override
    public void showEmptyView(boolean show) {
        if (show) {
            emptyView.setText(emptyViewText);
            emptyView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);

        if (!pullToRefresh) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        setTitle(gameCollectionText);
        presenter.loadOwnedIssues();
    }

    @Override
    public void loadDataFiltered(String filter) {
        setTitle(filter);
        presenter.loadSavedGamesFilteredByName(filter);
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

    private boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    void showClearQueryMenuItem(boolean show) {
        currentMenu.findItem(R.id.action_search).setVisible(!show);
        currentMenu.findItem(R.id.action_clear_search_query).setVisible(show);
    }
}
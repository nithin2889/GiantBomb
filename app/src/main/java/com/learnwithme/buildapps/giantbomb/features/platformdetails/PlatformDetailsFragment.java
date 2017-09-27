package com.learnwithme.buildapps.giantbomb.features.platformdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.base.BaseLceFragment;
import com.learnwithme.buildapps.giantbomb.data.model.GameCompanyInfoShort;
import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoListShort;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfo;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.features.gamesdetails.GameDetailsActivity;
import com.learnwithme.buildapps.giantbomb.features.gamesdetails.GameDetailsFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.features.platformdetails.PlatformDetailsGameAdapter.GamesAdapterCallbacks;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.FragmentUtils;
import com.learnwithme.buildapps.giantbomb.utils.HtmlUtils;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;
import com.learnwithme.buildapps.giantbomb.utils.ViewUtils;

import java.util.List;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class PlatformDetailsFragment extends
        BaseLceFragment<LinearLayout, GamePlatformInfo, PlatformDetailsView, PlatformDetailsPresenter>
        implements PlatformDetailsView {
    @Arg
    long platformId;

    @BindView(R.id.platform_cover)
    ImageView platformCover;
    @BindView(R.id.platform_details_company)
    TextView platformCompany;
    @BindView(R.id.platform_details_name)
    TextView platformName;
    @BindView(R.id.platform_company_name)
    TextView platformOtherCompany;
    @BindView(R.id.platform_release_year)
    TextView platformReleaseYear;
    @BindView(R.id.platform_details_description)
    TextView platformDescription;
    @BindView(R.id.platform_details_games_card)
    CardView gamesView;
    /*@BindView(R.id.platform_details_games_view)
    RecyclerView gamesRecyclerView;*/

    @BindString(R.string.dummy_hyphen)
    String hyphen;
    @BindString(R.string.msg_tracked)
    String messageTracked;
    @BindString(R.string.msg_track_removed)
    String messageUntracked;
    @BindBool(R.bool.is_tablet_layout)
    boolean twoPaneMode;

    private PlatformDetailsComponent platformDetailsComponent;
    private GamePlatformInfo currentPlatformInfo;
    private PlatformDetailsGameAdapter gamesAdapter;
    private Menu currentMenu;

    // --- FRAGMENT LIFECYCLE ---
    @Override
    public void onResume() {
        super.onResume();
        // Force recyclerView update so it can display actual bookmarks
        gamesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        gamesAdapter = new PlatformDetailsGameAdapter(new GamesAdapterCallbacks() {
            @Override
            public void gameClicked(long issueId) {
                if (twoPaneMode) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    Fragment fragment = new GameDetailsFragmentBuilder(issueId).build();

                    FragmentUtils.replaceFragmentIn(
                            manager, fragment, R.id.content_frame, "GameDetailsFragment", true);
                } else {
                    startActivity(GameDetailsActivity.prepareIntent(getContext(), issueId));
                }
            }

            @Override
            public boolean isGameTracked(long gameId) {
                return presenter.ifTargetGameSaved(gameId);
            }
        });

        gamesAdapter.setHasStableIds(true);

        /*LinearLayoutManager manager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        gamesRecyclerView.setLayoutManager(manager);
        gamesRecyclerView.setHasFixedSize(true);
        gamesRecyclerView.setNestedScrollingEnabled(false);
        gamesRecyclerView.setAdapter(gamesAdapter);

        gamesRecyclerView
                .addItemDecoration(new DividerItemDecoration(getContext(), manager.getOrientation()));*/

        if (savedInstanceState != null) {
            loadData(false);
        }
    }

    // --- OPTIONS MENU ---

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_platform_details, menu);

        currentMenu = menu;

        presenter.setUpTrackIconState(platformId);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_track:
                onTrackingClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- BASE LCE FRAGMENT ---

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_platform_details;
    }

    @NonNull
    @Override
    public PlatformDetailsPresenter createPresenter() {
        return platformDetailsComponent.presenter();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    protected void injectDependencies() {
        platformDetailsComponent = GiantBombApp.getAppComponent()
                .plusRemoteComponent(new GameRemoteDataModule())
                .plusLocalComponent(new GameLocalDataModule())
                .plusPlatformDetailsComponent();

        platformDetailsComponent.inject(this);
    }

    // --- MVP VIEW STATE ---

    @Override
    public GamePlatformInfo getData() {
        return currentPlatformInfo;
    }

    @Override
    public void setData(GamePlatformInfo data) {
        currentPlatformInfo = data;
        bindVolumeToUi(currentPlatformInfo);
    }

    // --- MVP VIEW ---

    @NonNull
    @Override
    public LceViewState<GamePlatformInfo, PlatformDetailsView> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        errorView.setText(R.string.error_game_not_available);
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        if (pullToRefresh) {
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        } else {
            contentView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadPlatformDetails(platformId);
    }

    @Override
    public void markAsTracked() {
        currentMenu.findItem(R.id.action_track).setIcon(R.drawable.ic_notifications_black_24dp);

        ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_track,
                R.color.material_color_white);
    }

    @Override
    public void unmarkAsTracked() {
        currentMenu.findItem(R.id.action_track).setIcon(R.drawable.ic_notifications_none_black_24dp);

        ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_track,
                R.color.material_color_white);
    }

    @Override
    public void onTrackingClick() {

        if (currentPlatformInfo == null) {
            return;
        }

        String message;

        boolean isTrackedNow = presenter.isCurrentPlatformTracked(platformId);

        if (isTrackedNow) {
            presenter.removeTracking(platformId);
            message = messageUntracked;
        } else {
            presenter.trackPlatform(currentPlatformInfo);
            message = messageTracked;
        }

        presenter.setUpTrackIconState(platformId);

        int parentLayoutId = (twoPaneMode) ?
                R.id.main_coordinator_layout :
                R.id.platform_details_activity_layout;

        Snackbar.make(
                ButterKnife.findById(getActivity(), parentLayoutId),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    // --- UI BINDING UTILS ---

    private void bindVolumeToUi(GamePlatformInfo platform) {

        loadHeaderImage(platformCover, platform.image());

        String name = platform.name();
        platformName.setText(name);

        String releaseDate = DateTextUtils.getFormattedDate(platform.release_date(), "MMM d, yyyy");
        platformReleaseYear.setText(releaseDate);

        GameCompanyInfoShort company = platform.company();
        setUpCompany(platformCompany, company);
        setUpDescription(platformDescription, platform.description());
        setUpCompanyList(platformOtherCompany, platform.company());
    }

    private void loadHeaderImage(ImageView header, GameImages image) {
        if (image != null) {
            String imageUrl = image.small_url();
            ImageUtils.loadImageWithTopCrop(header, imageUrl);
        } else {
            header.setVisibility(View.GONE);
        }
    }

    private void setUpCompany(TextView textView, GameCompanyInfoShort company) {
        if (company != null) {
            textView.setText(company.name());
        } else {
            textView.setText(hyphen);
        }
    }

    private void setUpDescription(TextView textView, String description) {
        if (description != null) {
            textView.setText(HtmlUtils.parseHtmlText(description));
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void setUpCompanyList(TextView textView, GameCompanyInfoShort company) {
        if (company != null) {
            textView.setText(company.name());
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
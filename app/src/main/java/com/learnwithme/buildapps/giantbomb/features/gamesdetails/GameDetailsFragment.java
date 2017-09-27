package com.learnwithme.buildapps.giantbomb.features.gamesdetails;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.base.BaseLceFragment;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfoShort;
import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfo;
import com.learnwithme.buildapps.giantbomb.data.source.local.di.modules.GameLocalDataModule;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.features.characterdetails.CharacterDetailsActivity;
import com.learnwithme.buildapps.giantbomb.features.characterdetails.CharacterDetailsFragmentBuilder;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.FragmentUtils;
import com.learnwithme.buildapps.giantbomb.utils.GameTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.HtmlUtils;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;
import com.learnwithme.buildapps.giantbomb.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class GameDetailsFragment
    extends BaseLceFragment<LinearLayout, GameInfo, GameDetailsView, GameDetailsPresenter>
    implements GameDetailsView {

    @Arg
    long gameId;

    @BindView(R.id.game_details_screen)
    ImageView gameScreen;
    @BindView(R.id.game_details_full_name)
    TextView gameFullTitleName;
    @BindView(R.id.game_details_date_added)
    TextView gameDateAdded;
    @BindView(R.id.game_details_date_last_updated)
    TextView gameDateLastUpdated;
    @BindView(R.id.game_details_description)
    TextView gameDescription;
    @BindView(R.id.game_details_characters_card)
    CardView charactersView;
    @BindView(R.id.game_details_characters_list)
    ListView charactersList;

    @BindString(R.string.msg_bookmarked)
    String messageBookmarked;
    @BindString(R.string.msg_bookmark_removed)
    String messageBookmarkRemoved;
    @BindBool(R.bool.is_tablet_layout)
    boolean twoPaneMode;

    private GameDetailsComponent gameDetailsComponent;
    private GameInfo currentGame;
    private GameDetailsCharacterAdapter listAdapter;
    private Menu currentMenu;

    // --- FRAGMENT LIFECYCLE ---

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        listAdapter = new GameDetailsCharacterAdapter(new ArrayList<>(0));

        charactersList.setAdapter(listAdapter);

        charactersList.setDivider(
                new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccentDark)));

        charactersList.setDividerHeight(1);

        charactersList.setOnItemClickListener((parent, view1, position, id) -> {
            if(twoPaneMode) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                Fragment fragment = new CharacterDetailsFragmentBuilder(id).build();

                FragmentUtils.replaceFragmentIn(
                        manager, fragment, R.id.content_frame, "CharacterDetailsFragment", true);
            } else {
                startActivity(CharacterDetailsActivity.prepareIntent(getContext(), id));
            }
        });

        if(savedInstanceState != null) {
            loadData(false);
        }
    }

    // --- OPTIONS MENU ---

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_game_details, menu);
        currentMenu = menu;

        presenter.setUpBookmarkIconState(gameId);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_bookmark:
                onBookmarkClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- BASE LCE FRAGMENT ---

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_game_details;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    public GameDetailsPresenter createPresenter() {
        return gameDetailsComponent.presenter();
    }

    @Override
    protected void injectDependencies() {
        gameDetailsComponent = GiantBombApp.getAppComponent()
                .plusRemoteComponent(new GameRemoteDataModule())
                .plusLocalComponent(new GameLocalDataModule())
                .plusGameDetailsComponent();
        gameDetailsComponent.inject(this);
    }

    // --- MVP VIEW STATE ---

    @Override
    public GameInfo getData() {
        return currentGame;
    }

    @Override
    public void setData(GameInfo data) {
        currentGame = data;
        bindGameDataToUi(currentGame);
    }

    // --- MVP VIEW ---

    @NonNull
    @Override
    public LceViewState<GameInfo, GameDetailsView> createViewState() {
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
        presenter.loadGameDetails(gameId);
    }

    @Override
    public void markAsBookmark() {
        currentMenu.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_bookmark_black_24dp);

        ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_bookmark,
                R.color.material_color_white);
    }

    @Override
    public void unmarkAsBookmark() {
        currentMenu.findItem(R.id.action_bookmark).setIcon(R.drawable.ic_bookmark_border_black_24dp);

        ViewUtils.tintMenuIcon(getContext(), currentMenu, R.id.action_bookmark,
                R.color.material_color_white);
    }

    @Override
    public void onBookmarkClick() {
        if (currentGame == null) {
            return;
        }

        String message;

        boolean isBookmarkedNow = presenter.isCurrentGameBookmarked(gameId);
        if (isBookmarkedNow) {
            presenter.removeBookmark(gameId);
            message = messageBookmarkRemoved;
        } else {
            presenter.bookmarkGame(currentGame);
            message = messageBookmarked;
        }

        presenter.setUpBookmarkIconState(gameId);

        int parentLayoutId = (twoPaneMode) ?
                R.id.main_coordinator_layout :
                R.id.game_details_activity_layout;

        Snackbar.make(
                ButterKnife.findById(getActivity(), parentLayoutId),
                message,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    // --- UI BINDING UTILS ---

    private void bindGameDataToUi(GameInfo game) {
        loadHeaderImage(gameScreen, game.image());
        String gameName = game.name();
        setUpText(gameFullTitleName, gameName);
        setUpDate(gameDateAdded, DateTextUtils.getFormattedDate(game.date_added(), "MMM d, yyyy"));
        setUpDate(gameDateLastUpdated, DateTextUtils.getFormattedDate(game.date_last_updated(), "MMM d, yyyy"));
        setUpDescription(gameDescription, game.description());
        setUpCharactersList(charactersView, charactersList, game.characters());
    }

    private void loadHeaderImage(ImageView header, GameImages image) {
        if(image != null) {
            String imageUrl = image.small_url();
            ImageUtils.loadImageWithTopCrop(header, imageUrl);
        } else {
            header.setVisibility(View.GONE);
        }
    }

    private void setUpText(TextView textView, String name) {
        textView.setText(GameTextUtils.getFormattedGameTitle(name));
    }

    private void setUpDate(TextView textView, String date) {
        if(date != null) {
            textView.setText(date);
        } else {
            textView.setText("-");
        }
    }

    private void setUpDescription(TextView textView, String description) {
        if (description != null) {
            textView.setText(HtmlUtils.parseHtmlText(description));
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void setUpCharactersList(CardView parent, ListView listView,
                                     List<GameCharacterInfoShort> characters) {
        if (characters != null && !characters.isEmpty()) {
            listAdapter.replaceCharacters(characters);
            ViewUtils.setListViewHeightBasedOnChildren(listView);
        } else {
            parent.setVisibility(View.GONE);
        }
    }
}
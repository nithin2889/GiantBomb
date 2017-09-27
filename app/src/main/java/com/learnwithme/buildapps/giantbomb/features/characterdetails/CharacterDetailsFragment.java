package com.learnwithme.buildapps.giantbomb.features.characterdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby3.mvp.viewstate.lce.data.RetainingLceViewState;
import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.base.BaseLceFragment;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GameFranchiseInfoShort;
import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules.GameRemoteDataModule;
import com.learnwithme.buildapps.giantbomb.utils.DateTextUtils;
import com.learnwithme.buildapps.giantbomb.utils.HtmlUtils;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;

import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;

@SuppressWarnings("WeakerAccess")
@FragmentWithArgs
public class CharacterDetailsFragment extends
        BaseLceFragment<CardView, GameCharacterInfo, CharacterDetailsView, CharacterDetailsPresenter>
        implements CharacterDetailsView {
    @Arg
    long characterId;

    @BindView(R.id.character_details_name)
    TextView characterName;
    @BindView(R.id.character_details_screen)
    ImageView characterPoster;
    @BindView(R.id.character_detail_real_name)
    TextView characterRealName;
    @BindView(R.id.character_detail_aliases)
    TextView characterAliases;
    @BindView(R.id.character_detail_birthdate)
    TextView characterBirthdate;
    @BindView(R.id.character_detail_date_added)
    TextView characterDateAdded;
    @BindView(R.id.character_detail_date_last_updated)
    TextView characterDateLastUpdated;
    @BindView(R.id.character_detail_franchise)
    TextView characterFranchise;
    @BindView(R.id.character_details_description)
    TextView characterDescription;

    @BindString(R.string.dummy_hyphen)
    String hyphen;
    @BindString(R.string.character_franchise_name)
    String franchiseFormat;

    private CharacterDetailsComponent characterDetailsComponent;
    private GameCharacterInfo currentCharacterInfo;

    // --- FRAGMENT LIFECYCLE ---

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        if (savedInstanceState != null) {
            loadData(false);
        }
    }

    // --- BASE LCE FRAGMENT ---

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_character_details;
    }

    @NonNull
    @Override
    public CharacterDetailsPresenter createPresenter() {
        return characterDetailsComponent.presenter();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    protected void injectDependencies() {
        characterDetailsComponent = GiantBombApp.getAppComponent()
                .plusRemoteComponent(new GameRemoteDataModule())
                .plusCharacterDetailsComponent();

        characterDetailsComponent.inject(this);
    }

    // --- MVP VIEW STATE ---

    @Override
    public GameCharacterInfo getData() {
        return currentCharacterInfo;
    }

    @Override
    public void setData(GameCharacterInfo data) {
        currentCharacterInfo = data;
        bindVolumeToUi(currentCharacterInfo);
    }

    // --- MVP VIEW ---

    @NonNull
    @Override
    public LceViewState<GameCharacterInfo, CharacterDetailsView> createViewState() {
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
        presenter.loadCharacterDetails(characterId);
    }

    // --- UI BINDING UTILS ---

    private void bindVolumeToUi(GameCharacterInfo character) {
        loadHeaderImage(characterPoster, character.image());
        setUsualText(characterName, character.name());
        setUsualText(characterRealName, character.real_name());
        setUsualText(characterAliases, character.aliases());
        setUsualText(characterBirthdate, character.birthday());
        setUsualText(characterDateAdded, DateTextUtils.getFormattedDate(character.date_added(), "MMM d, yyyy"));
        setUsualText(characterDateLastUpdated, DateTextUtils.getFormattedDate(character.date_last_updated(), "MMM d, yyyy"));

        if(character.franchises() != null) {
            for(GameFranchiseInfoShort franchises : character.franchises()) {
                setFranchise(characterFranchise, franchises);
            }
        }

        setDescription(characterDescription, character.description());
    }

    private void loadHeaderImage(ImageView header, GameImages image) {
        if (image != null) {
            String imageUrl = image.small_url();
            ImageUtils.loadImageWithTopCrop(header, imageUrl);
        } else {
            header.setVisibility(View.GONE);
        }
    }

    private void setUsualText(TextView textView, String text) {
        if (text != null) {
            textView.setText(text);
        } else {
            textView.setText(hyphen);
        }
    }

    private void setFranchise(TextView textView, GameFranchiseInfoShort franchise) {
        if (franchise != null) {
            String franchiseName = String.format(Locale.US, franchiseFormat, franchise.name());
            textView.setText(franchiseName);
        } else {
            textView.setText(hyphen);
        }
    }

    private void setDescription(TextView textView, String description) {
        if (description != null) {
            textView.setText(HtmlUtils.parseHtmlText(description));
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
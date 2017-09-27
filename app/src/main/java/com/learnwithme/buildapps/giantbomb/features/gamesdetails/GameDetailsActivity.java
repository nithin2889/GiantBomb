package com.learnwithme.buildapps.giantbomb.features.gamesdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.evernote.android.state.State;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.base.BaseActivity;
import com.learnwithme.buildapps.giantbomb.utils.FragmentUtils;

import butterknife.BindView;

@SuppressWarnings("WeakerAccess")
public class GameDetailsActivity extends BaseActivity {

    private static final String EXTRA_GAMER_ID_ARG = "current_game_id";

    @State
    long chosenGameId;

    @BindView(R.id.game_details_toolbar)
    Toolbar toolbar;

    public static Intent prepareIntent(Context context, long gameId) {
        Intent intent = new Intent(context, GameDetailsActivity.class);
        intent.putExtra(EXTRA_GAMER_ID_ARG, gameId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        chosenGameId = getIdFromExtras(extras);

        GameDetailsFragment fragment =
                (GameDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.game_details_container);

        if(fragment == null) {
            fragment = new GameDetailsFragmentBuilder(chosenGameId).build();
            FragmentUtils.addFragmentTo(getSupportFragmentManager(), fragment,
                    R.id.game_details_container);
        }
    }

    private long getIdFromExtras(Bundle extras) {
        if(extras != null && extras.containsKey(EXTRA_GAMER_ID_ARG)) {
            return extras.getLong(EXTRA_GAMER_ID_ARG);
        }
        return 1L;
    }
}
package com.learnwithme.buildapps.giantbomb.features.platformdetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoListShort;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class PlatformDetailsGameAdapter extends
        RecyclerView.Adapter<PlatformDetailsGameAdapter.GameViewHolder> {

    final GamesAdapterCallbacks listener;
    private List<GameInfoListShort> games;

    PlatformDetailsGameAdapter(GamesAdapterCallbacks listener) {
        games = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_platform_details_game_item, parent, false);

        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.bindTo(games.get(position));
    }

    @Override
    public int getItemCount() {
        return games == null ? 0 : games.size();
    }

    @Override
    public long getItemId(int position) {
        return games.get(position).id();
    }

    public List<GameInfoListShort> getGames() {
        return games;
    }

    public void setGames(List<GameInfoListShort> games) {
        this.games = games;
    }

    interface GamesAdapterCallbacks {

        void gameClicked(long gameId);

        boolean isGameTracked(long gameId);
    }

    class GameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.game_number_reviews)
        TextView gameNumberOfReviews;
        @BindString(R.string.platform_details_game_number)
        String gameNumberFormat;
        @BindView(R.id.game_name)
        TextView gameName;
        @BindView(R.id.game_bookmarked_icon)
        ImageView bookmarkIcon;
        private long currentGameId;

        GameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> listener.gameClicked(currentGameId));
        }

        void bindTo(GameInfoListShort game) {

            currentGameId = game.id();
            int number = game.number_of_user_reviews();
            gameNumberOfReviews.setText(String.format(Locale.US, gameNumberFormat, number));

            String gameNameText = game.name();

            if (gameNameText != null) {
                gameName.setText(gameNameText);
            } else {
                gameName.setVisibility(View.GONE);
            }

            if (listener.isGameTracked(currentGameId)) {
                bookmarkIcon.setImageResource(R.drawable.ic_bookmark_black_24dp);
            } else {
                bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            }
        }
    }
}
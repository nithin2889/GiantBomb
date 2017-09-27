package com.learnwithme.buildapps.giantbomb.features.gameslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.features.gameslist.GamesAdapter.GameViewHolder;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
public class GamesAdapter extends RecyclerView.Adapter<GameViewHolder> {
    final OnGameClickListener listener;
    private List<GameInfoList> games;

    GamesAdapter(OnGameClickListener listener) {
        games = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_games_list_item, parent, false);
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

    public List<GameInfoList> getGames() {
        return games;
    }

    public void setGames(List<GameInfoList> games) {
        this.games = games;
    }

    interface OnGameClickListener {
        void gameClicked(long gameId);
    }

    class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.game_cover)
        ImageView gameCover;
        @BindView(R.id.game_name)
        TextView gameName;
        @BindView(R.id.game_cover_progressbar)
        ProgressBar gameCoverProgressbar;
        private long currentGameId;

        GameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.gameClicked(currentGameId);
        }

        void bindTo(GameInfoList game) {
            currentGameId = game.id();

            String coverUrl = game.image().small_url();
            String gameNameText = game.name();

            if(coverUrl != null) {
                ImageUtils.loadImageWithProgress(gameCover, coverUrl, gameCoverProgressbar);
            }
            gameName.setText(gameNameText);
        }
    }
}
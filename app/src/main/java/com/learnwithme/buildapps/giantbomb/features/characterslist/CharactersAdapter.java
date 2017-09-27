package com.learnwithme.buildapps.giantbomb.features.characterslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GameImages;
import com.learnwithme.buildapps.giantbomb.features.characterslist.CharactersAdapter.CharacterViewHolder;
import com.learnwithme.buildapps.giantbomb.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("WeakerAccess")
class CharactersAdapter extends RecyclerView.Adapter<CharacterViewHolder> {

    final OnPlatformClickListener listener;
    private List<GameCharacterInfoList> characters;

    CharactersAdapter(OnPlatformClickListener listener) {
        characters = new ArrayList<>(0);
        this.listener = listener;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_characters_list_item, parent, false);

        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        holder.bindTo(characters.get(position));
    }

    @Override
    public int getItemCount() {
        return characters == null ? 0 : characters.size();
    }

    List<GameCharacterInfoList> getCharacters() {
        return characters;
    }

    public void setCharacters(List<GameCharacterInfoList> platforms) {
        this.characters = platforms;
    }

    interface OnPlatformClickListener {
        void platformClicked(long characterId);
    }

    class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.character_image_layout)
        FrameLayout imageLayout;
        @BindView(R.id.character_poster)
        ImageView characterPoster;
        @BindView(R.id.character_poster_progressbar)
        ProgressBar progressBar;
        @BindView(R.id.character_name)
        TextView characterName;
        private long currentCharacterId;

        CharacterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bindTo(GameCharacterInfoList character) {
            currentCharacterId = character.id();

            GameImages image = character.image();
            if(image != null) {
                String url = image.small_url();
                ImageUtils.loadImageWithProgress(characterPoster, url, progressBar);
            } else {
                imageLayout.setVisibility(View.GONE);
            }

            characterName.setText(character.name());
        }

        @Override
        public void onClick(View v) {
            listener.platformClicked(currentCharacterId);
        }
    }
}
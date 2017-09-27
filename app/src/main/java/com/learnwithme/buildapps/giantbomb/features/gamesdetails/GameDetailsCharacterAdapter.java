package com.learnwithme.buildapps.giantbomb.features.gamesdetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfoShort;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class GameDetailsCharacterAdapter extends BaseAdapter {
    private List<GameCharacterInfoShort> characters;

    GameDetailsCharacterAdapter(List<GameCharacterInfoShort> characters) {
        this.characters = characters;
    }

    public void replaceCharacters(List<GameCharacterInfoShort> characters) {
        this.characters = characters;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CharacterViewHolder holder;

        if (convertView != null) {
            holder = (CharacterViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_game_details_character_item, parent, false);
            holder = new CharacterViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.bindTo(characters.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return characters.size();
    }

    @Override
    public GameCharacterInfoShort getItem(int position) {
        return characters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return characters.get(position).id();
    }

    class CharacterViewHolder {
        @BindView(R.id.game_details_character_name)
        TextView characterName;

        long characterId;

        CharacterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bindTo(GameCharacterInfoShort character) {
            String name = character.name();
            characterId = character.id();

            if (name != null) {
                characterName.setText(name);
            }
        }
    }
}
package com.learnwithme.buildapps.giantbomb.features.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.learnwithme.buildapps.giantbomb.GiantBombApp;
import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.data.source.local.GameContract.GameEntry;
import com.learnwithme.buildapps.giantbomb.features.navigation.NavigationActivity;
import com.learnwithme.buildapps.giantbomb.utils.GameTextUtils;

import javax.inject.Inject;

import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class GameWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    // Query projection
    private static final String[] PROJECTION = {
            GameEntry.COLUMN_GAME_NAME
    };

    // Columns indexes
    private static final int INDEX_GAME_NAME = 0;

    private final Context context;

    @Inject
    ContentResolver contentResolver;
    private Cursor cursor;

    GameWidgetFactory(Context context) {
        this.context = context;
        this.cursor = null;

        GiantBombApp
            .getAppComponent()
            .plusWidgetComponent()
            .inject(this);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        cursor = contentResolver.query(
                GameEntry.CONTENT_URI_TODAY_GAMES,
                PROJECTION,
                null,
                null,
                null
        );

        Binder.restoreCallingIdentity(identityToken);

        Timber.d("Cursor size: " + cursor.getCount());
    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.today_games_widget_list_item);

        String gameName = cursor.getString(INDEX_GAME_NAME);
        String game = GameTextUtils.getFormattedGameTitle(gameName);

        views.setTextViewText(R.id.widget_game_name, game);

        Intent intent = new Intent(context, NavigationActivity.class);
        views.setOnClickFillInIntent(R.id.widget_list_item, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(), R.layout.today_games_widget_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
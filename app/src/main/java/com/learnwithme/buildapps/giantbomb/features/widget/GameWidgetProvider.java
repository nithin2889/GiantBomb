package com.learnwithme.buildapps.giantbomb.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.learnwithme.buildapps.giantbomb.R;
import com.learnwithme.buildapps.giantbomb.features.navigation.NavigationActivity;
import com.learnwithme.buildapps.giantbomb.features.sync.GameSyncAdapter;
import com.learnwithme.buildapps.giantbomb.features.sync.GameSyncManager;

public class GameWidgetProvider extends AppWidgetProvider {
    private static final String ACTION_CLICK_REFRESH_BUTTON = "ACTION_CLICK_REFRESH_BUTTON";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // Handle data updated broadcast
        if (GameSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }

        // Run forced sync when refresh button clicked
        if (ACTION_CLICK_REFRESH_BUTTON.equals(intent.getAction())) {
            GameSyncManager.syncImmediately();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.today_games_widget);
            views.setRemoteAdapter(R.id.widget_list, new Intent(context, GameWidgetService.class));
            views.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Set intent to refresh button
            Intent refreshIntent = new Intent(context, GameWidgetProvider.class);
            refreshIntent.setAction(ACTION_CLICK_REFRESH_BUTTON);
            PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_refresh_button, refreshPendingIntent);

            // Set pending intent to list item
            Intent intent = new Intent(context, NavigationActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
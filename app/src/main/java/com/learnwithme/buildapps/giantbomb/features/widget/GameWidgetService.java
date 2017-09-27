package com.learnwithme.buildapps.giantbomb.features.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class GameWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GameWidgetFactory(getApplicationContext());
    }
}
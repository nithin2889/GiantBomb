package com.learnwithme.buildapps.giantbomb.data.source.remote.di.modules;

import com.learnwithme.buildapps.giantbomb.data.source.remote.GameRemoteDataHelper;
import com.learnwithme.buildapps.giantbomb.data.source.remote.GiantBombService;
import com.learnwithme.buildapps.giantbomb.data.source.remote.RemoteDataScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {RetrofitModule.class, OkHttpModule.class, GsonModule.class})
public class GameRemoteDataModule {
    @Provides
    @RemoteDataScope
    GiantBombService provideGiantBombService(Retrofit retrofit) {
        return retrofit.create(GiantBombService.class);
    }

    @Provides
    @RemoteDataScope
    GameRemoteDataHelper provideGameRemoteDataHelper(GiantBombService service) {
        return new GameRemoteDataHelper(service);
    }
}
package com.learnwithme.buildapps.giantbomb.data.source.remote;

import com.learnwithme.buildapps.giantbomb.BuildConfig;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.ServerResponse;
import com.learnwithme.buildapps.giantbomb.utils.ClassUtils;
import com.learnwithme.buildapps.giantbomb.utils.RxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;

public class GameRemoteDataHelper {

    private static final String API_KEY = BuildConfig.GIANT_BOMB_API_KEY;

    private final GiantBombService giantBombService;

    @Inject
    public GameRemoteDataHelper(GiantBombService giantBombService) {
        this.giantBombService = giantBombService;
    }

    /**
     * Request games list (search by: original release date)
     *
     * @param date Date string in YYYY-MM-DD format.
     * @return Game info list.
     */
    public Single<List<GameInfoList>> getGamesListByDate(String date) {
        String fields = ClassUtils.getMethodsList(GameInfoList.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("field_list", fields);
        options.put("filter", "original_release_date:" + date);
        options.put("sort", "name:asc");
        options.put("format", "json");

        return giantBombService
            .getGamesList(options)
            .compose(RxUtils.applySchedulers())
            .map(ServerResponse::results)
            .singleOrError();
    }

    /**
     * Request games list (search by: specified name)
     *
     * @param name Target game name.
     * @return Game info list.
     */
    public Single<List<GameInfoList>> getGamesListByName(String name) {
        String fields = ClassUtils.getMethodsList(GameInfoList.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("filter", "name:" + name);
        options.put("field_list", fields);
        options.put("format", "json");

        return giantBombService
            .getGamesList(options)
            .compose(RxUtils.applySchedulers())
            .map(ServerResponse::results)
            .singleOrError();
    }

    /**
     * Request game details (search by: game id).
     *
     * @param gameId Target game id.
     * @return Detailed game info.
     */
    public Single<GameInfo> getGameDetailsById(long gameId) {
        String fields = ClassUtils.getMethodsList(GameInfo.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("field_list", fields);
        options.put("format", "json");

        return giantBombService
                .getGameDetails(gameId, options)
                .compose(RxUtils.applySchedulers())
                .map(ServerResponse::results)
                .singleOrError();
    }

    /**
     * Request platforms list (search by: specified name)
     *
     * @param name Target platform name.
     * @return Platform info list.
     */
    public Single<List<GamePlatformInfoList>> getPlatformsListByName(String name) {
        String fields = ClassUtils.getMethodsList(GamePlatformInfoList.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("filter", "name:" + name);
        options.put("field_list", fields);
        options.put("format", "json");

        return giantBombService
                .getPlatformsList(options)
                .compose(RxUtils.applySchedulers())
                .map(ServerResponse::results)
                .singleOrError();
    }

    /**
     * Request platform details (search by: platform id).
     *
     * @param platformId Target platform id.
     * @return Detailed platform info.
     */
    public Single<GamePlatformInfo> getPlatformDetailsById(long platformId) {

        String fields = ClassUtils.getMethodsList(GamePlatformInfo.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("field_list", fields);
        options.put("format", "json");

        return giantBombService
                .getPlatformDetails(platformId, options)
                .compose(RxUtils.applySchedulers())
                .map(ServerResponse::results)
                .singleOrError();
    }

    /**
     * Request characters list (search by: specified name)
     *
     * @param name Target character name to perform search.
     * @return Characters info list.
     */
    public Single<List<GameCharacterInfoList>> getCharactersListByName(String name) {

        String fields = ClassUtils.getMethodsList(GameCharacterInfoList.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("filter", "name:" + name);
        options.put("field_list", fields);
        options.put("format", "json");

        return giantBombService
                .getCharactersList(options)
                .compose(RxUtils.applySchedulers())
                .map(ServerResponse::results)
                .singleOrError();
    }

    /**
     * Request character details (search by: character id)
     *
     * @param characterId Target character ud.
     * @return Detailed character info.
     */
    public Single<GameCharacterInfo> getCharacterDetailsById(long characterId) {

        String fields = ClassUtils.getMethodsList(GameCharacterInfo.class);

        Map<String, String> options = new HashMap<>();
        options.put("api_key", API_KEY);
        options.put("field_list", fields);
        options.put("format", "json");

        return giantBombService
                .getCharacterDetails(characterId, options)
                .compose(RxUtils.applySchedulers())
                .map(ServerResponse::results)
                .singleOrError();
    }
}
package com.learnwithme.buildapps.giantbomb.data.source.remote;

import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GameCharacterInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GameInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfo;
import com.learnwithme.buildapps.giantbomb.data.model.GamePlatformInfoList;
import com.learnwithme.buildapps.giantbomb.data.model.ServerResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface GiantBombService {
    String ENDPOINT = "https://www.giantbomb.com/";
    String GAME_TYPE_CODE = "3030";
    String PLATFORM_TYPE_CODE = "3045";
    String CHARACTER_TYPE_CODE = "3005";

    // Request games list
    @GET("/api/games/")
    Observable<ServerResponse<List<GameInfoList>>> getGamesList(
            @QueryMap Map<String, String> options);

    // Request game details
    @GET("/api/game/" + GAME_TYPE_CODE + "-{id}/")
    Observable<ServerResponse<GameInfo>> getGameDetails(
            @Path("id") long gameId,
            @QueryMap Map<String, String> options);

    // Request platforms list
    @GET("/api/platforms/")
    Observable<ServerResponse<List<GamePlatformInfoList>>> getPlatformsList(
            @QueryMap Map<String, String> options);

    // Request platform details
    @GET("/api/platform/" + PLATFORM_TYPE_CODE + "-{id}/")
    Observable<ServerResponse<GamePlatformInfo>> getPlatformDetails(
            @Path("id") long volumeId,
            @QueryMap Map<String, String> options);

    // Request characters list
    @GET("/api/characters/")
    Observable<ServerResponse<List<GameCharacterInfoList>>> getCharactersList(
            @QueryMap Map<String, String> options);

    // Request character details
    @GET("/api/character/" + CHARACTER_TYPE_CODE + "-{id}/")
    Observable<ServerResponse<GameCharacterInfo>> getCharacterDetails(
            @Path("id") long characterId,
            @QueryMap Map<String, String> options);
}
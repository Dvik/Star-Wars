package com.starwarspeople.retrofit;

import com.starwarspeople.data.model.PeopleResponse;
import com.starwarspeople.data.model.PlanetResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Divya on 1/22/2017.
 */

public interface PeopleAPI {

    @GET("people/?format=json")
    Call<PeopleResponse> getPeopleData(@Query("page") String page);

    @GET("planets/?format=json")
    Call<PlanetResponse> getPlanet(@Query("id") String id);
}

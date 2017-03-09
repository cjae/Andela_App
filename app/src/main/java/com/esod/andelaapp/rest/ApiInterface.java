package com.esod.andelaapp.rest;


import com.esod.andelaapp.model.APIResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Jedidiah on 25/07/2016.
 */
public interface ApiInterface {

    //--GET REQUEST---------------------------------------------------------------------------------
    @GET
    Call<APIResponse> getDevelopers(@Url String url);
}

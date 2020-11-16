package com.cagriyorguner.geotask.network;

import com.cagriyorguner.geotask.Models.ApiResponseModels.BaseModel;
import com.cagriyorguner.geotask.Models.PostModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface JsonPlaceHolderApi {
    //for travelling salesman problem
    @POST("optimization")
    Call<BaseModel> getOptimizatedLocations(@Header ("Authorization") String apiKey, @Body PostModel postModel);
}

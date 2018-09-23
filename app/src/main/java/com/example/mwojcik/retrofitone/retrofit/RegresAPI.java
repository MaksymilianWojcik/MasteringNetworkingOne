package com.example.mwojcik.retrofitone.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RegresAPI {

    @GET("api/users")
    Call<SimpleModel> loadData(@Query("page") String pageNumber);

}

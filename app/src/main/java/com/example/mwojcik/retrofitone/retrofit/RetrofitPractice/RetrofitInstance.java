package com.example.mwojcik.retrofitone.retrofit.RetrofitPractice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * https://jsonplaceholder.typicode.com/photos
 */
public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";


    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

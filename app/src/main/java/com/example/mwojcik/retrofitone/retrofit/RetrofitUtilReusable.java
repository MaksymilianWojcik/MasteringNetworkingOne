package com.example.mwojcik.retrofitone.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 * Zeby nie isac w kólko builderów, to takie uwygodnienie. Klasa ta
 * pozwala nam tworzyc obiekt raz i reuse go dla lifetimeu aplikacji
 */
public class RetrofitUtilReusable {

    private static final String BASE_URL = "https://www.regres.in/";

    private static Gson gson = new GsonBuilder().setLenient().create();
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
    private static Retrofit retrofit = builder.build();
    private static OkHttpClient.Builder htttpClient = new OkHttpClient.Builder();

    //Np mamy serwis (interfejs retrofita) GithubAPI:
    // GithubAPI service = RetrofitUtilReusable.createService(GithubAPI.class);
    public static <S> S createService(Class<S> sClass){
        return retrofit.create(sClass);
    }
}

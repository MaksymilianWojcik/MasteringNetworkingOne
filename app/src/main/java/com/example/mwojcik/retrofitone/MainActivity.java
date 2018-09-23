package com.example.mwojcik.retrofitone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mwojcik.retrofitone.retrofit.NetworkUtilRetrofit;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NetworkUtilHardWay.downloadResponseFromNetwork();
        NetworkUtilRetrofit networkUtilRetrofit = new NetworkUtilRetrofit();
        networkUtilRetrofit.start();
    }
}

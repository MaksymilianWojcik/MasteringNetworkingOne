package com.example.mwojcik.retrofitone;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mwojcik.retrofitone.retrofit.RetrofitPractice.CustomAdapter;
import com.example.mwojcik.retrofitone.retrofit.RetrofitPractice.GetDataService;
import com.example.mwojcik.retrofitone.retrofit.RetrofitPractice.RetroPhoto;
import com.example.mwojcik.retrofitone.retrofit.RetrofitPractice.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NetworkUtilHardWay.downloadResponseFromNetwork();

//        NetworkUtilRetrofit networkUtilRetrofit = new NetworkUtilRetrofit();
//        networkUtilRetrofit.start();


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        final AlertDialog dialog = builder.create();
        dialog.show();

        GetDataService service = RetrofitInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroPhoto>> call = service.getAllPhotos();
        call.enqueue(new Callback<List<RetroPhoto>>() {
            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                dialog.hide();
                Log.d("RetrofitResponse", response.body().toString());
                List<RetroPhoto> responseList = response.body();
                Log.d("RetrofitResponse", responseList.toString());
                generateDataList(responseList);
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                dialog.hide();
                Log.d("RetrofitResponse", "onFailure!!");
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void generateDataList(List<RetroPhoto> photoList){
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(this, photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

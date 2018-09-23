package com.example.mwojcik.retrofitone.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/***
 *
 * Retrofit to REST Client. Ulatwia otrzymywania i uploadaowanie JSONow czy innych struktur danych przez
 * oparete na REST webserwisy. W Retroficie możemy skonfigurować jaki konwerter chcemy użyć do serializacji
 * danych. Dla np. JSONA możemy użyć GSONa, czy Jacksona, ale możemy dodać customowe konwertery. Retrofit do
 * HTTP Requestów wykorzystuje OkHttp.
 *
 * Przydatne linki
 * - http://www.jsonschema2pojo.org/
 *
 * W Retorficie potrzebujemy tak naprawdę na start 3 klas: model (który mapowany będzie na JSONA),
 * interfejsy które definiują możliwe operacje HTTP, oraz klasę dostarczająca nstancje Retrofit.Buildera (instancja
 * ta wykorzystuje interfejs i Builder API, który pozwala definiować endpointsy URLi do operacji HTTP)
 *
 * Każda metoda interfejsu reprezentuje jeden możliwy API call. Musi mieć więc jakąś adnotacje HTTP, jak np. GET, POST
 * itp. do określenia typu requestu, a także musi mieć relatywny adres URL (endpoint). Wartość zwracana z takiej
 * metody wrapuje responsa w obiekt Call, który jest typu oczekiwanego wyniku.:
 *                  @GET("users")
 *                  Call<List<User>> getUsers();
 * Możemy użyć bloków zastępujących i querować parametry by dostosować URL. Zastępujący blok dodany jest do relatywnego
 * URLa z {}. Z pomocą adnotacji @Path przed parametrem metody, wartość tego parametru jest powioązana z konkretnym
 * blokiem zastępczym:
 *                  @GET("users/{name}/commits")
 *                  Call<List<Commit>> getCommitsByName(@Path("name") String name)
 *
 * Parametry query dodane są z adnotacją @Query do parametru metody. Są automatycznie dodane na końcu URL
 *                  @GET("users")
 *                  Call<User> getUserById(@Query("id") Integer id)
 *
 * Adnotacja @Body na parametrze metody mówi Retrofitowy żeby użył obiektu jako request body calla:
 *                  @POST("users")
 *                  Call<User> postUser(@Body User user)
 *
 * 1. Najpeirw trzeba dodać dependency w gradlu:
 *                  implementation 'com.squareup.retrofit2:retrofit:2.4.0'
 * 2. Dobrze stworzyć sobie klasę z obiektem Retorfita (kontroler retrofita) i zdefiniować REST API dla Retrofita poprzez interfejs
 * 3.
 *
 */
public class NetworkUtilRetrofit implements Callback<SimpleModel> {

    public void start(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //dodajemy sobei interceptora
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                //zeby dodac interceptora
                .client(okHttpClient)
                .build();

        RegresAPI regresAPI = retrofit.create(RegresAPI.class);
        Call<SimpleModel> call = regresAPI.loadData("1");
//        Call call = regresAPI.loadDataSimple("1");
        //Uzywamy enqueue do asynchronicznego wywołania - po co blokowac. Do synchronicznego
        //uzylibysmy call.execute(this);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<SimpleModel> call, Response<SimpleModel> response) {
        if(response.isSuccessful()){
//            List<SimpleModel> simpleModelList = response.body();
            Log.d("RetrofitResponse", String.valueOf(response.body()));
            SimpleModel simpleModel = (SimpleModel) response.body();
            Log.d("RetrofitRespons", String.valueOf(simpleModel.getPage()));
            for (SimpleData data : simpleModel.getSimpleDataList()){
                Log.d("SimpleDataObject", data.toString());
            }
        } else {
            Log.d("RetrofitResponse", "Error!!!!");
        }
    }

    @Override
    public void onFailure(Call<SimpleModel> call, Throwable t) {
        Log.d("RetrofitResponse", "Failure!!!!");
        Log.d("RetrofitResponse", t.toString());
        Log.d("RetrofitResponse", call.toString());
    }

}

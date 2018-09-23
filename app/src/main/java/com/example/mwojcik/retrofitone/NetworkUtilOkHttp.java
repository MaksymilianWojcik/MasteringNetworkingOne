package com.example.mwojcik.retrofitone;


import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 1. Zaczniemy od poznania OkHttp.
 * Jest to 3rd party biblioteka opracowana przez Square, do wysyłania i odbierania opartych na HTTP
 * requestów. Zbudowana jest na bazie biblioteki Okio, która jest efektywniejsza w odczytywaniu i zapisywaniu
 * danych niz standardowe biblioteki I/O Javy poprzez tworzenie tzw. shared memory pool. OkHttp jest także
 * biblioteką leżącą pod Retrofitem, co daje możliwość bezpieczeństwa typów dla interfejsów Api opartych na REST.
 * OkHttp dostarcza implementacje interfejsu HttpUrlConnection, który od akutalnie używany jest w Androidach 4.4+.
 * Dodatkowo: OkHttp v2.4 dostarcza bardziej zupdejtowaną drogę zarządzania URLs. Zamiast java.net.URL, java.net.URI
 * czy android.net.Uri klas, dostarcza nową klasę HttpUrl, która ułatwia nam pobieranie HTTP portu, parsowanie URLi i
 * kanocjalizacje (canonicializing) URL stringów.
 *
 * Wiadomo, zacząć musimy od dodania dependency do gradla.
 *
 * 2. Wysyłanie i odbieranie network requestów
 * Najpierw musimy zainicjalizowac OkHttpClient i stworzyć obiekt Request, jak w metodzie prepareRequest (Ad.1).
 * Jak mamy jakieś parametry query, które chcielibyśmy dodać do naszego linka, to klasa HttpUrl dostarczona
 * przez OkHttp może być wykorzystana do skonstruowania URL (Ad.2)
 *
 * Gdy mamy jeszcze jakiekolwiek authenticated query parametery, headersy także mogą być dodane (Ad.3);
 *
 *
 * 3. Synchorniczne Network Callsy
 * Możemy też stworzyć object Call i rozdzielić network requesty synchronicznie. Z racji tego, że
 * Android nie pozwala na network callsy na main threadzie, możemy robić jedynie syncrhoniczne callsy  tylko
 * na osobnym threadzie lub background serwisie. Możemy np. użyć AsyncTaska do 'lekkich' callsów.
 *                  Response response = client.newCall(request).execute();
 *
 * 4. Asynchroniczne Network Callsy (Ad.4)
 * Możemy także robić asynchroniczne network callsy tworząc obiekt Call, wykorzystując metodę enqueue() i przekazując
 * anonimowy obiekt Callback, który implementuje onFailure() i onResponse() (Ad.4)
 *
 * 5. Updatowanie Widoków na UIThreadzie (Ad.5)
 * Normalnie OkHttp tworzy nowy wokrer thread do rozdzielenia network requestu i używa tego samego threada do obsługi
 * odpowiedzi. Zbudowane jest głównie jako biblioteka Javy więc nie obsługuje ograniczen Androidowego frameworka
 * co tylko permit widoki do zupdejtowania na main UI Threadzie.
 * Z tego powodu, jeżeli spróbojemy uzyskać dostęp czy zupdejtować widoku z zewnątrz main threada w Callbacku, to
 * dostaniemy exceptiona. Jeżeli chcemy updejtować widoki z responsa, to musimy użyć metody runOnUiThread() lub postować
 * result spowrotem na main threada. (Ad.5)
 * Alternatywnie mozna wykorzystac rozszerzenie Clabbacka jak np MainThreadCallback, który wrapuje ten rodzaj zachowania
 * i umieszcza nas w main threadzie juz z response callbacka domyślnie.
 *
 * 6. Processing Network Responsów
 * Zakładając że request nie jest anulowany i nie ma żadnych problemów z połączeniem, to metoda onRespone zostanie
 * wystrzelona. Przekazuje ona obiekt Response który może być użyty do sprawdzenia status kodu, response body i
 * dowolnego headarsa który został zwrócony. Wywoływanie isSuccesful() jest kiedy kod zwrócił status kod 2XX (czyli 200,
 * 201 itp):
 *                  if(!response.isSuccessful()){
 *                      throw new IOException("unexpected code " + response);
 *                  }
 *
 * Haders responses są także dostarczone jako listy:
 *
 *                  Headers responseHeaders = response.headers();
 *                  for(int i = 0; i < responseHeaders.size(); i++){
 *
 *                  }
 *
 * Headersy mogą też być accessed bezpośrednio używając response.header():
 *                  String header = response.header("Date");
 *
 * Możemy też uzyskać response data wywołując response.body() i wywołując string() do odczytania całego payloadu.
 * Zwróćmy uwagę że response.body() może tylko być uruchomione raz i powinno być zrobione na background threadzie.
 *
 *
 * 7. Procesowanie JSON data (Ad.6)
 *
 * 8. Wysyłanie Authenticated Requestów
 * OkHttp posiada mechanizm do modyfikowania outbund requestów używając interceptory. Powszechnym przypadkiem
 * użycia jest OAuth protokół, który wymaga żeby requesty były  podpisane używając prywatny klucz. OkHttp singpost
 * library pracuje z SignPost library do używania interceptora w celu podpisywania każdego requesta. Tym sposobem, caller
 * nie musi pamiętać do podpisywania każdego requesta:
 *
 *                  OkHttpOAuthConsumer consumer = new new OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER)SECRET);
 *                  consumer.setTokenWithSecret(token, secret);
 *                  okHttpClient.interceptors().add(new SigningInterceptor(consumer));
 *
 * 9. Caching Network Requestów
 * Możemy ustawić network caching przekazując cache kiedy budujemy OkHttpClienta:
 *                  int cacheSize = 10 * 1024 * 1024;
 *                  Cache cache = new Cache(new File(getApplication().getCacheDir(),"cacheFileName"),cacheSie);
 *                  OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();
 * Możemy kontrolować czy uzyskać skaszowany response ustawiając cacheControl na requescie. Np. jeżeli chcemy
 * dostać tylko request jeżeli dane są skaszowane, możemy skonstruować obiekt Request tak:
 *                  Request request = new Request.Builder()
 *                      .url("http://publicobject.com/helloworld.txt")
 *                      .cacheControl(new CacheControl.Builder().onlyIfCached().build())
 *                      .build();
 * Możemy także wymusić network response używająć noCache dla requestu:
 *                  .cacheControl(new CacheControl.Builder().noCache().build())
 * Możemy też określić maximum staleness age dla skaszowanego responsu:
 *                  .caheControl(new CacheControl.Builder().maxStale(365, TimeUnit.DAYS).build()
 * Do uzyskania skasszowanego response, możemy zwyczajnie wywołać cacheResponse na obiekcie Response:
 *                  onResponse(Call call, final Response response) throws IOException  {
 *                      final Response text = response.cacheResponse();
 *                      //jak brak skaszowanego response to text bedzie nullem
 *                      if(text!=null){
 *                      }
 *                  }
 * 10. HttpLogInterceptor
 * Jako że OkHttp może być trudny w troubleshootingu, to możemy dodać HttpLogINterceptor, który może być
 * dodany kiedy korzystamy z OkHttp3 library, który printuje wszystkie HTTP requesty/response w LogCacie.
 * Trzeba go dodać w gradlu (com.suareup.okhttp3:logging-interceptor:3.6.0) i dodać network interceptora dla HttpLogInterceptor:
 *                  OkHttpClient.Builder builder = new OkHttpClient.Builder();
 *                  HtpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
 *                  httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
 *                  builder.networkInterceptors().add(httpLoggingInterceptor);
 *                  builder.build();
 */
public class NetworkUtilOkHttp {

    public static void prepareRequest(){
        //Ad.1
        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://publicobject.com/helloworld.txt")
//                .build();

        //Ad.2
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://ajax.googleapis.com/ajax/services/search/images")
                .newBuilder();
        urlBuilder.addQueryParameter("v", "1.0");
        urlBuilder.addQueryParameter("q", "android");
        urlBuilder.addQueryParameter("rsz", "8");
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
//                .header("Authorization-token", "tokenabascadsasdas") - Ad.3
                .url(url)
                .build();
    }

    /***
     * Ad.4
     */
    public static void prepareAsynchronousNetworkCalls(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        //powinnismy tu wziac hadnlera do postowania danych na main threadzie
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw  new IOException("Unexpected code " + response);
                }
            }
        });
    }


    /***
     * Ad.5
     */
    public static void prepareAsynchronousNetworkCallsUpdateMainThread(final MainActivity mainActivity){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //mTextView.setText(responseData);
                            String text = responseData;
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /***
     * Ad.7 Processing JSON data
     */
    public static void processingJsonData(){
        //Załóżmy że chcemy zrobić calla do GitHub API, co zwraca JSON-based data.
        Request request = new Request.Builder()
                .url("https://api.github.com/users/codepath")
                .build();
        //Mozemy tez zdekodować dane przez konwertowanie ich do obiektu JSONObject lub JSONArray, zaleznie
        //response data:
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //Moglibysmy to zrobic takze np. uzywajac biblioteki GSON
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String owner = jsonObject.getString("name");
                } catch (JSONException e){

                }
            }
        });
    }



}

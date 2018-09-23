package com.example.mwojcik.retrofitone;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/***
 * Network requesty wykorzystywane są do uzyskiwania i modyfikowania API danych z serwera. To
 * jest bardzo powszzechne zadanie w developmencei Androida.
 * Podstawową klasą Javową używaną do połączeń sieciowych jest HttpUrlConnection (był jeszcze
 * DefaultHTTPClient ale deprecated od 6.0). Klasa ta jest nisko-levelowa i wymaga całkowicie
 * manualnego zarządzania parsowaniem danych z input streama i executowaniem requestów asynchronicznie.
 * W większości powszechnych przypadków dużo lepiej jest wykorzystać jakąś istniejącą 3rd party bibliotekę
 * zwaną android-async-http lub OkHttp, która może obsłużyć cały proces wysyłania i parsowania network
 * requestów za nas w nieco łatwiejszej drodze.
 * Mamuy też takie, jeszcze wyższe biblioteki, jak np. Volley czy Retrofit.
 * Co już wiemy ale przypomne, z OkHtp musimy pamiętać też o obsłużeniu przypadków kiedy nasz callback
 * musi zostać uruchomiony na main threadzie do osługi updejtu UI.
 * Jest jeszcez Android Async Http Client - do robienia podstawowych network callsow. Nie jest jednak
 * raczej używana w produkcji. Ma ona jakieś ograniczone sposoby na obserwowanie sciezek networka przydatnych
 * w debuggowaniu. Wymaga także w przypadku fetchowania zdjęć więcej manualnej konfiguracji, a np. OkHttp potrzebuje
 * skorzystać z Picasso czy z Glide, w przeciwienstwie do np. Volleya, który to obsługuje już w sobie.
 * Ważną kwestią jeszcze jest to, ze OkHttp nie jest tylko i wyłącznie standalone library, ale może też
 * być używana dla underling implementacji HttpUrlConnection. W związku z tym np. Volley także może
 * dzwigać OkHttp do wspierania automatycznego Gzip i HTTP/2 procesowania.
 *
 * 1. Przyjrzyjmy się więc, jak wysyłać HTTP Requesty z HttpUrlConnection
 *      1. Deklarujemy polaczenie URL - Ad.1
 *      2. Otwieramy InputStreama do połączenia - Ad.1
 *      3. Pobieramy i dekodujemy na bazie typu danych - Ad.1
 *      4. Wrapujemy w AsyncTaska i wykonujemy w tle. - Ad.2
 */
public class NetworkUtilHardWay {

    /***
     * Ad.1
     */
    public static void exampleOne(){
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL("https://reqres.in/api/users?page=3");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String exampleTwo(){
//        Uri uri = Uri.parse("https://www.google.pl").buildUpon()
//                .appendQueryParameter("q", "dodanyParametr")
//                .build();
        HttpURLConnection connection = null;
        Scanner cs = null;
        try {
            URL url = buildURL("3");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            cs = new Scanner(inputStream);
            boolean hasNext = cs.hasNext();
            if(hasNext){
                return cs.next();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                connection.disconnect();
            }
            if (cs != null){
                cs.close();
            }
        }
        return null;
    }

    public static URL buildURL(String queryParameter){
        Uri uri = Uri.parse("https://reqres.in/api/users").buildUpon()
                .appendQueryParameter("page", queryParameter)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static class NetworkAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... voids) {
            String response = exampleTwo();
            try {
                /***
                 * Przyklad parsowania jsona bez zadnych bibliotek - pokazac to
                 */
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                JSONObject object = jsonArray.getJSONObject(1);
                Log.d("Response json", object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Response", response);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    public static void downloadResponseFromNetwork(){
        new NetworkAsyncTask().execute();
    }

}

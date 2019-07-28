package com.demo.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private TextView textViewWeatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  getSupportActionBar().hide();

        editTextCity = findViewById(R.id.editTextCity);
        textViewWeatherInfo = findViewById(R.id.textViewWeatherInfo);
    }

    public void onClickGetWeather(View view) {

        String appId = "b6907d289e10d714a6e88b30761fae22";
        String url = "https://samples.openweathermap.org/data/2.5/weather?q={CityName}&appid={appId}";

        String city = editTextCity.getText().toString();

        try {
            Integer.parseInt(city);
            url = url.replace("q={CityName}", "id=" + city);
            Log.i("test1", "Index");
        } catch (NumberFormatException e) {
            url = url.replace("{CityName}", city);
            Log.i("test1", "City!");
            e.printStackTrace();
        }

        url = url.replace("{appId}", appId);
        Log.i("test1", url);

        DownloadWeatherContent downloadWeatherContent = new DownloadWeatherContent();
        try {

            String content = downloadWeatherContent.execute(url).get();

            try {
                JSONObject jsonObject = new JSONObject(content);
                String cityName = jsonObject.getString("name");
                JSONObject main = jsonObject.getJSONObject("main");
                String temp = main.getString("temp");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

            //    editTextCity.setText(cityName);

                String weatherInfo = cityName + "\n" + temp + "\n" + description;
                textViewWeatherInfo.setText(weatherInfo);

                Log.i("test1", cityName);
                Log.i("test1", temp);
                Log.i("test1", description);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static class DownloadWeatherContent extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            StringBuilder stringBuilder = new StringBuilder();
            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();
                while (line != null){
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }

                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection!=null){
                    httpURLConnection.disconnect();
                }
            }

            return null;
        }
    }
}

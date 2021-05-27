package com.example.shoptest1;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetCall extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        Request request = new Request.Builder().url(url).build();

        try (Response response = Constants.client.newCall(request).execute()) {
            String resp = response.body().string();
            return resp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
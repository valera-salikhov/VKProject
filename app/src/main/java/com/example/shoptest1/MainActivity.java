package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiManager;
import com.vk.api.sdk.VKMethodCall;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    OkHttpClient client = new OkHttpClient();
    Button buttonLogin;
    Context contextMain = this;
    TextView tw;
    String accessToken = new String();
    String userId;
    private VKScope[] scope = new VKScope[]{VKScope.MARKET, VKScope.GROUPS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tw = findViewById(R.id.textView);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this::onClick);
        //VK.addTokenExpiredHandler(vkTokenExpiredHandler);
        //Log.d("vkTokenExpiredHandler: ", String.valueOf(vkTokenExpiredHandler));

    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                accessToken = vkAccessToken.getAccessToken();
                userId = vkAccessToken.getUserId().toString();
                Log.d("VKAccessToken", accessToken);
                Intent i = new Intent(MainActivity.this, MarketActivity.class);
                i.putExtra("access_token", accessToken);
                i.putExtra("user_id", userId);
                startActivityForResult(i, 1);
            }

            @Override
            public void onLoginFailed(int i) {
                Log.d("Login", "Bad!");
            }
        };
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        /*
        AccessToken accessToken = new AccessToken();
        String accessTokenMarket = new String();
        try {
            accessTokenMarket = accessToken.execute("https://oauth.vk.com/authorize?client_id=7823461&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=134217728&response_type=token").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tw.setText(accessTokenMarket);

         */
        VK.login((Activity) contextMain, Arrays.asList(scope));
        //VKMethodCall call = new VKMethodCall.Builder().method("market.get").build();

       // Log.d("onClick", "OK");
    }


    private class AccessToken extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                String resp = response.body().string();
                int start = resp.indexOf("access_token=");
                int end = resp.indexOf("&", start + 1);
                String accessToken = new StringBuilder(resp).substring(start, end);
                return  accessToken;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    

}
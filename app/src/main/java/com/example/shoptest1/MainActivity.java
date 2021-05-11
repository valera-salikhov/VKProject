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

//import com.google.gson.Gson;
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
    String accessToken = new String();
    String userId;
    private VKScope[] scope = new VKScope[]{VKScope.MARKET, VKScope.GROUPS, VKScope.PHOTOS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this::onClick);
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                accessToken = vkAccessToken.getAccessToken();
                userId = vkAccessToken.getUserId().toString();
                Log.d("VKAccessToken", accessToken);
                String groupsStr = new String();
                //String url = "https://api.vk.com/method/groups.get?v=5.52&access_token="
                //        + accessToken + "&user_id=" + userId + "&extended=1";
                String url = "https://api.vk.com/method/groups.get?v=5.52&access_token="
                        + accessToken + "&user_id=" + userId + "&extended=1&filter=editor";
                GetCall getGroups = new GetCall();
                try {
                    groupsStr = getGroups.execute(url).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(MainActivity.this, GroupsEditor.class);
                i.putExtra("access_token", accessToken);
                i.putExtra("user_id", userId);
                i.putExtra("getGroupsHttps", groupsStr);
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
        VK.login((Activity) contextMain, Arrays.asList(scope));
    }

}
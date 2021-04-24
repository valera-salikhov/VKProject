package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button buttonLogin;
    Context contextMain = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this::onClick);
        VK.addTokenExpiredHandler(vkTokenExpiredHandler);
    }

    VKTokenExpiredHandler vkTokenExpiredHandler = new VKTokenExpiredHandler() {
        @Override
        public void onTokenExpired() {
            Log.d("Token", "brrrrrrrrr");
        }
    };

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                Log.d("Login", "Excellent!!!");
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
        VK.login((Activity) contextMain, Collections.singleton(VKScope.WALL));
        Log.d("onClick", "OK");
    }
}
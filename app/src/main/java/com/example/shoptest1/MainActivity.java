package com.example.shoptest1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button buttonLogin;
    TextView somethingWrongTryAgain;
    Context contextMain = this;
    String accessToken = new String();
    String userId;
    private VKScope[] scope = new VKScope[]{VKScope.MARKET, VKScope.GROUPS, VKScope.PHOTOS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        VK.login((Activity) contextMain, Arrays.asList(scope));
        somethingWrongTryAgain = findViewById(R.id.somethingWrongTryAgain);
        somethingWrongTryAgain.setVisibility(View.INVISIBLE);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this::onClick);

    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                somethingWrongTryAgain.setVisibility(View.INVISIBLE);
                accessToken = vkAccessToken.getAccessToken();
                userId = vkAccessToken.getUserId().toString();
                String groupsStr = new String();
                String url = "https://api.vk.com/method/groups.get?v=5.131&access_token="
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
                somethingWrongTryAgain.setVisibility(View.VISIBLE);
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
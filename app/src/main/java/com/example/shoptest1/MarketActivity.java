package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.requests.VKRequest;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarketActivity extends AppCompatActivity implements View.OnClickListener {

    Button createNewProduct;
    String  access_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        createNewProduct = findViewById(R.id.createNewProduct);
        Log.d("WE HERE", "YES");
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    OkHttpClient client = new OkHttpClient.Builder().build();

                    Request.Builder request = new Request.Builder();
                    String CLIENT_ID = 	"7823461";                      // "Цифры тут из настроек приложения в ВК"
                    String CLIENT_SECRET = "Uoat5YpwpmRPP0FvfSa4";                       //"Секретный код из настроек приложения в ВК"
                    request.url("https://oauth.vk.com/access_token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=5.50&grant_type=client_credentials");

                    Response response = client.newCall(request.build()).execute();

                    String answerHttp = response.body().string();
                    gettingAccessToken(answerHttp, access_token);
                    Log.d("LOG", access_token);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        createNewProduct.setOnClickListener(this::onClick);
    }


    @Override
    public void onClick(View v) {
        
    }

    private void gettingAccessToken(String answerHttp, String access_token) {   // не забыть закинуть в sharedpreferences!!!
        int count_quotes = 0;       // quotes - кавычки
        int firstCharAccessToken = 0;
        for (int i = 0; i < answerHttp.length(); i++) {
            if (answerHttp.charAt(i) == '\"') {
                count_quotes++;
            }
            if (count_quotes == 3) {
                firstCharAccessToken = i + 1;
                break;
            }
        }
        int indexNow = firstCharAccessToken;
        while (answerHttp.charAt(indexNow) != '\"') {
            access_token += answerHttp.charAt(indexNow);
            indexNow++;
        }
    }

}

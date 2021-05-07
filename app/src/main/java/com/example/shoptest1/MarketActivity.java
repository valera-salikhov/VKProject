package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MarketActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    Button crNewProduct, getAllProducts, getGroups;
    String accessToken = new String();
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        accessToken = getIntent().getStringExtra("access_token");
        userId = Integer.parseInt(getIntent().getStringExtra("user_id"));

        crNewProduct = findViewById(R.id.CreateNewProduct);
        getAllProducts = findViewById(R.id.getAllProducts);
        getGroups = findViewById(R.id.getGroups);

        getGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.vk.com/method/groups.get?v=5.52&access_token="
                        + accessToken + "&user_id=" + userId + "&extended=1";
                //String url = "https://api.vk.com/method/groups.get?v=5.52&access_token="
                //        + accessToken + "&user_id=" + userId + "&extended=1&filter=editor"
                GetCall getGroups = new GetCall();
                try {
                    String gr = getGroups.execute(url).get();
                   // Log.d("GetGroups", gr);
                    Intent intent = new Intent(MarketActivity.this, GroupsEditor.class);
                    intent.putExtra("getGroupsHttps", gr);
                    intent.putExtra("access_token", accessToken);
                    startActivityForResult(intent, 2);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        //crNewProduct.setOnClickListener(this::onClick);
        //getAllProducts.setOnClickListener(this::onClick);

    }


/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.getAllProducts):

                break;
        }
    }

 */

    private class GetCall extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                String resp = response.body().string();
                return resp;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

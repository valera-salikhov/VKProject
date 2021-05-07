package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GroupMarket extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    int groupId, countProducts;
    ListView listView;
    String accessToken = new String();
    String productsStr = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_market);
        groupId = Integer.parseInt(getIntent().getStringExtra("group_id"));
        listView = findViewById(R.id.listView);
        accessToken = getIntent().getStringExtra("access_token");

        String url = "https://api.vk.com/method/market.get?v=5.52&access_token=" +
                accessToken + "&owner_id=-" + groupId;
        GetCall getProducts = new GetCall();
        try {
            productsStr = getProducts.execute(url).get();
            countProducts = getCountProducts();
            //Product product1 = new Product();
            //Product.toProducts(product1, productsStr);
            //Log.d("NEWPRODUCT", product1.description);
            //Log.d("PRODUCT", productsStr);
            //Log.d("PRODUCTCOUNT", String.valueOf(countProducts));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getCountProducts() {
        int start = productsStr.indexOf("count\":");
        int end = productsStr.indexOf(",", start + 1);
        String s = new StringBuilder(productsStr).substring(start + 7, end);
        int count = Integer.parseInt(s);
        return count;
    }

    private class GetCall extends AsyncTask<String, Integer, String> {

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
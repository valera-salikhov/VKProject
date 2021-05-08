package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button addProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_market);
        groupId = Integer.parseInt(getIntent().getStringExtra("group_id"));
        listView = findViewById(R.id.listView);
        accessToken = getIntent().getStringExtra("access_token");
        addProduct = findViewById(R.id.addProduct);

        String url = "https://api.vk.com/method/market.get?v=5.52&access_token=" +
                accessToken + "&owner_id=-" + groupId;
        GetCall getProducts = new GetCall();
        try {
            productsStr = getProducts.execute(url).get();
            countProducts = getCountProducts();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Product[] products = new Product[countProducts];
        for (int i = 0; i < countProducts; i++) {
            products[i] = new Product();
        }
        String[] items = new String[countProducts];
        getListGroups(items, products);
        String[] titles = new String[countProducts];
        String[] id = new String[countProducts];
        for (int i = 0; i < countProducts; i++) {
            titles[i] = products[i].title;
            id[i] = String.valueOf(products[i].id);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GroupMarket.this,
                android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupMarket.this, CreateProduct.class);
                i.putExtra("group_id", groupId);
                i.putExtra("access_token", accessToken);
                startActivityForResult(i, 1);
            }
        });

    }

    private int getCountProducts() {
        int start = productsStr.indexOf("count") + 7;
        int end = productsStr.indexOf(",", start + 1);
        String s = new StringBuilder(productsStr).substring(start, end);
        int count = Integer.parseInt(s);
        return count;
    }

    private void getListGroups(String[] items, Product[] products) {
        int start = productsStr.indexOf("{", productsStr.indexOf("items")) + 1;
        for (int i = 0; i < countProducts; i++) {
            int startOfEnd = productsStr.indexOf("cart_quantity", start);
            int end = productsStr.indexOf("}", startOfEnd);
            items[i] = new StringBuilder(productsStr).substring(start, end);
            start = end + 3;
            //Log.d("ITEM" + i, items[i]);
        }

        for (int i = 0; i < countProducts; i++) {
            // Log.d("ITEMS" + i, items[i]);
            Product.toProducts(products[i], items[i]);
        }

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
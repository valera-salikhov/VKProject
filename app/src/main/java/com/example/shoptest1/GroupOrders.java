package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class GroupOrders extends AppCompatActivity {

    String accessToken = new String();
    String groupId = new String();
    String ordersStr = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_orders);
        accessToken = getIntent().getStringExtra("access_token");
        groupId = getIntent().getStringExtra("group_id");
        getOrdersCall();
    }


    private void getOrdersCall() {
        String url = "https://api.vk.com/method/market.getGroupOrders?v=5.52&access_token=" +
                accessToken + "&group_id=" + groupId;
        GetCall getCall = new GetCall();
        try {
            ordersStr = getCall.execute(url).get();
            Log.d("ORDERSTR", ordersStr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
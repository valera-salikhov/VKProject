package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

public class GroupOrders extends AppCompatActivity {
    ListView listViewOrders;
    String accessToken = new String();
    String groupId = new String();
    String ordersStr = new String();
    int countOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_orders);

        listViewOrders = findViewById(R.id.listViewOrders);

        accessToken = getIntent().getStringExtra("access_token");
        groupId = getIntent().getStringExtra("group_id");

        getOrdersCall();
        countOrders = getCountOrders();

        Orders[] orders = new Orders[countOrders];
        String[] items = new String[countOrders];
        for (int i = 0; i < countOrders; i++) {
            orders[i] = new Orders();
            items[i] = new String();
        }

        getListOrders(items, orders);


    }

    private int getCountOrders() {
        int start = ordersStr.indexOf("count") + 7;
        int end = ordersStr.indexOf(",", start + 1);
        String count = new StringBuilder(ordersStr).substring(start, end);
        return Integer.parseInt(count);
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


    private void getListOrders(String[] items, Orders[] orders) {
        int start = ordersStr.indexOf("{", ordersStr.indexOf("items")) + 1;
        for (int i = 0; i < countOrders; i++) {
            int indexPreStartOfEnd = ordersStr.indexOf("seller", start);
            int startOfTheEnd = ordersStr.indexOf("group_id", indexPreStartOfEnd);
            int end = ordersStr.indexOf("}}", startOfTheEnd) + 1;
            items[i] = new StringBuilder(ordersStr).substring(start, end);
            start = end + 3;
            //Log.d("ITEM" + i, items[i]);
        }

        for (int i = 0; i < countOrders; i++) {
            // Log.d("ITEMS" + i, items[i]);
            Orders.toOrder(orders[i], items[i]);
        }
    }
}
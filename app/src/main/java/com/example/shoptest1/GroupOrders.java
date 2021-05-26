package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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



        String[] customersNames = new String[countOrders];
        String[] orderPrice = new String[countOrders];
        String[] orderStatus = new String[countOrders];
        ImageView[] orderPhoto = new ImageView[countOrders];
        //int[] orderPhotoAdapter = new int[countOrders];
        for (int i = 0; i < countOrders; i++) {
            customersNames[i] = getCustomerName(orders[i].userId);
            orderPrice[i] = orders[i].totalPriceText;
            orderStatus[i] = getStatusName(orders[i].status);
            if (!orders[i].thumbPhoto.equals("")) {
                //orderPhoto[i] = findViewById(R.id.imageView);
                //Picasso.with(this).load(orders[i].thumbPhoto).into(orderPhoto[i]);
                //orderPhotoAdapter[i] = orderPhoto[i].getId();
                //Log.d("PHOTO", String.valueOf(orderPhotoAdapter[i]));
            }
            //orderPhoto[i] = Integer.parseInt(orders[i].thumbPhoto);
            //Log.d("PHOTO", String.valueOf(orderPhoto[i]));
        }
        
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(countOrders);
        Map<String, Object> map;
        for (int i = 0; i < countOrders; i++) {
            map = new HashMap<String, Object>();
            map.put("customerName", customersNames[i]);
            map.put("orderPrice", orderPrice[i]);
            map.put("orderStatus", orderStatus[i]);
            //map.put("orderPhoto", orderPhotoAdapter[i]);
            data.add(map);
        }

        String[] from = {"customerName", "orderPrice", "orderStatus", "orderPhoto"};
        int[] to = {R.id.customerOrder, R.id.orderPrice, R.id.statusOrder, R.id.orderPhoto};

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.custom_listview, from, to);
        listViewOrders.setAdapter(adapter);

        listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupOrders.this, OrdersInfo.class);
                intent.putExtra("access_token", accessToken);
                intent.putExtra("group_id", groupId);
                intent.putExtra("displayOrderId", orders[position].displayOrderId);
                intent.putExtra("orderPrice", orders[position].totalPriceText);
                intent.putExtra("customerName", getCustomerName(orders[position].userId));
                intent.putExtra("orderAddress", orders[position].address);
                //intent.putExtra("customerTelephoneNumber", orders[i].)
                intent.putExtra("orderStatus", orders[position].status);
                startActivity(intent);
            }
        });

    }

    private int getCountOrders() {
        int start = ordersStr.indexOf("count") + 7;
        int end = ordersStr.indexOf(",", start + 1);
        String count = new StringBuilder(ordersStr).substring(start, end);
        return Integer.parseInt(count);
    }

    private void getOrdersCall() {
        String url = "https://api.vk.com/method/market.getGroupOrders?v=5.131&access_token=" +
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

    private String getCustomerName(String userId) {
        String url = "https://api.vk.com/method/users.get?v=5.131&access_token=" + accessToken +
                "&user_ids=" + userId;
        GetCall getCall = new GetCall();
        try {
            String response = getCall.execute(url).get();
            String firstName = new StringBuilder(response).substring(response.indexOf("first_name") + 13,
                    response.indexOf("\"", response.indexOf("first_name") + 13));
            String lastName = new StringBuilder(response).substring(response.indexOf("last_name") + 12,
                    response.indexOf("\"", response.indexOf("last_name") + 12));
            return firstName + " " + lastName;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStatusName(String status) {
        switch (status) {
            case ("0"):
                return "Новый";
            case ("1"):
                return "Согласуется";
            case ("2"):
                return "Собирается";
            case ("3"):
                return "Доставляется";
            case ("4"):
                return "Выполнен";
            case ("5"):
                return "Отменен";
            case ("6"):
                return "Возвращен";
        }
        return null;
    }

    private String createRightUrl(String url) {
        String urlNew = new String();
        for (int i = 0; i < url.length(); i++) {
            if (url.charAt(i) == '\\') {
                continue;
            }
            urlNew += url.charAt(i);
        }
        return urlNew;
    }
}
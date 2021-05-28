package com.example.shoptest1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
        String[] productOrder = new String[countOrders];
        String[] groupOrder = new String[countOrders];
        String[] orderPhoto = new String[countOrders];

        for (int i = 0; i < countOrders; i++) {
            customersNames[i] = getCustomerName(orders[i].userId);
            orderPrice[i] = orders[i].totalPriceText;
            orderStatus[i] = getStatusName(orders[i].status);
            productOrder[i] = orders[i].title;
            groupOrder[i] = orders[i].sellerName;
            orderPhoto[i] = orders[i].thumbPhoto;
        }
        
        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(countOrders);
        Map<String, Object> map;
        for (int i = 0; i < countOrders; i++) {
            map = new HashMap<String, Object>();
            map.put("customerName", customersNames[i]);
            map.put("orderPrice", orderPrice[i]);
            map.put("orderStatus", orderStatus[i]);
            map.put("productOrder", productOrder[i]);
            map.put("groupOrder", groupOrder[i]);
            data.add(map);
        }

        String[] from = {"customerName", "orderPrice", "orderStatus", "productOrder", "groupOrder"};
        int[] to = {R.id.customerOrder, R.id.orderPrice, R.id.statusOrder, R.id.productOrder, R.id.groupOrder};

        CustomAdapter adapter = new CustomAdapter(this, data, R.layout.custom_listview,
                from, to, orderPhoto);
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
                intent.putExtra("orderStatus", orders[position].status);
                intent.putExtra("productNameInOrder", orders[position].title);
                intent.putExtra("sellerNameOrder", orders[position].sellerName);
                intent.putExtra("quantity", orders[position].quantity);
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
        Log.d("ITEM63125", ordersStr);
        for (int i = 0; i < countOrders; i++) {
            int indexPreStartOfEnd = ordersStr.indexOf("seller", start);
            int startOfTheEnd = ordersStr.indexOf("group_id", indexPreStartOfEnd);
            int end = ordersStr.indexOf("}}", startOfTheEnd) + 1;
            items[i] = new StringBuilder(ordersStr).substring(start, end);
            start = end + 3;
        }

        for (int i = 0; i < countOrders; i++) {
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
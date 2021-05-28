package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class OrdersInfo extends AppCompatActivity {

    Spinner spinnerOrderStatus;
    TextView idOfOrder, orderPrice, orderAddress, customerName, orderStatus, productNameInOrder,
            sellerNameOrder, countProductsInOrder;
    Button acceptСhanges;
    String fullIdOrder = new String();
    String accessToken = new String();
    String groupId = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_info);
        accessToken = getIntent().getStringExtra("access_token");
        groupId = getIntent().getStringExtra("group_id");
        fullIdOrder = getIntent().getStringExtra("displayOrderId");

        acceptСhanges = findViewById(R.id.acceptСhanges);
        spinnerOrderStatus = findViewById(R.id.spinneOrderStatus);
        idOfOrder = findViewById(R.id.idOfOrder);
        orderPrice = findViewById(R.id.orderPrice);
        orderAddress = findViewById(R.id.orderAddress);
        customerName = findViewById(R.id.customerName);
        orderStatus = findViewById(R.id.orderStatus);
        productNameInOrder = findViewById(R.id.productNameInOrder);
        sellerNameOrder = findViewById(R.id.sellerNameOrder);
        countProductsInOrder = findViewById(R.id.countProductsInOrder);

        idOfOrder.setText(fullIdOrder);
        orderPrice.setText(getIntent().getStringExtra("orderPrice"));
        orderAddress.setText(getIntent().getStringExtra("orderAddress"));
        customerName.setText(getIntent().getStringExtra("customerName"));
        orderStatus.setText(fromOrderStatus(getIntent().getStringExtra("orderStatus")));
        productNameInOrder.setText(getIntent().getStringExtra("productNameInOrder"));
        sellerNameOrder.setText(getIntent().getStringExtra("sellerNameOrder"));
        countProductsInOrder.setText(getIntent().getStringExtra("quantity"));

        acceptСhanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = new StringBuilder(fullIdOrder).substring(0, fullIdOrder.indexOf("-"));
                String orderId = new StringBuilder(fullIdOrder).substring(fullIdOrder.indexOf("-") + 1,
                        fullIdOrder.length());
                String url = "https://api.vk.com/method/market.editOrder?v=5.131&access_token=" +
                        accessToken + "&user_id=" + userId + "&order_id=" + orderId + "&status=" +
                        getOrderStatus(spinnerOrderStatus);
                GetCall getCall = new GetCall();
                try {
                    String resp = getCall.execute(url).get();
                    Log.d("OLOLOLOLO", resp);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(OrdersInfo.this, GroupOrders.class);
                startActivity(intent);
            }
        });
    }

    private String getOrderStatus(Spinner spinnerOrderStatus) {
        switch (spinnerOrderStatus.getSelectedItem().toString()) {
            case ("Новый"):
                return "0";
            case ("Согласуется"):
                return "1";
            case ("Собирается"):
                return "2";
            case ("Доставляется"):
                return "3";
            case ("Выполнен"):
                return "4";
            case ("Отменен"):
                return "5";
            case ("Возвращен"):
                return "6";
        }
        return null;
    }

    private String fromOrderStatus(String status) {
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
}
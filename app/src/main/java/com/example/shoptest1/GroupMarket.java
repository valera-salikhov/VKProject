package com.example.shoptest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Person;
import android.content.Context;
import android.content.DialogInterface;
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

    int countProducts;
    String groupId = new String();
    ListView listView;
    String accessToken = new String();
    String productsStr = new String();
    Button addProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_market);
        groupId = getIntent().getStringExtra("group_id");
        listView = findViewById(R.id.listView);
        accessToken = getIntent().getStringExtra("access_token");
        addProduct = findViewById(R.id.addProduct);

        getProductsCall();

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
                FragmentManager manager = getSupportFragmentManager();
                GroupMarket.DialogFragmentProductsEditor dialogFragmentGroupsEditor = new GroupMarket.DialogFragmentProductsEditor(GroupMarket.this,
                        accessToken, products[position], manager, groupId);
                dialogFragmentGroupsEditor.show(manager, "dialog");
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupMarket.this, CreateProduct.class);
                intent.putExtra("group_id", groupId);
                intent.putExtra("access_token", accessToken);
                startActivity(intent);
            }
        });
    }


    public static class DialogFragmentProductsEditor extends androidx.fragment.app.DialogFragment {

        Context context;
        String accessToken;
        Product product;
        private FragmentManager manager;
        String groupId;

        public DialogFragmentProductsEditor(Context context, String accessToken, Product product,
                                            FragmentManager manager, String groupId) {
            this.context = context;
            this.accessToken = accessToken;
            this.product = product;
            this.manager = manager;
            this.groupId = groupId;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Что хотите сделать с этим товаром?";
            String buttonProductsStr = "Изменить";
            String buttonOrdersStr = "Удалить";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setPositiveButton(buttonProductsStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, EditProduct.class);
                    intent.putExtra("access_token", accessToken);
                    intent.putExtra("group_id", groupId);
                    intent.putExtra("productName", product.title);
                    intent.putExtra("description", product.description);
                    intent.putExtra("price", product.priceAmount);
                    intent.putExtra("dimension_width", product.dimensions_width);        // trying to see
                    intent.putExtra("dimensions_height", product.dimensions_height);     // trying to see
                    intent.putExtra("dimensions_length", product.dimensions_length);     // trying to see
                    intent.putExtra("weight", product.weight);                           // trying to see
                    intent.putExtra("sku", product.sku);                                 // trying to see
                    intent.putExtra("item_id", String.valueOf(product.id));
                    Log.d("PRODUCTID", String.valueOf(product.id));
                    startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(buttonOrdersStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GroupMarket.DialogYesOrNo DialogYesOrNo = new GroupMarket.DialogYesOrNo(context,
                            accessToken, product, groupId);
                    DialogYesOrNo.show(manager, "dialog");
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            return builder.create();
        }
    }

    public static class DialogYesOrNo extends androidx.fragment.app.DialogFragment {

        Context context;
        String accessToken;
        Product product;
        String groupId;

        public DialogYesOrNo(Context context, String accessToken, Product product, String groupId) {
            this.context = context;
            this.accessToken = accessToken;
            this.product = product;
            this.groupId = groupId;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Уверены, что хотите удалить товар?";
            String buttonProductsStr = "Да";
            String buttonOrdersStr = "Нет";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setPositiveButton(buttonProductsStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String url = "https://api.vk.com/method/market.delete?v=5.131&access_token=" +
                            accessToken + "&owner_id=-" + groupId + "&item_id=" + product.id;
                    GetCall getCall = new GetCall();
                    try {
                        String response = getCall.execute(url).get();
                        Log.d("ONDELETE", response);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton(buttonOrdersStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            return builder.create();
        }
    }


    private void getProductsCall() {
        String url = "https://api.vk.com/method/market.get?v=5.131&access_token=" +
                accessToken + "&owner_id=-" + groupId;
        GetCall getProducts = new GetCall();
        try {
            productsStr = getProducts.execute(url).get();
            Log.d("PRODUCTSTR", productsStr);
            countProducts = getCountProducts();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}
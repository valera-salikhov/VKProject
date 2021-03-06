package com.example.shoptest1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

        String[] price = new String[countProducts];
        String[] urlPhoto = new String[countProducts];
        String[] productCategory = new String[countProducts];

        for (int i = 0; i < countProducts; i++) {
            titles[i] = products[i].title;
            price[i] = products[i].priceText;
            urlPhoto[i] = products[i].thumbPhoto;
            productCategory[i] = products[i].categoryName;
            id[i] = String.valueOf(products[i].id);
        }

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>(countProducts);
        Map<String, Object> map;
        for (int i = 0; i < countProducts; i++) {
            map = new HashMap<String, Object>();
            map.put("titles", titles[i]);
            map.put("price", price[i]);
            map.put("productCategory", productCategory[i]);
            data.add(map);
        }

        String[] from = {"titles", "price", "productCategory"};
        int[] to = {R.id.productProduct, R.id.productPrice, R.id.productCategory};

        CustomAdapterForProducts customAdapterForProducts = new CustomAdapterForProducts(GroupMarket.this,
                data, R.layout.cuistom_textview_for_products, from, to, urlPhoto);

        listView.setAdapter(customAdapterForProducts);
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
            String title = "?????? ???????????? ?????????????? ?? ???????? ???????????????";
            String buttonProductsStr = "????????????????";
            String buttonOrdersStr = "??????????????";

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
                    intent.putExtra("price", product.priceText);
                    intent.putExtra("dimension_width", product.dimensions_width);        // trying to see
                    intent.putExtra("dimensions_height", product.dimensions_height);     // trying to see
                    intent.putExtra("dimensions_length", product.dimensions_length);     // trying to see
                    intent.putExtra("weight", product.weight);                           // trying to see
                    intent.putExtra("sku", product.sku);                                 // trying to see
                    intent.putExtra("item_id", String.valueOf(product.id));
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
            String title = "??????????????, ?????? ???????????? ?????????????? ???????????";
            String buttonProductsStr = "????";
            String buttonOrdersStr = "??????";

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
        }

        for (int i = 0; i < countProducts; i++) {
            Product.toProducts(products[i], items[i]);
        }
    }
}
package com.example.shoptest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.function.IntToDoubleFunction;

public class GroupsEditor extends AppCompatActivity {

    ListView listView;

    String groupsStr = new String();
    int countGroups;
    String accessToken = new String();
    String dialogButtonChose = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_editor);
        accessToken = getIntent().getStringExtra("access_token");
        listView = findViewById(R.id.listView);

        groupsStr = getIntent().getStringExtra("getGroupsHttps");
        countGroups = getCountGroups();
        Group[] groups = new Group[countGroups];
        for (int i = 0; i < countGroups; i++) {
            groups[i] = new Group();
        }
        //Log.d("GetGroups!", String.valueOf(countGroups));
        String[] groupNames = new String[countGroups];
        String[] groupId = new String[countGroups];
        String[] items = new String[countGroups];
        getListGroups(items, groups);

        for (int i = 0; i < countGroups; i++) {
            groupId[i] = String.valueOf(groups[i].id);
            groupNames[i] = groups[i].name;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GroupsEditor.this,
                android.R.layout.simple_list_item_1, groupNames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getSupportFragmentManager();
                DialogFragmentGroupsEditor dialogFragmentGroupsEditor = new DialogFragmentGroupsEditor(GroupsEditor.this,
                        accessToken, groupId[position]);
                dialogFragmentGroupsEditor.show(manager, "dialog");
            }
        });
    }

    private int getCountGroups() {
        int start = groupsStr.indexOf("count\":");
        int end = groupsStr.indexOf(",", start + 1);
        String s = new StringBuilder(groupsStr).substring(start + 7, end);
        int count = Integer.parseInt(s);
        return count;
    }

    private void getListGroups(String[] items, Group[] groups) {
        int start = groupsStr.indexOf("[{");
        start += 2;
        for (int i = 0; i < countGroups; i++) {
            int end = groupsStr.indexOf("}", start);
            items[i] = new StringBuilder(groupsStr).substring(start, end);
            start = end + 3;
        }
        //Log.d("ITEM0", groupsStr);
        for (int i = 0; i < countGroups; i++) {
           // Log.d("ITEMS" + i, items[i]);
           Group.toGroup(groups[i], items[i]);
        }

    }

    public static class DialogFragmentGroupsEditor extends androidx.fragment.app.DialogFragment {

        Context context;
        String accessToken = new String();
        String groupId = new String();

        public DialogFragmentGroupsEditor(Context context, String accessToken, String groupId) {
            this.context = context;
            this.accessToken = accessToken;
            this.groupId = groupId;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Что хотите посмотреть?";
            String buttonProductsStr = "Товары";
            String buttonOrdersStr = "Заказы";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setPositiveButton(buttonProductsStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //((GroupsEditor)getActivity()).dialogButtonChose = buttonProductsStr;
                    Intent intent = new Intent(context, GroupMarket.class);
                    intent.putExtra("access_token", accessToken);
                    intent.putExtra("group_id", groupId);
                    startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(buttonOrdersStr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //((GroupsEditor)getActivity()).dialogButtonChose = buttonOrdersStr;
                    Intent intent = new Intent(context, GroupOrders.class);
                    intent.putExtra("access_token", accessToken);
                    intent.putExtra("group_id", groupId);
                    startActivityForResult(intent, 1);
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            return builder.create();
        }



    }

    private void getActivityProductsOrOrders(String accessToken, String listClickedGroupId, String dialogButtonChose) {
        switch (dialogButtonChose) {
            case ("Товары"):
                Intent intent = new Intent(GroupsEditor.this, GroupMarket.class);
                intent.putExtra("group_id", listClickedGroupId);
                intent.putExtra("access_token", accessToken);
                startActivityForResult(intent, 1);
                break;
            case ("Заказы"):
                Intent intent1 = new Intent(GroupsEditor.this, GroupOrders.class);
                intent1.putExtra("group_id", listClickedGroupId);
                intent1.putExtra("access_token", accessToken);
                startActivityForResult(intent1, 1);
        }
    }
}
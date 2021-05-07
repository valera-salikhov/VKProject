package com.example.shoptest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class GroupsEditor extends AppCompatActivity {
    Button backButton;
    ListView listView;
    String groupsStr = new String();
    int countGroups;
    String accessToken = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_editor);
        accessToken = getIntent().getStringExtra("access_token");
        listView = findViewById(R.id.listView);
        //backButton = findViewById(R.id.backButton);
        groupsStr = getIntent().getStringExtra("getGroupsHttps");
        countGroups = getCountGroups();
        //Log.d("GetGroups!", String.valueOf(countGroups));
        String[] groupNames = new String[countGroups];
        String[] groupId = new String[countGroups];
        String[] items = new String[countGroups];
        getListGroups(items, groupNames, groupId);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GroupsEditor.this,
                android.R.layout.simple_list_item_1, groupNames);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupsEditor.this, GroupMarket.class);      // Создать отдельный класс для параметров групп
                intent.putExtra("group_id", groupId[position]);
                intent.putExtra("access_token", accessToken);
                startActivityForResult(intent, 3);
            }
        });
/*
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

 */
    }

    private int getCountGroups() {
        int start = groupsStr.indexOf("count\":");
        int end = groupsStr.indexOf(",", start + 1);
        String s = new StringBuilder(groupsStr).substring(start + 7, end);
        int count = Integer.parseInt(s);
        return count;
    }

    private void getListGroups(String[] items, String[] groupNames, String[] groupId) {
        int start = groupsStr.indexOf("[{");
        start += 2;
        for (int i = 0; i < countGroups; i++) {
            int end = groupsStr.indexOf("}", start);
            items[i] = new StringBuilder(groupsStr).substring(start, end);
            start = end + 3;
        }
        Log.d("ITEM0", groupsStr);
        for (int i = 0; i < countGroups; i++) {
            //Log.d("ITEMS" + i, items[i]);
            int startItemId = items[i].indexOf("id");
            int endItemId = items[i].indexOf(",", startItemId);
            groupId[i] = new StringBuilder(items[i]).substring(startItemId + 4, endItemId);
            int startItemName = items[i].indexOf("name", endItemId + 1);
            int endItemName = items[i].indexOf("\",", startItemName + 1);
            groupNames[i] = new StringBuilder(items[i]).substring(startItemName + 7, endItemName);
            //Log.d("ITEM" + i, "name: " + groupNames[i] + " id: " + groupId[i]);
        }

    }
}
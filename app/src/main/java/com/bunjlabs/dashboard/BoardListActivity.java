package com.bunjlabs.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bunjlabs.dashboard.model.Item;
import com.bunjlabs.dashboard.model.ItemAdapter;
import com.bunjlabs.dashboard.model.ItemStorage;

import java.util.ArrayList;
import java.util.List;

public class BoardListActivity extends AppCompatActivity {

    private ItemAdapter adapter;

    static final int NEW_ITEM_REQUEST = 1;
    static final int ITEM_VIEW_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        FloatingActionButton addBtn = (FloatingActionButton) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemEditActivity.class);
                startActivityForResult(intent, NEW_ITEM_REQUEST);
            }
        });

        ListView listView = (ListView) findViewById(R.id.dashboardListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ItemViewActivity.class);
                intent.putExtra("item", adapter.getItem(position));
                startActivityForResult(intent, ITEM_VIEW_REQUEST);
            }
        });

        List<Item> items = new ArrayList<>();
        Cursor cursor = ItemStorage.getInstance(getApplicationContext()).getAll();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            items.add(new Item(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getLong(2),
                    cursor.getString(3)
            ));
        }

        adapter = new ItemAdapter(this, items);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_ITEM_REQUEST && resultCode == Activity.RESULT_OK) {
            Item item = (Item) data.getSerializableExtra("item");
            ItemStorage.getInstance(getApplicationContext()).add(item);
        }
    }

    @Override
    protected void onRestart() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = ItemStorage.getInstance(getApplicationContext()).getAll();

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            items.add(new Item(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getLong(2),
                    cursor.getString(3)
            ));
        }

        adapter.clear();
        adapter.addAll(items);

        super.onRestart();
    }


}

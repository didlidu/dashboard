package com.bunjlabs.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bunjlabs.dashboard.model.Item;
import com.bunjlabs.dashboard.model.ItemStorage;

public class ItemViewActivity extends AppCompatActivity {

    private Item item;

    static final int EDIT_ITEM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        Intent myIntent = getIntent();
        this.item = (Item) myIntent.getSerializableExtra("item");

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.itemEditBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemEditActivity.class);
                intent.putExtra("item", item);
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
        });

        ((TextView) findViewById(R.id.itemNameLabel)).setText(item.name);
        ((TextView) findViewById(R.id.itemPriceLabel)).setText(String.format("%d.%02d", item.price / 100, item.price % 100));
        ((TextView) findViewById(R.id.itemContentLabel)).setText(item.content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Item item = (Item) data.getSerializableExtra("item");

                // Item was deleted - return to BoardListView
                if (item == null) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    return;
                }

                ItemStorage.getInstance(getApplicationContext()).update(item);

                ((TextView) findViewById(R.id.itemNameLabel)).setText(item.name);
                ((TextView) findViewById(R.id.itemPriceLabel)).setText(String.format("%d.%02d", item.price / 100, item.price % 100));
                ((TextView) findViewById(R.id.itemContentLabel)).setText(item.content);
                this.item = item;
            }
        }
    }

}

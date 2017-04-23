package com.bunjlabs.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bunjlabs.dashboard.model.Item;
import com.bunjlabs.dashboard.model.ItemStorage;

public class ItemEditActivity extends AppCompatActivity {

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        Intent myIntent = getIntent();
        this.item = (Item) myIntent.getSerializableExtra("item");

        FloatingActionButton btnSave = (FloatingActionButton) findViewById(R.id.itemSaveBtn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.itemNameField)).getText().toString();
                String price = ((EditText) findViewById(R.id.itemPriceField)).getText().toString();
                String content = ((EditText) findViewById(R.id.itemContentField)).getText().toString();

                if (name.isEmpty() || price.isEmpty()) {
                    Toast.makeText(ItemEditActivity.this, "Поля название и цена должны быть заполнены", Toast.LENGTH_SHORT).show();
                    return;
                } else if (price.split("\\.")[0].length() > 16) {
                    Toast.makeText(ItemEditActivity.this, "Поле цена содержит слишком большое значение", Toast.LENGTH_SHORT).show();
                    return;
                }

                item.name = name;
                item.price = Long.parseLong(price.split("\\.")[0]) * 100;

                if (price.split("\\.").length > 1 && !price.split("\\.")[1].substring(0, 2).isEmpty()) {
                    item.price += (price.split("\\.")[1].substring(0, 2).length() == 1) ? Long.parseLong(price.split("\\.")[1].substring(0, 2)) * 10 : Long.parseLong(price.split("\\.")[1].substring(0, 2));
                }

                item.content = content;

                Intent returnIntent = new Intent();
                returnIntent.putExtra("item", item);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        FloatingActionButton btnDelete = (FloatingActionButton) findViewById(R.id.itemDeleteBtn);

        if (item != null) {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemStorage.getInstance(getApplicationContext()).delete(item);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("item", (Item) null);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            });

            btnDelete.setVisibility(View.VISIBLE);

            ((EditText) findViewById(R.id.itemNameField)).setText(item.name);
            ((EditText) findViewById(R.id.itemPriceField)).setText(String.format("%d.%02d", item.price / 100, item.price % 100));
            ((EditText) findViewById(R.id.itemContentField)).setText(item.content);
        } else {
            btnDelete.setVisibility(View.INVISIBLE);
            this.item = new Item();
        }
    }
}

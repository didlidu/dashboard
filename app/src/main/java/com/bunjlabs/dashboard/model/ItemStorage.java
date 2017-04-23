package com.bunjlabs.dashboard.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ItemStorage {

    private Map<Long, Item> map;
    private SQLiteOpenHelper _openHelper;

    private static ItemStorage instance = null;

    private ItemStorage(Context context) {
        map = new HashMap<>();
        _openHelper = new ItemStorageDbOpenHelper(context);
    }

    public static ItemStorage getInstance(Context context) {
        return instance == null ? instance = new ItemStorage(context) : instance;
    }

    public Cursor getAll() {
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }

        return db.rawQuery("SELECT id, name, price, content FROM items ORDER BY creationDate DESC", null);
    }

    public long add(Item item) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        if (db == null) {
            return -1;
        }

        ContentValues row = new ContentValues();
        row.put("name", item.name);
        row.put("price", item.price);
        row.put("content", item.content);
        long id = db.insert("items", null, row);
        db.close();

        item.id = id;

        return id;
    }

    public void delete(Item item) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        if (db == null) {
            return;
        }

        db.delete("items", "id = ?", new String[] { String.valueOf(item.id) });
        db.close();
    }

    public void update(Item item) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        if (db == null) {
            return;
        }

        ContentValues row = new ContentValues();
        row.put("name", item.name);
        row.put("price", item.price);
        row.put("content", item.content);
        db.update("items", row, "id = ?", new String[] { String.valueOf(item.id) } );
        db.close();
    }

}

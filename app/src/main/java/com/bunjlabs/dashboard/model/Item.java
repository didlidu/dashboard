package com.bunjlabs.dashboard.model;

import java.io.Serializable;

public class Item implements Serializable {
    public long id;
    public String name;
    public long price;
    public String content;

    public Item() {
        this("", 0, "");
    }

    public Item(String name, long price, String content) {
        this.id = -1;
        this.name = name;
        this.price = price;
        this.content = content;
    }

    public Item(long id, String name, long price, String content) {
        this(name, price, content);
        this.id = id;
    }

}

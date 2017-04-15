package com.scanba.solidusandroid.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import com.scanba.solidusandroid.models.order.OrderLineItem;

import java.sql.SQLException;
import java.util.List;

@DatabaseTable(tableName = "orders")
public class Order extends Base {
    @DatabaseField(id = true)
    @SerializedName("id")
    private int id;

    @DatabaseField
    @SerializedName("number")
    private String number;

    @DatabaseField
    @SerializedName("email")
    public String email;

    @DatabaseField
    @SerializedName("token")
    private String token;

    @SerializedName("line_items")
    private List<OrderLineItem> lineItems;

    @SerializedName("ship_address")
    private Address shipAddress;

    @SerializedName("bill_address")
    private Address billAddress;

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getToken() {
        return token;
    }

    public List<OrderLineItem> getLineItems() {
        return lineItems;
    }

    public Address getShipAddress() {
        return shipAddress;
    }

    public Address getBillAddress() {
        return billAddress;
    }
}

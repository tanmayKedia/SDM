package com.poipoint.sdm.Models;

/**
 * Created by Tanmay on 5/26/2016.
 */
public class CategoryItem {
    private int id;
    private String name;
    private int order_id;
    private String icon;

    public CategoryItem(int id, String name, int order_id, String icon) {
        this.id = id;
        this.name = name;
        this.order_id = order_id;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

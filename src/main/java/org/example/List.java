package org.example;

import org.json.JSONObject;

public class List {
    private int id;
    private String name;
    private String icon;
    private int type;


    // 构造函数
    public List(int id, String name, String icon, int type) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
    }

    // Getters 和 Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    // JSON序列化方法
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("icon", icon);
        json.put("type", type);
        return json;
    }
}

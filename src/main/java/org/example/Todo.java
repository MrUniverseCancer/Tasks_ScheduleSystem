package org.example;

import org.json.JSONObject;

public class Todo {
    private int id;
    private String title;
    private boolean completed;
    private String dueDate;
    private int importance;
    private String list;

    public Todo(int id, String title, boolean completed, String dueDate, int importance, String list) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.dueDate = dueDate;
        this.importance = importance;
        this.list = list;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getImportance() {
        return importance;
    }

    public String getList() {
        return list;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setList(String list) {
        this.list = list;
    }

    // 将Todo对象转换为JSON格式
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("completed", completed);
        json.put("dueDate", dueDate);
        json.put("importance", importance);
        json.put("list", list);
        return json;
    }
}

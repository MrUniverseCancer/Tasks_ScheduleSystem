package org.example;

import java.sql.ResultSet;

public class DataItem {
    private int list_id;
    private int id;;
    private String name;
    private boolean isCompleted;
    private String dueDate;
    private int importance;

    public DataItem(String name, int age) {
        this.name = name;
        this.list_id = age;
    }

    public DataItem(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("title");
            this.list_id = resultSet.getInt("list_id");
            this.isCompleted = resultSet.getBoolean("completed");
            this.dueDate = resultSet.getString("dueDate");
            this.importance = resultSet.getInt("importance");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getter 方法
    public String getName() {
        return name;
    }
    public int getlist_id() {
        return list_id;
    }
    public boolean isCompleted() {
        return isCompleted;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getImportance() {
        return importance;
    }

    public int getId() {
        return id;
    }
}
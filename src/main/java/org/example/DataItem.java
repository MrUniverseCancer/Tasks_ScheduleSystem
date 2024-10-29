package org.example;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DataItem {
    private int list_id;
    private int id;;
    private String name;
    private boolean isCompleted;
//    private String dueDate;
    private int dueDate;
    private int importance;


    public DataItem() {
    }
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
            this.dueDate = getLeftDays(resultSet.getString("due_date"));

            this.importance = resultSet.getInt("importance");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getLeftDays(String dueDate) {
        // 获取现在的时间，精确到天
        // 解析dueDate的时间，形态为2024-10-31
        // 获取两者的差值
        // 返回差值
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 解析输入的截止日期
        LocalDate dueLocalDate = LocalDate.parse(dueDate, formatter);

        // 获取当前日期
        LocalDate nowLocalDate = LocalDate.now();

        // 计算两个日期之间的天数差
        long daysBetween = ChronoUnit.DAYS.between(nowLocalDate, dueLocalDate);

        // 返回差值（注意：这里返回的是int类型，如果差值可能为负数，请确保这是你想要的行为）
        return (int) daysBetween;
    }
    public static void main(String[] args) {
        // test
        DataItem calculator = new DataItem();
        String dueDate = "2024-11-09";
        int leftDays = calculator.getLeftDays(dueDate);
        System.out.println("Left days: " + leftDays);
    }

    // Getter 方法
    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getDueDate() {
        return dueDate;
    }

    public int getImportance() {
        return importance;
    }

    public int getId() {
        return id;
    }
    public int getList_id() {
        return list_id;
    }

}
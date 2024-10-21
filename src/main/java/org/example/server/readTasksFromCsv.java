package org.example.server;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class readTasksFromCsv {

    public List<Task> readtasks() {
        String sql = "SELECT * FROM tasks ORDER BY (importance + urgency) DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Task task = new Task();
                task.setUid(rs.getInt("uid"));
                task.setName(rs.getString("name"));
                task.setDescription(rs.getString("description"));
                task.setImportance(rs.getInt("importance"));
                task.setUrgency(rs.getInt("urgency"));
                task.setTaskTime(rs.getObject("task_time") != null ? rs.getInt("task_time") : null);
                String ddlStr = rs.getString("ddl");
                task.setDdl(ddlStr != null ? LocalDateTime.parse(ddlStr) : null);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<String[]> getTasks() {
        List<String[]> result = new ArrayList<>();
        List<Task> tasks = this.readtasks();

        for (Task task : tasks) {
            String[] taskArray = new String[13]; // 保持13个元素以匹配原来的CSV格式
            taskArray[0] = String.valueOf(task.getUid());
            taskArray[1] = task.getName();
            taskArray[2] = task.getDescription();
            taskArray[3] = ""; // 开始时间，当前Task类不支持，用空字符串占位
            taskArray[4] = ""; // 结束时间，当前Task类不支持，用空字符串占位
            taskArray[5] = task.getDdl() != null ? task.getDdl().toString() : "";
            taskArray[6] = task.getTaskTime() != null ? task.getTaskTime().toString() : "";
            taskArray[7] = ""; // 状态，当前Task类不支持，用空字符串占位
            taskArray[8] = ""; // 类型，当前Task类不支持，用空字符串占位
            taskArray[9] = String.valueOf(task.getImportance());
            taskArray[10] = String.valueOf(task.getUrgency());
            taskArray[11] = ""; // 创建时间，当前Task类不支持，用空字符串占位
            taskArray[12] = ""; // 更新时间，当前Task类不支持，用空字符串占位

            result.add(taskArray);
        }

        return result;
    }
}

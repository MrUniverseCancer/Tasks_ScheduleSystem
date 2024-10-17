package org.example.server;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class TaskAdapter {

    public static void addTask(String taskName, int importance, int urgency, String description, Integer taskTime, LocalDateTime ddl) {
        Task task = new Task();
        task.setName(taskName);
        task.setImportance(importance);
        task.setUrgency(urgency);
        task.setDescription(description);
        task.setTaskTime(taskTime);
        task.setDdl(ddl);

        try {
            int newId = DatabaseManager.addTask(task);
            System.out.println("新任务已添加，ID: " + newId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int deleteTask(String taskName, int importance, int urgency) {
        try {
            List<Task> tasks = DatabaseManager.getAllTasks();
            for (Task task : tasks) {
                if (task.getName().equals(taskName) &&
                        task.getImportance() == importance &&
                        task.getUrgency() == urgency) {
                    boolean deleted = DatabaseManager.deleteTask(task.getUid());
                    return deleted ? 0 : 2; // 0 表示删除成功，2 表示删除失败
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 1; // 1 表示发生错误
        }
        return 2; // 没有找到匹配的任务
    }

    public static List<String[]> readTasks() {
        List<String[]> result = new ArrayList<>();
        try {
            List<Task> tasks = DatabaseManager.getAllTasks();
            for (Task task : tasks) {
                String[] taskArray = new String[]{
                        task.getName(),
                        String.valueOf(task.getImportance()),
                        String.valueOf(task.getUrgency()),
                        String.valueOf(task.getUid()),
                        task.getDescription(),
                        task.getTaskTime() != null ? task.getTaskTime().toString() : "",
                        task.getDdl() != null ? task.getDdl().toString() : ""
                };
                result.add(taskArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

package org.example.server;

import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            createTable();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        String csvFile = "src/main/resources/task_plan/data.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile));
             Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO tasks (name, description, importance, urgency, task_time, ddl) VALUES (?, ?, ?, ?, ?, ?)")) {

            // 跳过CSV文件的标题行
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                // 解析CSV行数据
                String name = line[0];
                int importance = Integer.parseInt(line[9]);
                int urgency = Integer.parseInt(line[10]);
                Integer taskTime = line[8].isEmpty() ? null : Integer.parseInt(line[8]);
                String description = line.length > 12 ? line[12] : null;

                // 设置PreparedStatement参数
                pstmt.setString(1, name);
                pstmt.setString(2, description);
                pstmt.setInt(3, importance);
                pstmt.setInt(4, urgency);
                if (taskTime != null) {
                    pstmt.setInt(5, taskTime);
                } else {
                    pstmt.setNull(5, Types.INTEGER);
                }
                pstmt.setNull(6, Types.VARCHAR); // 由于CSV中没有DDL数据，设置为null

                // 执行插入操作
                pstmt.executeUpdate();
            }

            System.out.println("Database initialized successfully.");
        } catch (IOException | CsvException | SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }


    private static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "uid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "importance INTEGER," +
                "urgency INTEGER," +
                "task_time INTEGER," +
                "ddl TEXT" +
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // 添加任务
    public static int addTask(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (name, description, importance, urgency, task_time, ddl) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getImportance());
            pstmt.setInt(4, task.getUrgency());
            if (task.getTaskTime() != null) {
                pstmt.setInt(5, task.getTaskTime());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setString(6, task.getDdl() != null ? task.getDdl().toString() : null);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating task failed, no ID obtained.");
                }
            }
        }
    }

    // 更新任务
    public static boolean updateTask(Task task) throws SQLException {
        String sql = "UPDATE tasks SET name = ?, description = ?, importance = ?, urgency = ?, task_time = ?, ddl = ? WHERE uid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getDescription());
            pstmt.setInt(3, task.getImportance());
            pstmt.setInt(4, task.getUrgency());
            if (task.getTaskTime() != null) {
                pstmt.setInt(5, task.getTaskTime());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setString(6, task.getDdl() != null ? task.getDdl().toString() : null);
            pstmt.setInt(7, task.getUid());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // 删除任务
    public static boolean deleteTask(int uid) throws SQLException {
        String sql = "DELETE FROM tasks WHERE uid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, uid);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // 获取所有任务
    public static List<Task> getAllTasks() throws SQLException {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = getConnection();
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
        }
        return tasks;
    }

    // 根据UID获取任务
    public static Task getTaskByUid(int uid) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE uid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, uid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Task task = new Task();
                    task.setUid(rs.getInt("uid"));
                    task.setName(rs.getString("name"));
                    task.setDescription(rs.getString("description"));
                    task.setImportance(rs.getInt("importance"));
                    task.setUrgency(rs.getInt("urgency"));
                    task.setTaskTime(rs.getObject("task_time") != null ? rs.getInt("task_time") : null);
                    String ddlStr = rs.getString("ddl");
                    task.setDdl(ddlStr != null ? LocalDateTime.parse(ddlStr) : null);
                    return task;
                }
            }
        }
        return null;
    }

    // 更新任务紧迫程度
    public static boolean updateTaskUrgency(int uid, int newUrgency) throws SQLException {
        String sql = "UPDATE tasks SET urgency = ? WHERE uid = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newUrgency);
            pstmt.setInt(2, uid);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}

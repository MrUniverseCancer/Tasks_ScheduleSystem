package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;


public class TodoSQLiteManager {
    private static final String DB_URL = "jdbc:sqlite:todos.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            createTables();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TodoSQLiteManager() {
        if (getAllLists().length() == 0) {
            populateExampleData(); // 用于测试
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }


    private static void createTables() {
        String todosSql = "CREATE TABLE IF NOT EXISTS todos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "completed BOOLEAN NOT NULL," +
                "dueDate TEXT," +
                "importance INTEGER," +
                "list_id INTEGER," +
                "FOREIGN KEY (list_id) REFERENCES lists(id))";

        String listsSql = "CREATE TABLE IF NOT EXISTS lists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL UNIQUE," +
                "icon TEXT)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(listsSql);
            stmt.execute(todosSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JSONObject addList(JSONObject listJson) {
        String sql = "INSERT INTO lists (name, icon) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, listJson.getString("name"));
            pstmt.setString(2, listJson.getString("icon"));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        listJson.put("id", generatedKeys.getInt(1));
                        return listJson;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getAllLists() {
        JSONArray listsJson = new JSONArray();
        String sql = "SELECT * FROM lists";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                listsJson.put(resultSetToListJson(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listsJson;
    }

    public JSONObject updateList(JSONObject listJson) {
        String sql = "UPDATE lists SET name = ?, icon = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, listJson.getString("name"));
            pstmt.setString(2, listJson.getString("icon"));
            pstmt.setInt(3, listJson.getInt("id"));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return listJson;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteList(int id) {
        String sql = "DELETE FROM lists WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JSONObject resultSetToListJson(ResultSet rs) throws SQLException {
        JSONObject listJson = new JSONObject();
        listJson.put("id", rs.getInt("id"));
        listJson.put("name", rs.getString("name"));
        listJson.put("icon", rs.getString("icon"));
        return listJson;
    }


    public JSONObject addTodo(JSONObject todoJson) {
        String sql = "INSERT INTO todos (title, completed, dueDate, importance, list_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, todoJson.getString("title"));
            pstmt.setBoolean(2, todoJson.getBoolean("completed"));
            pstmt.setString(3, todoJson.getString("dueDate"));
            pstmt.setInt(4, todoJson.getInt("importance"));
            pstmt.setInt(5, todoJson.getInt("list_id"));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        todoJson.put("id", generatedKeys.getInt(1));
                        return todoJson;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject updateTodo(JSONObject todoJson) {
        String sql = "UPDATE todos SET title = ?, completed = ?, dueDate = ?, importance = ?, list_id = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, todoJson.getString("title"));
            pstmt.setBoolean(2, todoJson.getBoolean("completed"));
            pstmt.setString(3, todoJson.getString("dueDate"));
            pstmt.setInt(4, todoJson.getInt("importance"));
            pstmt.setInt(5, todoJson.getInt("list_id"));
            pstmt.setInt(6, todoJson.getInt("id"));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return todoJson;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject resultSetToJson(ResultSet rs) throws SQLException {
        JSONObject todoJson = new JSONObject();
        todoJson.put("id", rs.getInt("id"));
        todoJson.put("title", rs.getString("title"));
        todoJson.put("completed", rs.getBoolean("completed"));
        todoJson.put("dueDate", rs.getString("dueDate"));
        todoJson.put("importance", rs.getInt("importance"));
        todoJson.put("list_id", rs.getInt("list_id"));
        todoJson.put("list", rs.getString("list_name"));
        return todoJson;
    }


    public void deleteTodo(int id) {
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getAllTodos() {
        JSONArray todosJson = new JSONArray();
        String sql = "SELECT todos.*, lists.name AS list_name FROM todos LEFT JOIN lists ON todos.list_id = lists.id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                todosJson.put(resultSetToJson(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todosJson;
    }

    public JSONObject getTodo(int id) {
        String sql = "SELECT todos.*, lists.name AS list_name FROM todos LEFT JOIN lists ON todos.list_id = lists.id WHERE todos.id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToJson(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray sortTodos(String criteria, String direction) {
        String sql = "SELECT * FROM todos ORDER BY " + criteria + " " + direction;
        JSONArray sortedTodosJson = new JSONArray();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sortedTodosJson.put(resultSetToJson(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sortedTodosJson;
    }

    public JSONArray searchTodos(String query) {
        String sql = "SELECT todos.*, lists.name AS list_name FROM todos " +
                "LEFT JOIN lists ON todos.list_id = lists.id " +
                "WHERE todos.title LIKE ? OR lists.name LIKE ?";
        JSONArray searchResults = new JSONArray();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String likeQuery = "%" + query + "%";
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    searchResults.put(resultSetToJson(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    public void populateExampleData() {
        String[] defaultLists = {"我的一天", "计划内", "已分配给我", "任务"};
        for (String listName : defaultLists) {
            JSONObject list = new JSONObject();
            list.put("name", listName);
            list.put("icon", "•");
            addList(list);
        }
    }
}
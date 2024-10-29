package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;


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
                "icon TEXT," +
                "type INTEGER DEFAULT 0)";

        String ordersSql = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "list_id INTEGER," +
                "todo_id INTEGER," +
                "FOREIGN KEY (list_id) REFERENCES lists(id)," +
                "FOREIGN KEY (todo_id) REFERENCES todos(id))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(listsSql);
            stmt.execute(todosSql);
            stmt.execute(ordersSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 对orderSql进行add
    public void addOrder(int list_id, int todo_id) {
        String sql = "INSERT INTO orders (list_id, todo_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, list_id);
            pstmt.setInt(2, todo_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 对orderSql进行delete，删除全部
    public void deleteOrder() {
        String sql = "DELETE FROM orders";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询orders表的每一行，根据todo_id选项从todos表中得到对应的所有数据,使用resultSetToJson进行包装
    public JSONArray getOrders() {
        String sql = "SELECT * FROM orders";
        JSONArray ordersJson = new JSONArray();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int todo_id = rs.getInt("todo_id");
                // 将todo_id对应的数据加入到ordersJson中
                ordersJson.put(getTodo(todo_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordersJson;
    }





    public JSONObject addList(JSONObject listJson) {
        String sql = "INSERT INTO lists (name, icon, type) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, listJson.getString("name"));
            pstmt.setString(2, listJson.getString("icon"));
            pstmt.setInt(3, listJson.optInt("type", 0)); // 默认为0

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

    public ArrayList<ListsItem> getAllLists(int i) {
        ArrayList<ListsItem> result = new ArrayList<>();
        String sql = "SELECT * FROM lists";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ListsItem listsItem = new ListsItem(rs);
                result.add(listsItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject updateList(JSONObject listJson) {
        String sql = "UPDATE lists SET name = ?, icon = ?, type = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, listJson.getString("name"));
            pstmt.setString(2, listJson.getString("icon"));
            pstmt.setInt(3, listJson.optInt("type", 0)); // 默认为0
            pstmt.setInt(4, listJson.getInt("id"));

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
        listJson.put("type", rs.getInt("type"));
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

    public ArrayList<DataItem> getAllTodos(int i) {
        // 为了在Java模块中给任务做排序
        ArrayList<DataItem> result = new ArrayList<>();
        String sql = "SELECT todos.*, lists.name AS list_name FROM todos LEFT JOIN lists ON todos.list_id = lists.id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DataItem dataItem = new DataItem(rs);
                result.add(dataItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
        String orderByColumn;

        // 映射前端排序依据到数据库列名
        switch (criteria) {
            case "importance":
                orderByColumn = "importance";
                break;
            case "dueDate":
                orderByColumn = "dueDate";
                break;
            case "completed":
                orderByColumn = "completed";
                break;
            case "alphabetical":
                orderByColumn = "title";
                break;
            default:
                orderByColumn = "id"; // 默认排序
        }

        String orderDirection = "ASC";
        if ("desc".equalsIgnoreCase(direction)) {
            orderDirection = "DESC";
        }

        // 更新 SQL 查询，确保包含必要的 JOIN 操作
        String sql = "SELECT todos.*, lists.name AS list_name FROM todos " +
                "LEFT JOIN lists ON todos.list_id = lists.id " +
                "ORDER BY " + orderByColumn + " " + orderDirection;

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
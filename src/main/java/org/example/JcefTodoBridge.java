package org.example;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandler;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class JcefTodoBridge implements CefMessageRouterHandler {
    private List<Todo> todos = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
        JSONObject jsonRequest = new JSONObject(request);
        String action = jsonRequest.getString("action");

        try {
            switch (action) {
                case "addTodo":
                    callback.success(addTodo(jsonRequest).toString());
                    return true;
                case "deleteTodo":
                    deleteTodo(jsonRequest.getInt("id"));
                    callback.success("{\"success\": true}");
                    return true;
                case "getAllTodos":
                    callback.success(getAllTodos().toString());
                    return true;
                case "updateTodo":
                    callback.success(updateTodo(jsonRequest).toString());
                    return true;
                case "getTodo":
                    callback.success(getTodo(jsonRequest.getInt("id")).toString());
                    return true;
                case "sortTodos":
                    callback.success(sortTodos(jsonRequest.getString("criteria"), jsonRequest.getString("direction")).toString());
                    return true;
                default:
                    callback.failure(0, "Unknown action");
                    return true;
            }
        } catch (Exception e) {
            callback.failure(0, "Error: " + e.getMessage());
            return true;
        }
    }

    private JSONObject addTodo(JSONObject jsonRequest) {
        Todo newTodo = new Todo(
                nextId++,
                jsonRequest.getString("title"),
                jsonRequest.getBoolean("completed"),
                jsonRequest.getString("dueDate"),
                jsonRequest.getInt("importance"),
                jsonRequest.getString("list")
        );
        todos.add(newTodo);
        return newTodo.toJson();
    }

    private void deleteTodo(int id) {
        todos.removeIf(todo -> todo.getId() == id);
    }

    private JSONArray getAllTodos() {
        JSONArray todosJson = new JSONArray();
        for (Todo todo : todos) {
            todosJson.put(todo.toJson());
        }
        return todosJson;
    }

    private JSONObject updateTodo(JSONObject jsonRequest) {
        int id = jsonRequest.getInt("id");
        Todo todoToUpdate = todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Todo not found"));

        todoToUpdate.setTitle(jsonRequest.getString("title"));
        todoToUpdate.setCompleted(jsonRequest.getBoolean("completed"));
        todoToUpdate.setDueDate(jsonRequest.getString("dueDate"));
        todoToUpdate.setImportance(jsonRequest.getInt("importance"));
        todoToUpdate.setList(jsonRequest.getString("list"));

        return todoToUpdate.toJson();
    }

    private JSONObject getTodo(int id) {
        return todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Todo not found"))
                .toJson();
    }

    private JSONArray sortTodos(String criteria, String direction) {
        Comparator<Todo> comparator;
        switch (criteria) {
            case "importance":
                comparator = Comparator.comparingInt(Todo::getImportance);
                break;
            case "dueDate":
                comparator = Comparator.comparing(Todo::getDueDate);
                break;
            case "alphabetical":
                comparator = Comparator.comparing(Todo::getTitle);
                break;
            default:
                throw new IllegalArgumentException("Invalid sort criteria");
        }

        if ("desc".equals(direction)) {
            comparator = comparator.reversed();
        }

        List<Todo> sortedTodos = todos.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        JSONArray sortedTodosJson = new JSONArray();
        for (Todo todo : sortedTodos) {
            sortedTodosJson.put(todo.toJson());
        }
        return sortedTodosJson;
    }

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long queryId) {
        // 处理查询取消的情况
    }

    @Override
    public long getNativeRef(String s) {
        return 0;
    }

    @Override
    public void setNativeRef(String s, long l) {

    }
}


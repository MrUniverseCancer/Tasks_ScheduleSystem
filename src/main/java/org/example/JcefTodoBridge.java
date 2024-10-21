package org.example;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefQueryCallback;
import org.cef.handler.CefMessageRouterHandler;
import org.json.JSONObject;

public class JcefTodoBridge implements CefMessageRouterHandler {
    private TodoSQLiteManager todoManager;

    public JcefTodoBridge() {
        todoManager = new TodoSQLiteManager();
    }

    @Override
    public boolean onQuery(CefBrowser browser, CefFrame frame, long queryId, String request, boolean persistent, CefQueryCallback callback) {
        JSONObject jsonRequest = new JSONObject(request);
        String action = jsonRequest.getString("action");

        try {
            switch (action) {
                case "addTodo":
                    callback.success(todoManager.addTodo(jsonRequest).toString());
                    return true;
                case "deleteTodo":
                    todoManager.deleteTodo(jsonRequest.getInt("id"));
                    callback.success("{\"success\": true}");
                    return true;
                case "getAllTodos":
                    callback.success(todoManager.getAllTodos().toString());
                    return true;
                case "updateTodo":
                    callback.success(todoManager.updateTodo(jsonRequest).toString());
                    return true;
                case "getTodo":
                    callback.success(todoManager.getTodo(jsonRequest.getInt("id")).toString());
                    return true;
                case "sortTodos":
                    callback.success(todoManager.sortTodos(jsonRequest.getString("criteria"), jsonRequest.getString("direction")).toString());
                    return true;
                case "addList":
                    callback.success(todoManager.addList(jsonRequest).toString());
                    return true;
                case "getAllLists":
                    callback.success(todoManager.getAllLists().toString());
                    return true;
                case "updateList":
                    callback.success(todoManager.updateList(jsonRequest).toString());
                    return true;
                case "deleteList":
                    todoManager.deleteList(jsonRequest.getInt("id"));
                    callback.success("{\"success\": true}");
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

    @Override
    public void onQueryCanceled(CefBrowser browser, CefFrame frame, long queryId) {
        // Handle query cancellation if needed
    }

    @Override
    public long getNativeRef(String s) {
        return 0;
    }

    @Override
    public void setNativeRef(String s, long l) {
        // Implementation if needed
    }
}
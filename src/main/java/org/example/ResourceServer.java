package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

public class ResourceServer {
    private static final int PORT = 8009;
    private static final String WEBAPP_BASE_PATH = "/out";

    private final HttpServer server;

    public ResourceServer() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        setupHandlers();
    }

    private void setupHandlers() {
        server.createContext("/", exchange -> {
            try {
                String requestPath = exchange.getRequestURI().getPath();
                // 将请求路径转换为资源路径
                String resourcePath = WEBAPP_BASE_PATH + (requestPath.equals("/") ? "/index.html" : requestPath);
                // 使用Class路径加载资源
                try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
                    if (inputStream != null) {
                        // 读取资源内容
                        byte[] content = inputStream.readAllBytes();

                        // 设置Content-Type
                        String contentType = getContentType(resourcePath);
                        exchange.getResponseHeaders().set("Content-Type", contentType);

                        // 发送响应
                        exchange.sendResponseHeaders(200, content.length);
                        exchange.getResponseBody().write(content);
                    } else {
                        // 资源未找到
                        sendNotFound(exchange);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendError(exchange, e);
            } finally {
                exchange.close();
            }
        });
    }

    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }

    private void sendNotFound(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        String response = "404 Not Found";
        exchange.sendResponseHeaders(404, response.length());
        exchange.getResponseBody().write(response.getBytes());
    }

    private void sendError(com.sun.net.httpserver.HttpExchange exchange, Exception e) throws IOException {
        String response = "500 Internal Server Error: " + e.getMessage();
        exchange.sendResponseHeaders(500, response.length());
        exchange.getResponseBody().write(response.getBytes());
    }

    public void start() {
        new Thread(server::start).start();
        System.out.println("Resource server started on port " + PORT);
    }

    public void stop() {
        server.stop(0);
    }
}
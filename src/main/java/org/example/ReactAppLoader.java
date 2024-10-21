package org.example;

import com.jetbrains.cef.JCefAppConfig;
import com.sun.net.httpserver.HttpServer;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefRendering;
import org.cef.browser.CefMessageRouter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReactAppLoader extends JFrame {
    private final CefApp cefApp;
    private final CefClient client;
    private final CefBrowser browser;
    private static final int PORT = 8009;
    private static final String BASE_DIR = "out";

    public ReactAppLoader(String reactAppPath) {
        // 1. 初始化JCEF配置
        final JCefAppConfig jCefAppConfig = JCefAppConfig.getInstance();
        final CefSettings cefSettings = jCefAppConfig.getCefSettings();

        // 2. 启动CEF
        CefApp.startup(jCefAppConfig.getAppArgs());
        cefApp = CefApp.getInstance(jCefAppConfig.getAppArgs(), cefSettings);

        // 3. 创建客户端
        client = cefApp.createClient();

        // 4. 设置消息路由器（如果需要JavaScript通信）
        CefMessageRouter messageRouter = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig());
        // 注册Java桥接类
        messageRouter.addHandler(new JcefTodoBridge(), true);
        client.addMessageRouter(messageRouter);

        // 7. 创建浏览器实例（使用新的创建方式）
        String url = "http://localhost:" + PORT + "/index.html";
        System.out.println("访问的URL: " + url);
        browser = client.createBrowser(url, CefRendering.DEFAULT, true);
        Component browserUI = browser.getUIComponent();

        // 8. 设置窗口属性
        setTitle("Test");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(browserUI);
        setSize(1600, 1000);
        setLocationRelativeTo(null);
    }

    private static void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", exchange -> {
            String filePath = BASE_DIR + exchange.getRequestURI().getPath();
            File file = new File(filePath);
            System.out.println("访问的绝对路径: " + file.getAbsolutePath());

            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                exchange.sendResponseHeaders(200, fileBytes.length);
                exchange.getResponseBody().write(fileBytes);
            } else {
                String errorMessage = "404 (Not Found)\n";
                exchange.sendResponseHeaders(404, errorMessage.length());
                exchange.getResponseBody().write(errorMessage.getBytes());
            }
            exchange.close();
        });
        new Thread(server::start).start();
    }

    public static void main(String[] args) throws IOException {
        startHttpServer();
        // 设置React应用的绝对路径
        String reactAppPath = System.getProperty("user.dir") + "/web";
        System.out.println("React App Path: " + reactAppPath);

        SwingUtilities.invokeLater(() -> {
            ReactAppLoader frame = new ReactAppLoader(reactAppPath);
            frame.setVisible(true);
        });
    }
}
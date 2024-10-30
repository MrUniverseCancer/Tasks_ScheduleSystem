package org.example;

import com.jetbrains.cef.JCefAppConfig;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefRendering;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ReactAppLoaderBuild extends JFrame {
    private static final int PORT = 8009;
    private final ResourceServer resourceServer;

    public ReactAppLoaderBuild() throws IOException {
        // 启动资源服务器
        this.resourceServer = new ResourceServer();
        this.resourceServer.start();

        // 初始化CEF浏览器
        initBrowser();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ReactAppLoaderBuild frame = new ReactAppLoaderBuild();
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Failed to start application: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initBrowser() {
        final JCefAppConfig jCefAppConfig = JCefAppConfig.getInstance();
        final CefSettings cefSettings = jCefAppConfig.getCefSettings();
        CefApp.startup(jCefAppConfig.getAppArgs());
        CefApp cefApp = CefApp.getInstance(jCefAppConfig.getAppArgs(), cefSettings);
        CefClient client = cefApp.createClient();

        // 设置消息路由
        CefMessageRouter messageRouter = CefMessageRouter.create(
                new CefMessageRouter.CefMessageRouterConfig()
        );
        messageRouter.addHandler(new JcefTodoBridge(), true);
        client.addMessageRouter(messageRouter);

        // 创建浏览器
        String url = "http://localhost:" + PORT + "/index.html";
        System.out.println("Loading URL: " + url);
        CefBrowser browser = client.createBrowser(url, CefRendering.DEFAULT, true);
        Component browserUI = browser.getUIComponent();

        // 设置窗口
        setTitle("Tasks Schedule System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(browserUI);
        setSize(1600, 1000);
        setLocationRelativeTo(null);

        // 添加窗口关闭事件处理
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                resourceServer.stop();
            }
        });
    }
}
package org.example;

import com.jetbrains.cef.JCefAppConfig;
import com.sun.net.httpserver.HttpServer;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefRendering;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReactAppLoader extends JFrame {
    private static final int PORT = 8009;
    private static final String BASE_DIR = "frontend/out";

    public ReactAppLoader(String reactAppPath) {
        final JCefAppConfig jCefAppConfig = JCefAppConfig.getInstance();
        final CefSettings cefSettings = jCefAppConfig.getCefSettings();
        CefApp.startup(jCefAppConfig.getAppArgs());
        CefApp cefApp = CefApp.getInstance(jCefAppConfig.getAppArgs(), cefSettings);
        CefClient client = cefApp.createClient();
        CefMessageRouter messageRouter = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig());
        messageRouter.addHandler(new JcefTodoBridge(), true);
        client.addMessageRouter(messageRouter);
//        String url = "http://localhost:" + PORT + "/index.html";
        String url = "http://localhost:3000";
        System.out.println("访问的URL: " + url);
        CefBrowser browser = client.createBrowser(url, CefRendering.DEFAULT, true);
        Component browserUI = browser.getUIComponent();
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
//        startHttpServer();
        String reactAppPath = System.getProperty("user.dir") + "/web";
        SwingUtilities.invokeLater(() -> {
            ReactAppLoader frame = new ReactAppLoader(reactAppPath);
            frame.setVisible(true);
        });
    }
}
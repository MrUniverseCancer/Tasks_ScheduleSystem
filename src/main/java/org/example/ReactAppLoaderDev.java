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

public class ReactAppLoaderDev extends JFrame {

    public ReactAppLoaderDev() {
        final JCefAppConfig jCefAppConfig = JCefAppConfig.getInstance();
        final CefSettings cefSettings = jCefAppConfig.getCefSettings();
        CefApp.startup(jCefAppConfig.getAppArgs());
        CefApp cefApp = CefApp.getInstance(jCefAppConfig.getAppArgs(), cefSettings);
        CefClient client = cefApp.createClient();
        CefMessageRouter messageRouter = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig());
        messageRouter.addHandler(new JcefTodoBridge(), true);
        client.addMessageRouter(messageRouter);
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

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            ReactAppLoaderDev frame = new ReactAppLoaderDev();
            frame.setVisible(true);
        });
    }
}
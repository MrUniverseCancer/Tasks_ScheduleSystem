package org.example;

import com.jetbrains.cef.JCefAppConfig;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefRendering;

import javax.swing.*;

public class Demo {
    public static void main(String[] args) {
        final JCefAppConfig jCefAppConfig = JCefAppConfig.getInstance();
        final CefSettings cefSettings = jCefAppConfig.getCefSettings();
        CefApp.startup(jCefAppConfig.getAppArgs());
        final CefApp cefApp = CefApp.getInstance(jCefAppConfig.getAppArgs(), cefSettings);

        System.out.println(cefApp.getVersion());

        final CefClient cefClient = cefApp.createClient();
        final CefBrowser browser = cefClient.createBrowser("https://www.baidu.com", CefRendering.DEFAULT, true);

        final JFrame frame = new JFrame();
        frame.setTitle("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(browser.getUIComponent());
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
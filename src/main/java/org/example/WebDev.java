//package org.example;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class WebDev {
//
//    private static final String UPLOAD_URL = "http://your-cloud-storage/upload";
//    private static final String DOWNLOAD_URL = "http://your-cloud-storage/download";
//
//    private static final String DB_path = "jdbc:sqlite:todos.db";
//
//    public WebDev() {
//    }
//
//    public void syncDatabase() {
//        uploadDatabase(DB_path);
//        downloadDatabase(DB_path);
//    }
//
//    private void uploadDatabase(String filePath) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost uploadFile = new HttpPost(UPLOAD_URL);
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addBinaryBody("file", new File(filePath));
//            HttpEntity multipart = builder.build();
//            uploadFile.setEntity(multipart);
//            HttpResponse response = httpClient.execute(uploadFile);
//            HttpEntity responseEntity = response.getEntity();
//            System.out.println(EntityUtils.toString(responseEntity));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void downloadDatabase(String filePath) {
//        try (InputStream in = new URL(new URI(DOWNLOAD_URL).toASCIIString()).openStream();
//             FileOutputStream fos = new FileOutputStream(new File(filePath))) {
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = in.read(buffer)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
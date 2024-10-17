package org.example.server;

import java.io.*;

public class IDMaintain {
    // 维护一个全局的ID


    public static int setID(){
        String filePath = "src/main/resources/task_plan/ID.txt";
        int ID = 0;
        boolean fileExists = new java.io.File(filePath).exists(); // 检查文件是否存在

        ID = getID();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            // 如果文件不存在，ID设置为1
            if (!fileExists) {
                ID = 1;
            }

            // 读取ID，并且+1
            writer.write(String.valueOf(ID+1));
            writer.newLine();

            System.out.println("Data processed successfully!+-*/36");
            return ID+1; // success
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // fail
        }
    }

    public static int getID(){
        String filePath = "src/main/resources/task_plan/ID.txt";
        int ID = 0;
        boolean fileExists = new java.io.File(filePath).exists(); // 检查文件是否存在

        // 读取第一行的ID信息
        if(!fileExists){
            return -1;
        }
        else {
            // 读取文件第一行
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line = reader.readLine();
                if(line == null){
                    System.out.println("error! ID file is empty, check it out!");
                    return 0;
                }
                ID = Integer.parseInt(line);
                return ID;
            } catch (IOException e) {
                e.printStackTrace();
                return -2;
                // 读取失败
            }
        }

    }
}

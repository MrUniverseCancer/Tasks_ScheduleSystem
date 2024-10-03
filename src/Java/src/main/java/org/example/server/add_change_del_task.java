package org.example.server;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.GUI_design.generalData.Conditional_Compilation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class add_change_del_task {

    public static void add_task(String task_name, int fact_importance, int fact_urgency, int ID){
        // 添加任务
        if(ID == -1) {
            ID = ID_maintain.setID();
        }
        String [] new_data = {task_name, "0", "0", "0", "0", "0", "0", "0", "0",
                String.valueOf(fact_importance), String.valueOf(fact_urgency),String.valueOf(ID), "Task Description"};
        // 输出文件路径

        if(Conditional_Compilation.Is_Building) {
            String filePath = "task_plan/data.csv";
            boolean fileExists = true; // 检查文件是否存在
            // 创建目录（如果不存在的话）
            File directory = new File(filePath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
                fileExists = false;
            }
            // 文件写入
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                if(!fileExists){
                    writer.write("TaskName,LeftTime,ImportanceLevel,TaskConsuming,PunishLevel,PreferenceLevel,DifficultyLevel,BufferbtwTasks,singalTaskTime,FactImportance,FactUrgency,ID,Description"); // 表头
                    writer.newLine();
                }
                // 写入数据和
                writer.write(String.join(",", new_data));
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException("无法写入文件", e);
            }
        }


        // 创建文件对象
        else {
            String filePath = "src/main/resources/task_plan/data.csv";
            boolean fileExists = new java.io.File(filePath).exists(); // 检查文件是否存在
//            System.out.println("Current working directory: " + System.getProperty("user.dir"));
//            System.out.println("File exists: " + fileExists);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                // 如果文件不存在，写入表头
                if (!fileExists) {
                    writer.write("TaskName,LeftTime,ImportanceLevel,TaskConsuming,PunishLevel,PreferenceLevel,DifficultyLevel,BufferbtwTasks,singalTaskTime,FactImportance,FactUrgency"); // 表头
                    writer.newLine();
                }

                // 写入数据和
                writer.write(String.join(",", new_data));
                writer.newLine();

                System.out.println("Data processed successfully!+-*/36");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int delete_task(String task_name, int fact_importance, int fact_urgency){
        // 删除任务
        // 读取文件
//        String filePath = "src/main/resources/task_plan/data.csv";
//        String filePath = "task_plan/data.csv";
        String filePath = Conditional_Compilation.Is_Building ? "task_plan/data.csv" : "src/main/resources/task_plan/data.csv";
        List<String[]> allData = new ArrayList<>();
        // 创建文件对象
        File file = new File(filePath);
        boolean fileExists = file.exists(); // 检查文件是否存在
        if (!fileExists) {
            return 1;
            // 文件不存在或损坏
        }

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            allData = reader.readAll();
            // 要匹配的固定位置
            int[] indicesToMatch = {0, 9, 10}; // 例如我们要检查第0列和第2列
            String[] valuesToMatch = {task_name, String.valueOf(fact_importance), String.valueOf(fact_urgency)}; // 要匹配的值

            // 删除匹配的行
            int result = removeMatchingRows(allData, indicesToMatch, valuesToMatch);
            if(result == 0){
                return 2;
                // 删除失败
            }
        } catch (IOException | CsvException e) {
            System.out.println("Error in reading CSV file");
            e.printStackTrace();
        }

        try (FileWriter  writer = new FileWriter (filePath, false)) {
            // 写入内容
            for(String[] row : allData){
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int removeMatchingRows(List<String[]> list, int[] indicesToMatch, String[] valuesToMatch) {
        if (indicesToMatch.length != valuesToMatch.length) {
            throw new IllegalArgumentException("Indices and values length must be the same.");
        }

        Iterator<String[]> iterator = list.iterator();

        while (iterator.hasNext()) {
            String[] current = iterator.next();

            boolean matches = true;
            for (int i = 0; i < indicesToMatch.length; i++) {
                int index = indicesToMatch[i];
                if (index >= current.length || !current[index].equals(valuesToMatch[i])) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                iterator.remove(); // 安全地删除匹配的行
                return 1;
                // 删除成功
            }
        }
        return 0;
        // 删除失败
    }

}

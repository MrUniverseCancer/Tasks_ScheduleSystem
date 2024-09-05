package org.example.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class add_change_del_task {

    public static void add_task(String task_name, int fact_importance, int fact_urgency){
        // 添加任务
        String [] new_data = {task_name, "0", "0", "0", "0", "0", "0", "0", "0", String.valueOf(fact_importance), String.valueOf(fact_urgency)};
        // 输出文件路径
        String filePath = "src/main/resources/task_plan/data.csv";

        // 创建文件对象
        File file = new File(filePath);
        boolean fileExists = file.exists(); // 检查文件是否存在

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // 如果文件不存在，写入表头
            if (!fileExists) {
                writer.write("TaskName,LeftTime,ImportanceLevel,TaskConsuming,PunishLevel,PreferenceLevel,DifficultyLevel,BufferbtwTasks,singalTaskTime,FactImportance,FactUrgency"); // 表头
                writer.newLine();
            }

            // 写入数据和
            writer.write(String.join(",", new_data));
            writer.newLine();
            
            System.out.println("Data processed successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

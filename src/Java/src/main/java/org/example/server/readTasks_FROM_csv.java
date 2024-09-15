package org.example.server;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.GUI_design.generalData.Conditional_Compilation;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class readTasks_FROM_csv {

    private int length = 11; // 描述任务的属性的个数

    public List<String[]>  readtasks() {
        // 使用类加载器获取资源文件的输入流
        if(Conditional_Compilation.Is_Building) {
//            System.out.println("Building");
            String filePath = "task_plan/data.csv";
            boolean fileExists = true; // 检查文件是否存在
            // 创建目录（如果不存在的话）
            File directory = new File(filePath).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
                fileExists = false;
            }
            if( !fileExists ) {
                // 第一次从资源文件中读取缓存数据
                // 写入外部文件
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("task_plan/data.csv");
                     CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
                    if (inputStream == null) {
                        throw new IOException("Resource not found: task_plan/data.csv");
                    }

                    List<String[]> allData = reader.readAll();

                    // 过滤掉包含 -1 的项
                    List<String[]> filteredData = allData.stream()
                            .filter(row -> Arrays.stream(row).noneMatch(cell -> "-1".equals(cell)))
                            .collect(Collectors.toList());

                    for(String[] row : filteredData) {
                        writer.write(String.join(",", row));
                        writer.newLine();
                    }

                } catch (IOException | CsvException e) {
                    System.out.println("Error in reading CSV file");
                    e.printStackTrace();
                }
            }
            try (CSVReader reader = new CSVReader(new FileReader(filePath));) {

                List<String[]> allData = reader.readAll();
                // 去除第一行
                if (!allData.isEmpty()) {
                    allData.remove(0); // 修正： 使用 remove(0) 去除第一行
                }
                // 过滤掉包含 -1 的项
                List<String[]> filteredData = allData.stream()
                        .filter(row -> Arrays.stream(row).noneMatch(cell -> "-1".equals(cell)))
                        .collect(Collectors.toList());

                return tasksSort(filteredData); // 返回过滤后的数据

            } catch (IOException | CsvException e) {
                System.out.println("Error in reading CSV file");
                e.printStackTrace();
            }
            return null; // 返回 null 或者合适的默认值}
        }
        else {
//            System.out.println("Not Building");
            String csvFile = "src/main/resources/task_plan/data.csv";
            try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                List<String[]> allData = reader.readAll();
                // 去除第一行
                if (!allData.isEmpty()) {
                    allData.removeFirst();
                }
                // 过滤掉包含 -1 的项
                List<String[]> filteredData = allData.stream()
                        .filter(row -> Arrays.stream(row).noneMatch(cell -> "-1".equals(cell)))
                        .collect(Collectors.toList());
                return tasksSort(filteredData);    // 返回过滤后的数据
            } catch (IOException | CsvException e) {
                System.out.println("Error in reading CSV file");
                e.printStackTrace();
            }
            return null;
        }
    }

    public List<String[]> tasksSort(List<String[]> tasks){
        // 对任务进行排序
        tasks.sort((o1, o2) -> {
            double importance1 = Double.parseDouble(o2[9]);
            double urgency1 = Double.parseDouble(o2[10]);
            double importance2 = Double.parseDouble(o1[9]);
            double urgency2 = Double.parseDouble(o1[10]);
            return Double.compare(importance1 + urgency1, importance2 + urgency2);
        });
        return tasks;
    }

    public static void main(String[] args) {
        readTasks_FROM_csv readTasks_FROM_csv = new readTasks_FROM_csv();
        readTasks_FROM_csv.readtasks();
    }
}

package org.example.server;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class readTasks_FROM_csv {

    private int length = 11; // 描述任务的属性的个数

    public List<String[]>  readtasks() {
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
            return filteredData;    // 返回过滤后的数据
        } catch (IOException | CsvException e) {
            System.out.println("Error in reading CSV file");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        readTasks_FROM_csv readTasks_FROM_csv = new readTasks_FROM_csv();
        readTasks_FROM_csv.readtasks();
    }
}
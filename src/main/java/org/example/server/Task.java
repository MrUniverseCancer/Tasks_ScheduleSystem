package org.example.server;

import java.time.LocalDateTime;

public class Task {
    private int uid;
    private String name;
    private String description;
    private int importance;
    private int urgency;
    private Integer taskTime; // 可以为null
    private LocalDateTime ddl;

    // 默认构造函数
    public Task() {}

    // 带参数的构造函数
    public Task(int uid, String name, String description, int importance, int urgency, Integer taskTime, LocalDateTime ddl) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.importance = importance;
        this.urgency = urgency;
        this.taskTime = taskTime;
        this.ddl = ddl;
    }

    // Getter 和 Setter 方法
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public Integer getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Integer taskTime) {
        this.taskTime = taskTime;
    }

    public LocalDateTime getDdl() {
        return ddl;
    }

    public void setDdl(LocalDateTime ddl) {
        this.ddl = ddl;
    }

    @Override
    public String toString() {
        return "Task{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", importance=" + importance +
                ", urgency=" + urgency +
                ", taskTime=" + taskTime +
                ", ddl=" + ddl +
                '}';
    }
}

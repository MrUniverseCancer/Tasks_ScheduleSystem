package org.example;

import java.util.ArrayList;

public class RankingList {

    public RankingList() {
    }

    public void addRankingList() {
        // 从数据库中获取数据
        // 并且保存在一个列表中
        ArrayList<DataItem> dataItems = new ArrayList<>();
        TodoSQLiteManager todoManager = new TodoSQLiteManager();
        dataItems = todoManager.getAllTodos(0);
        // 对列表进行排序

    }
}

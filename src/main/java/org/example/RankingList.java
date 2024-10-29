package org.example;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Comparator;

public class RankingList {

    public RankingList() {
    }

    public static JSONArray addRankingList() {
        // 先清楚上一次的缓存
        TodoSQLiteManager todoSQLiteManager = new TodoSQLiteManager();
        todoSQLiteManager.deleteOrder();
        // 从数据库中获取数据
        // 并且保存在一个列表中
        ArrayList<DataItem> dataItems = new ArrayList<>();
//        ArrayList<ListsItem> listsItems = new ArrayList<>();
        TodoSQLiteManager todoManager = new TodoSQLiteManager();
        dataItems = todoManager.getAllTodos(0);
//        listsItems = todoManager.getAllLists(0);
        // 对列表进行检查和筛选
//        // 记录listsItems中的每一个元素的id，如果是0或者1保留id。然后删除dataItems中的所有list_id不在这个列表中的元素
//        ArrayList<Integer> list_id_work = new ArrayList<>();
//        for (int i = 0; i < listsItems.size(); i++) {
//            if (listsItems.get(i).getId() == 1 || listsItems.get(i).getId() == 2) {
//                list_id_work.add(listsItems.get(i).getId());
//            }
//        }
        for (int i = 0; i < dataItems.size(); i++) {
            if (dataItems.get(i).isCompleted()) {
                dataItems.remove(i);
                i--;
            }
        }


        // 通过dataItems中的每一个元素的list_id，将其分为不同的新的dataItems列表
//        ArrayList<ArrayList<DataItem>> rankingList = new ArrayList<>();
//        for (int i = 0; i < dataItems.size(); i++) {
//            int list_id = dataItems.get(i).getList_id();
//            boolean flag = false;
//            for (int j = 0; j < rankingList.size(); j++) {
//                if (rankingList.get(j).get(0).getList_id() == list_id) {
//                    rankingList.get(j).add(dataItems.get(i));
//                    flag = true;
//                    break;
//                }
//            }
//            if (!flag) {
//                ArrayList<DataItem> temp = new ArrayList<>();
//                temp.add(dataItems.get(i));
//                rankingList.add(temp);
//            }
//        }
        // 对DataItems进行排序
        rankingALG(dataItems);
        // 将排序后的DataItems保存到数据库中
        for(int i = 0; i < dataItems.size(); i++){
            DataItem dataItem = dataItems.get(i);
            todoSQLiteManager.addOrder(dataItem.getList_id(), dataItem.getId());
        }

        return todoManager.getOrders();
    }

    public static void main(String[] args) {
        RankingList rankingList = new RankingList();
        rankingList.addRankingList();
    }


    public static void rankingALG(ArrayList<DataItem> dataItems) {
        // 对dataItems中的每一个元素进行排序
        // 只考虑重要程度
        dataItems.sort(new importance_Compare());
    }

    static class importance_Compare implements Comparator<DataItem>{
        @Override
        public int compare(DataItem o1, DataItem o2) {
            // 先通过时间区分，1天以内的，3天以内的，和3天以上的
            // 然后再通过重要程度区分
            int o1state = o1.getDueDate() > 3 ? 2 : (o1.getDueDate() > 1 ? 1 : 0);
            int o2state = o2.getDueDate() > 3 ? 2 : (o2.getDueDate() > 1 ? 1 : 0);
            if(o1state != o2state){
                return o1state - o2state;
            }
            return o2.getImportance() - o1.getImportance();
        }
    }
}

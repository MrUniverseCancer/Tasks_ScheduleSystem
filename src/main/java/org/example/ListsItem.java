package org.example;

import java.sql.ResultSet;

public class ListsItem {

//    DbColumn(name=id, type=4, typeName=INTEGER, schema=  )
//    DbColumn(name=name, type=12, typeName=TEXT, schema=NOT NULL  )
//    DbColumn(name=icon, type=12, typeName=TEXT, schema=  )
//    DbColumn(name=type, type=4, typeName=INTEGER, schema= DEFAULT 0 )
    private int id;
    private String name;
    private String icon;
    private int type;

    public ListsItem() {
    }

    public ListsItem(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("id");
            this.name = resultSet.getString("name");
            this.icon = resultSet.getString("icon");
            this.type = resultSet.getInt("type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}

package org.example.GUI_design;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class Menu_MainPage {
    private MenuBar menuBar = new MenuBar(); // 返回的菜单栏
    private Main main;

    public Menu_MainPage(Main main){
        this.main = main;
        createMenu();
    }

    public void createMenu(){
        // 创建任务菜单
        // 创建任务菜单项
        Menu menu_task = new Menu("任务");
        MenuItem task_page    = new MenuItem("任务页面");
        MenuItem history_task = new MenuItem("历史任务");

        // 创建计划菜单和菜单项项
        Menu menu_plan = new Menu("计划");
        MenuItem plan_page  = new MenuItem("计划页面");
        MenuItem make_plan     = new MenuItem("制定计划");


        menu_task.getItems().addAll(task_page, history_task);
        menu_plan.getItems().addAll(plan_page, make_plan);
        menuBar.getMenus().addAll(menu_task, menu_plan);
    }

    public MenuBar getMenuBar(){
        return this.menuBar;
    }
}


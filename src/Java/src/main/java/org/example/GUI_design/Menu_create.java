package org.example.GUI_design;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class Menu_create {
    private MenuBar MainmenuBar = new MenuBar(); // 主页面返回的菜单栏
    private MenuBar TaskmenuBar = new MenuBar(); // 任务页面返回的菜单栏
    private Main main;

    public Menu_create(Main main){
        this.main = main;
        createMainMenu();
        createTaskMenu();
    }

    public void createMainMenu(){
        // 创建任务菜单
        // 创建任务菜单项
        Menu menu_task = new Menu("任务");
        MenuItem task_page    = new MenuItem("任务页面");
        MenuItem history_task = new MenuItem("历史任务");

        task_page.setOnAction(e -> {
            main.getMainPage().from_Main_to_Task();
        });

        // 创建计划菜单和菜单项项
        Menu menu_plan = new Menu("计划");
        MenuItem plan_page  = new MenuItem("计划页面");
        MenuItem make_plan     = new MenuItem("制定计划");


        menu_task.getItems().addAll(task_page, history_task);
        menu_plan.getItems().addAll(plan_page, make_plan);
        MainmenuBar.getMenus().addAll(menu_task, menu_plan);
    }


    public void createTaskMenu(){
        // 创建任务菜单
        // 创建任务菜单项
        Menu menu_main = new Menu("主页面");
        MenuItem main_return    = new MenuItem("回到主页面");
        main_return.setOnAction(e -> {
            main.getMainPage().from_Task_to_Main();
        });

        // 创建计划菜单和菜单项项
        Menu menu_plan = new Menu("计划");
        MenuItem plan_page  = new MenuItem("计划页面");
        MenuItem make_plan     = new MenuItem("制定计划");

        // 创建任务菜单
        Menu menu_task = new Menu("任务选项");
        MenuItem add_task    = new MenuItem("添加任务");
        MenuItem delete_task = new MenuItem("删除任务");
        MenuItem edit_task   = new MenuItem("编辑任务");
        MenuItem finish_task = new MenuItem("完成任务");
        MenuItem unfinish_task = new MenuItem("未完成任务");
        add_task.setOnAction(e -> {
            Task_add task_add = new Task_add();
            task_add.openWindows_Task_add();
            main.getMainPage().refresh_scene(1);
        });



        // 创建任务菜单
        menu_main.getItems().addAll(main_return);
        menu_plan.getItems().addAll(plan_page, make_plan);
        menu_task.getItems().addAll(add_task, delete_task, edit_task, finish_task, unfinish_task);
        TaskmenuBar.getMenus().addAll(menu_main, menu_plan, menu_task);
    }

    public MenuBar getMainMenuBar(){
        return this.MainmenuBar;
    }

    public MenuBar getTaskMenuBar(){
        return this.TaskmenuBar;
    }
}


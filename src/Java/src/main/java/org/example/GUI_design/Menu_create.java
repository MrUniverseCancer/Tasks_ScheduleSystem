package org.example.GUI_design;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class Menu_create {
    private MenuBar MainmenuBar = new MenuBar(); // 主页面返回的菜单栏
    private MenuBar TaskmenuBar = new MenuBar(); // 任务页面返回的菜单栏
    private MenuBar PlanmenuBar = new MenuBar(); // 计划页面返回的菜单栏
    private Main main;

    public Menu_create(Main main){
        this.main = main;
        createMainMenu();
        createTaskMenu();
        createPlanMenu();
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

        // 创建计划菜单和菜单项
        Menu menu_plan = new Menu("计划");
        MenuItem plan_page  = new MenuItem("计划页面");
        MenuItem make_plan     = new MenuItem("制定计划");

        plan_page.setOnAction(e -> {
            main.getMainPage().from_Main_to_Plan();
        });

        // 创建设置菜单和菜单项
        Menu menu_set = getSetUpMenu(0);




        menu_task.getItems().addAll(task_page, history_task);
        menu_plan.getItems().addAll(plan_page, make_plan);
        MainmenuBar.getMenus().addAll(menu_task, menu_plan, menu_set);
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

        plan_page.setOnAction(e -> {
            main.getMainPage().from_Task_to_Plan();
        });

        // 创建任务菜单
        Menu menu_task = new Menu("任务选项");
        MenuItem add_task    = new MenuItem("添加任务");
        MenuItem delete_task = new MenuItem("删除任务");
        MenuItem edit_task   = new MenuItem("编辑任务");
        MenuItem finish_task = new MenuItem("完成任务");
        MenuItem unfinish_task = new MenuItem("未完成任务");
        add_task.setOnAction(e -> {
            Task_add task_add = new Task_add();
            task_add.openWindows_Task_add(main.getMainPage(), 1);
        });
        delete_task.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("删除任务提示");
            alert.setContentText("删除任务和编辑任务请点击具体任务卡片上的按钮");
            alert.showAndWait();
        });
        edit_task.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("编辑任务提示");
            alert.setContentText("编辑任务和删除任务请点击具体任务卡片上的按钮");
            alert.showAndWait();
        });

        // 创建设置菜单和菜单项
        Menu menu_set = getSetUpMenu(1);



        // 创建任务菜单
        menu_main.getItems().addAll(main_return);
        menu_plan.getItems().addAll(plan_page, make_plan);
        menu_task.getItems().addAll(add_task, delete_task, edit_task, finish_task, unfinish_task);
        TaskmenuBar.getMenus().addAll(menu_main, menu_plan, menu_task, menu_set);
    }


    public void createPlanMenu(){
        // 创建任务菜单
        // 创建任务菜单项
        Menu menu_main = new Menu("主页面");
        MenuItem main_return    = new MenuItem("回到主页面");
        main_return.setOnAction(e -> {
            main.getMainPage().from_Plan_to_Main(); // TODO:
        });

        // 创建任务菜单
        // 创建任务菜单项
        Menu menu_task = new Menu("任务");
        MenuItem task_page    = new MenuItem("任务页面");
        MenuItem history_task = new MenuItem("历史任务");

        task_page.setOnAction(e -> {
            main.getMainPage().from_Plan_to_Task();
        });

        // 创建计划菜单和菜单项项
        Menu menu_plan = new Menu("计划");
        MenuItem make_plan     = new MenuItem("制定计划");
        MenuItem plan_page  = new MenuItem("计划页面");
        MenuItem export_plan = new MenuItem("导出计划"); // 导出计划



        menu_plan.getItems().addAll(plan_page, make_plan);
        menu_task.getItems().addAll(task_page, history_task);
        menu_main.getItems().addAll(main_return);
        PlanmenuBar.getMenus().addAll(menu_main, menu_task, menu_plan);
    }


    public Menu getSetUpMenu(int status) {
        // 创建设置菜单和菜单项
        Menu menu_set = new Menu("设置");
        MenuItem set_page = new MenuItem("设置页面");
        set_page.setOnAction(e -> {
            SetUp_Page setUp_page = new SetUp_Page();
            setUp_page.openWindows_Set_UP(main.getMainPage(), status);
        });

        menu_set.getItems().addAll(set_page);
        return menu_set;
    }



    public MenuBar getMainMenuBar(){
        return this.MainmenuBar;
    }

    public MenuBar getTaskMenuBar(){
        return this.TaskmenuBar;
    }
    public MenuBar getPlanMenuBar(){
        return this.PlanmenuBar;
    }
}


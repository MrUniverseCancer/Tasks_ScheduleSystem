package org.example.GUI_design.planPage;

import javafx.scene.layout.Pane;
import org.example.GUI_design.Main;
import org.example.GUI_design.Main_Page;

import java.util.ArrayList;
import java.util.List;

public class plan_page {
    // 设计计划页面，包括计划的制定和查看

    private Main main;
    private Main_Page main_page;
    private Pane planPage;
    private List<String[]> tasks = new ArrayList<String[]>();



    public plan_page(Main main, Main_Page main_page) {
        this.main = main;
        this.main_page = main_page;
        createPlanPage();
    }

    public void createPlanPage(){
        planPage = new Pane();

    }

    public Pane getPlanPage(){
        return planPage;
    }






}

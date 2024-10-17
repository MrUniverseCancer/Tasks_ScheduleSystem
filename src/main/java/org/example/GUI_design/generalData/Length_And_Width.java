package org.example.GUI_design.generalData;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Length_And_Width {
    private double length;
    private double width;

    public Length_And_Width(){
        final double ratio = 0.7; // 初始界面占屏幕的比例
        // 获取主屏幕的屏幕尺寸
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        // 获取屏幕的宽度和高度
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        length = screenWidth * ratio;
        width = screenHeight * ratio;
    }

    public double getLength(){
        return this.length;
    }

    public double getWidth(){
        return this.width;
    }

    public void setLength(double length){
        this.length = length;
    }

    public void setWidth(double width){
        this.width = width;
    }
}

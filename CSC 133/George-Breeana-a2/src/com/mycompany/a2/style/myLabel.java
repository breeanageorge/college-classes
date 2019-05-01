package com.mycompany.a2.style;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Display;
import com.codename1.ui.Label;

public class myLabel extends Label{
    
    public myLabel(String s){
        this.setText(s);
        style();
    }
      
    public void style(){
        int height = Display.getInstance().getDisplayHeight();
        int width = Display.getInstance().getDisplayWidth();
        this.getUnselectedStyle().setFgColor(ColorUtil.BLUE);
        if (height <= 641 && width <= 960)
            this.getAllStyles().setPadding(1, 0, 2, 2);
        else if (height <= 640 && width <= 1136)
            this.getAllStyles().setPadding(1, 0, 2, 2);
        else if (height <= 1136 && width <= 640)
            this.getAllStyles().setPadding(1, 0, 2, 2);
        else
            this.getAllStyles().setPadding(1, 1, 2, 5);
    }
}
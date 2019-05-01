package com.mycompany.a3.gameObjects.interfaces;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public interface ISelectable {
	public void setSelected(boolean selected);
    public boolean isSelected();
    public boolean contains(Point pPtrRelPrnt, Point PCmpRelPrnt);
    public void draw(Graphics g, Point pcmpRelPrnt);
}
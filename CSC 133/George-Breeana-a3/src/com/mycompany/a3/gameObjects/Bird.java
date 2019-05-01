package com.mycompany.a3.gameObjects;
import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.mycompany.a3.gameObjects.interfaces.IChangeable;
import com.mycompany.a3.gameObjects.interfaces.MoveableObject; 

public class Bird extends MoveableObject implements IChangeable{
	private Random randomNum = new Random();
	//Constructor sets birds information Heading, Speed, Location, Size
	public Bird (){
		super.setHeading(randomNum.nextInt(360));
		super.setSpeed(randomNum.nextInt(10 - 1) + 5);
		//super.setLocation(randomNum.nextInt(1000), randomNum.nextInt(1000));
		super.setLocationX(randomNum.nextInt(1000));
		super.setLocationY(randomNum.nextInt(1000));
		super.setSize(randomNum.nextInt(40-1) + 20);
		this.setColor(ColorUtil.BLUE);
	}
	
	@Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
		int xLoc = pCmpRelPrnt.getX() + (int)this.getLocationX()-(super.getSize()/2);
        int yLoc = pCmpRelPrnt.getY() + (int)this.getLocationY()-(super.getSize()/2);
        int x1 = (int)(xLoc - (super.getSize()/2));
        int x2 = (int)(xLoc + (super.getSize()/2));
        int y1 = (int)(yLoc - (super.getSize()/2));
        int y2 = (int)(yLoc + (super.getSize()/2));
        int xArray[] = {x1, x2, xLoc};
        int yArray[] = {y1, y1, y2};
        
        g.setColor(super.getColor());
        g.drawPolygon(xArray, yArray, 3);
        //for debug
        super.setBounds(new Rectangle(xLoc, yLoc, super.getSize(), super.getSize()));
        //g.drawRect(xLoc, yLoc, super.getSize(), super.getSize());
    }
	
	//prints out birds info
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" + " color = " + this.getColor() + " speed = " + this.getSpeed() + " heading = " + this.getHeading() + " Size = " + this.getSize();
    }
	
	public boolean contains(Point pPtrRelPrnt, Point PCmpRelPrnt) {
        int xLoc = pPtrRelPrnt.getX();
        int yLoc = pPtrRelPrnt.getY();
        return super.getBounds().intersects(new Rectangle(xLoc, yLoc, 1, 1));
    }

	public int changeColor() {
		// TODO Auto-generated method stub
		return 0;
	}

}

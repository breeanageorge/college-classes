package com.mycompany.a3.gameObjects;
import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.mycompany.a3.gameObjects.interfaces.FixedObject;
import com.mycompany.a3.gameObjects.interfaces.IChangeable;
import com.mycompany.a3.gameObjects.interfaces.ISelectable;

public class Pylon extends FixedObject implements IChangeable, ISelectable{
	//variables for pylon
	private int radius = 5;
	private int size = 40;
	private int sequenceNumber;
	private Random randomNum = new Random();
	private boolean selected;
	
	//constructor for pylon that sets location, sequence number and color
	public Pylon(int newSeqNum){
		super.setSize(size);
		//this.setLocation(randomNum.nextInt(1000), randomNum.nextInt(1000));
		super.setLocationX(randomNum.nextInt(1000));
		super.setLocationY(randomNum.nextInt(1000));
		this.setSeqNum(newSeqNum);
		super.setColor(ColorUtil.GREEN);
		selected = false;
	}
	
	//set method for sequence number
	public void setSeqNum(int newNum){
		sequenceNumber = newNum;
	}
	
	//get method for sequence number
	public int getSeqNum(){
		return sequenceNumber;
	}
	
	//get method for radius
	public int getRadius(){
		return radius;
	}

	public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" + " color = " + this.getColor() 
        		+ " Radius = " + this.getRadius() + " seqNum =  " + this.getSeqNum();
    }
	
	//overridden method so that pylon does not change color
	@Override
	public int changeColor(){
		return 0;
	}
	
	@Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        int xLoc = pCmpRelPrnt.getX() + (int)this.getLocationX()-(super.getSize()/2);
        int yLoc = pCmpRelPrnt.getY() + (int)this.getLocationY()-(super.getSize()/2);
        int x1 = (int)(xLoc - (super.getSize()/2));
        int x2 = (int)(xLoc + (super.getSize()/2));
        int y1 = (int)(yLoc - (super.getSize()/2));
        int y2 = (int)(yLoc + (super.getSize()/2));
        g.setColor(super.getColor());
        if(selected){
        	g.setColor(ColorUtil.YELLOW);
        }else{
        	g.setColor(super.getColor());
        }
        g.fillTriangle(x1, y1, x2, y1, xLoc, y2);
        g.setColor(ColorUtil.BLACK);
        g.drawString(Integer.toString(this.getSeqNum()), xLoc, yLoc);
        //for debug
        super.setBounds(new Rectangle(xLoc, yLoc, super.getSize(), super.getSize()));
        //g.drawRect(xLoc, yLoc, super.getSize(), super.getSize());
    }
	
	public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean contains(Point pPtrRelPrnt, Point PCmpRelPrnt) {
        int xLoc = pPtrRelPrnt.getX();
        int yLoc = pPtrRelPrnt.getY();
        return super.getBounds().intersects(new Rectangle(xLoc, yLoc, super.getSize(), super.getSize()));
    }
}

package com.mycompany.a3.gameObjects;
import java.util.Random;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.mycompany.a3.gameObjects.interfaces.FixedObject;
import com.mycompany.a3.gameObjects.interfaces.ISelectable;

public class FuelCan extends FixedObject implements ISelectable{
	private int FCsize;
	private Random randomNumbers = new Random();
	private boolean selected;
	private boolean empty;
	
	//constructor sets random location and size
	public FuelCan(){
		//this.setLocation(randomNumbers.nextInt(1000), randomNumbers.nextInt(1000));
		super.setLocationX(randomNumbers.nextInt(1000));
		super.setLocationY(randomNumbers.nextInt(1000));
		int random = randomNumbers.nextInt((60 - 1) + 1) + 40;
		this.setSize(random);
		super.setColor(ColorUtil.MAGENTA);
		selected = false;
		this.setIsEmpty(false);
	}
	
	//set method for fuel can size with random
	@Override
	public void setSize(int newSize){
		FCsize = newSize;
	}
	
	//get method for fuel can size
	@Override
	public int getSize(){
		return FCsize;
	}
	
	//set if fuel can has been used
	public void setIsEmpty(boolean emp){
		empty = emp;
	}
	
	//checks if fuel can has been used
	public boolean getIsEmpty(){
		return empty;
	}
	
	//change color
	@Override
	public int changeColor(){
		if(this.getIsEmpty()){
			this.setColor(ColorUtil.GRAY);
			return ColorUtil.GRAY;
		}else{
			this.setColor(ColorUtil.MAGENTA);
            return ColorUtil.MAGENTA;
        }
    }
	
	
	@Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        int xLoc = pCmpRelPrnt.getX() + (int)this.getLocationX()-(this.getSize()/2);
        int yLoc = pCmpRelPrnt.getY() + (int)this.getLocationY()-(this.getSize()/2);
        g.setColor(super.getColor());
        if(selected){
        	g.setColor(ColorUtil.YELLOW);
        }else{
        	g.setColor(this.getColor());
        }
        g.fillRect(xLoc, yLoc, (this.getSize()/2), (this.getSize()/2));
    	g.setColor(ColorUtil.BLACK);
        g.drawString(Integer.toString(this.getSize()), xLoc, yLoc);
        //for debug
        super.setBounds(new Rectangle(xLoc, yLoc, this.getSize(), this.getSize()));
        //g.drawRect(xLoc, yLoc, super.getSize(), super.getSize());
    }
	
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" + " color = " 
        		+ this.getColor() + " size = " +this.getSize();
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
        return super.getBounds().intersects(new Rectangle(xLoc, yLoc, 1, 1));
    }
}

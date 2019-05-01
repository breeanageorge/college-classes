package com.mycompany.a2.gameObjects;
import com.codename1.charts.util.ColorUtil;
import com.mycompany.a2.gameObjects.interfaces.FixedObject;
import com.mycompany.a2.gameObjects.interfaces.IChangeable;

import java.util.Random;

public class Pylon extends FixedObject implements IChangeable{
	//variables for pylon
	private int radius = 5;
	private int sequenceNumber;
	private Random randomNum = new Random();
	//constructor for pylon that sets location, sequence number and color
	public Pylon(int newSeqNum){
		this.setLocation(randomNum.nextInt(1000), randomNum.nextInt(1000));
		this.setSeqNum(newSeqNum);
		this.setColor(ColorUtil.red(0));
		
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
	public void changeColor(ColorUtil newColor){
		//do nothing
	}
}

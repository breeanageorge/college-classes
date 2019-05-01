package com.mycompany.a2.gameObjects;
import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.mycompany.a2.gameObjects.interfaces.IChangeable;
import com.mycompany.a2.gameObjects.interfaces.MoveableObject; 

public class Bird extends MoveableObject implements IChangeable{
	private int size;	//size of Bird
	private Random randomNum = new Random();
	//Constructor sets birds information Heading, Speed, Location, Size
	public Bird (){
		this.setHeading(randomNum.nextInt(360));
		this.setSpeed(randomNum.nextInt(10 - 1) + 1);
		this.setLocation(randomNum.nextInt(1000), randomNum.nextInt(1000));
		this.setSize(randomNum.nextInt(20-1) + 1);
		this.setColor(ColorUtil.BLUE);
	}
	
	//set method for size
	public void setSize(int newSize){
		size = newSize;
	}
	
	//get method for size
	public int getSize(){
		return size;
	}
	
	//method for changing color 
	@Override
	public void changeColor(ColorUtil newColor){
		//do nothing
	}
	
	//prints out birds info
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" + " color = " + this.getColor() + " speed = " + this.getSpeed() + " heading = " + this.getHeading() + " Size = " + this.getSize();
    }
}

package com.mycompany.a2.gameObjects;
import java.util.Random;

import com.mycompany.a2.gameObjects.interfaces.FixedObject;

public class FuelCan extends FixedObject{
	private int size;
	private Random randomNumbers = new Random();
	
	//constructor sets random location and size
	public FuelCan(){
		this.setLocation(randomNumbers.nextInt(1000), randomNumbers.nextInt(1000));
		this.setSize();
	}
	
	//set method for fuel can size with random
	public void setSize(){
		size = randomNumbers.nextInt(9)+1;
	}
	
	//get method for fuel can size
	public int getSize(){
		return size;
	}
	
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" + " color = " 
        		+ this.getColor() + " size = " +this.getSize();
    }
}

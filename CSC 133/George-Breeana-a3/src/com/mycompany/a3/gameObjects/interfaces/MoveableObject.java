package com.mycompany.a3.gameObjects.interfaces;
import java.util.Random;

import com.mycompany.a3.game.GameWorld;
import com.mycompany.a3.gameObjects.GameObject;

public abstract class MoveableObject extends GameObject{
	private GameWorld gw;
	private int heading;
	private int speed;
	private int newLocationX;
	private int newLocationY;
	
	public MoveableObject(){
		heading = 0;
	}

	//makes object move based on formula given
	public void move(int objSpeed, GameWorld gameWorld){
		this.gw = gameWorld;
		newLocationX = (int)(super.getLocationX() + Math.cos(Math.toRadians(90 - heading))*objSpeed);
		newLocationY = (int)(super.getLocationY() + Math.sin(Math.toRadians(90 - heading))*objSpeed);
		super.setLocationX(newLocationX);
		super.setLocationY(newLocationY);
		if(super.getLocationX() >= gw.getWidth()){
            super.setLocationX(gw.getWidth()-(super.getSize()/2)-15);
        }else if(super.getLocationX() <= 0){
             super.setLocationX((super.getSize()/2)+15);
        }
        if(super.getLocationY() >= gw.getHeight()){
            super.setLocationY(gw.getHeight()-(super.getSize()/2)-15);
        }else if(super.getLocationY() <= 0){
             super.setLocationY((super.getSize()/2)+15);
        }
        if(super.getLocationX() >= gw.getWidth()-(super.getSize()/2) || super.getLocationX() <= (super.getSize()/2) 
                || super.getLocationY() >= gw.getHeight()-(super.getSize()/2) || super.getLocationY() <= (super.getSize()/2)){
            setHeading(getHeading()-185);
        }else{
            Random r = new Random();
            int flag = r.nextInt(1);
            int move = r.nextInt(500);
            if(move < 20){
                if(flag == 1){
                    setHeading(getHeading()-5);
                }else{
                    setHeading(getHeading()+5);
                }
            }
        }
	}
	
	//get method for heading
	public int getHeading(){
		return heading;
	}
	
	//set method for changing heading
	public void setHeading(int newHeading){
		heading = newHeading;
	}
	
	//get method for heading
	public int getSpeed(){
		return speed;
	}
	
	//set method for changing heading
	public void setSpeed(int newSpeed){
		speed = newSpeed;
	}

	public void changeHeading(int newHeading) {
		// TODO Auto-generated method stub
		
	}
}

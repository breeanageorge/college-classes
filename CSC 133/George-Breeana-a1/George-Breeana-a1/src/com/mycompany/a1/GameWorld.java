package com.mycompany.a1;
import java.util.ArrayList;
import java.util.Random;

import com.codename1.charts.util.ColorUtil;

public class GameWorld{
	private int clockTime;	
	private int lives;		
	private int lastPylon;	
	private int fuelLevel;
	private int damageLevel;
	private Random randomNum = new Random();
	private ArrayList<Object> objects = new ArrayList<Object>();
	
	public void init(){
		//Sets Variables
		clockTime = 0;
		lives = 3;
		fuelLevel = 100;
		lastPylon = 1;
		
		//adds objects to arraylist 
		objects.add(new Pylon(1));		//instantiates Pylon Class
		objects.add(new Pylon(2));		//instantiates Pylon Class
		objects.add(new Pylon(3));		//instantiates Pylon Class
		objects.add(new Pylon(4));		//instantiates Pylon Class
		objects.add(new FuelCan());	    //instantiates Fuel_Can Class
		objects.add(new FuelCan());	    //instantiates Fuel_Can Class
		objects.add(new Bird());		//instantiates Bird Class
		objects.add(new Bird());		//instantiates Bird Class

		carLocation();			//instantiates Car and places car at pylon 1
	}
	
	//method for setting the car location at pylon 1
	public void carLocation(){
		for (int i = 0; i < objectList().size(); i++){		
			if(objectList().get(i) instanceof Pylon){
				Pylon p = (Pylon)objectList().get(i);
				if(p.getSeqNum() == 1){
					objects.add(new Car(p.getLocationX(), p.getLocationY()));	//instantiates Car
				}
			}
		}
	}

	// Sets the Clock Time after each tick and moves moveable objects
	public void setClockTime(int newClockTime){
		clockTime = newClockTime;
		moveObj();
	}
	//Get Clock Time
	public int getClockTime(){
		return clockTime;
	}
	//Set Lives
	public void setLives(int newLives){
		lives = newLives;
	}
	
	//Get Lives
	public int getLives(){
		return lives;
	}
	//Set Pylon
	public void setLastPylon(int newPylon){
		lastPylon = newPylon;
	}
	//Get Pylon
	public int getLastPylon(){
		return lastPylon;
	}
	//Set Fuel Level
	public void setFuelLevel(int newFuel){
		fuelLevel = newFuel;
	}
	//Get Fuel Level
	public int getFuelLevel(){
		return fuelLevel;
	}
	//Set Damage Level
		public void setDamageLevel(int newDamage){
			damageLevel = newDamage;
		}
	//Get damage level
	public int getDamageLevel(){
		return damageLevel;
	}
	//Returns Array of objects
	public ArrayList<Object> objectList(){
		return objects;
	}
	
	//changes color of all game objects
	public void colorChange(){
		for (int i = 0; i < objectList().size(); i++){		
			if(objectList().get(i) instanceof GameObject){
				GameObject o = (GameObject)objectList().get(i);
				int myColor = ColorUtil.rgb(randomNum.nextInt(255),randomNum.nextInt(255),randomNum.nextInt(255));
				o.changeColor(myColor);				
			}
		}
	}
	
	//code for accelerating or decelerating
	public void changeSpeed(int changeSpeed){
		for (int i = 0; i < objectList().size(); i++){		
			if(objectList().get(i) instanceof Car){
				Car c = (Car)objectList().get(i);
				
			}
		}
	}
	
	//method for changing direction of steering wheel
	public void changeWheel(int newDir){
		for (int i = 0; i < objectList().size(); i++){		
			if(objectList().get(i) instanceof Car){
				Car c = (Car)objectList().get(i);
				c.setSteering(newDir);
			}
		}
	}
	
	//Moves objects
	public void moveObj(){
		//makes all moveable objects move
		for (int i = 0; i < objectList().size(); i++){		
			if(objectList().get(i) instanceof MoveableObject){
				if(objectList().get(i) instanceof Car){
					Car c = (Car)objectList().get(i);
				}
				else{
					MoveableObject o = (MoveableObject)objectList().get(i);
					o.move(o.getSpeed());
				}
			}
		}
	}
	
	//change heading of Car
	public void changeHeading(int newHeading){
		for (int i = 0; i < objectList().size(); i++){	
			if(objectList().get(i) instanceof Car){
				Car car = (Car)objectList().get(i);
				car.changeHeading(newHeading);
			}
		}
	}

	//method for colliding with Pylon object
	public void collisionPylon(int pylonNumber){
		if(this.getLastPylon() == (pylonNumber - 1)){
			this.setLastPylon(pylonNumber);
		}
	}
	
	//method for colliding with Fuel Can object
	public void collisionFuel(){
		//removes fuel after setting fuel level
		for (int i = 0; i < objectList().size(); i++){
			if(objectList().get(i) instanceof FuelCan){
				FuelCan fuel = (FuelCan)objectList().get(i);
				setFuelLevel(this.getFuelLevel() + fuel.getSize());
				objectList().remove(fuel);
				break;
			}
		}
		
		//sets fuel level of car
		for (int i = 0; i < objectList().size(); i++){
			if(objectList().get(i) instanceof Car){
				Car c = (Car)objectList().get(i);
				c.setFuelLevel(this.getFuelLevel());
				break;
			}
		}
		objectList().add(new FuelCan());
	}
	
	//method for colliding with another car
	public void collisionCar(){
		for (int i = 0; i < objectList().size(); i++){
			if(objectList().get(i) instanceof Car){
				Car c = (Car)objectList().get(i);
				c.setDamageLevel(c.getDamageLevel() + 50);
				this.setDamageLevel(c.getDamageLevel());
				if(c.getDamageLevel() >= 100){
					setLives(getLives()-1);
					c.setDamageLevel(0);
					this.setDamageLevel(c.getDamageLevel());
				}
			}
		}	
	}
	
	//method for colliding with Bird
	public void collisionBird(){
		for (int i = 0; i < objectList().size(); i++){
			if(objectList().get(i) instanceof Car){
				Car c = (Car)objectList().get(i);
				c.setDamageLevel(c.getDamageLevel() + 25);
				this.setDamageLevel(c.getDamageLevel());
				if(c.getDamageLevel() >= 100){
					setLives(getLives()-1);
					c.setDamageLevel(0);
					this.setDamageLevel(c.getDamageLevel());
				}
			}
		}	
	}
	
	//Displays Map
	public void map(){
		for(int i = 0; i < objects.size(); i++){	
			System.out.println(objects.get(i).toString());
		}
	}
}

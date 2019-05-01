package com.mycompany.a3.game;
import java.util.Observable;
import java.util.Random;

import com.mycompany.a3.gameObjectCollection.GameObjectCollection;
import com.mycompany.a3.gameObjects.Bird;
import com.mycompany.a3.gameObjects.Car;
import com.mycompany.a3.gameObjects.FuelCan;
import com.mycompany.a3.gameObjects.GameObject;
import com.mycompany.a3.gameObjects.NonPlayerCar;
import com.mycompany.a3.gameObjects.Pylon;
import com.mycompany.a3.gameObjects.interfaces.ICollider;
import com.mycompany.a3.gameObjects.interfaces.IIterator;
import com.mycompany.a3.gameObjects.interfaces.ISelectable;
import com.mycompany.a3.gameObjects.interfaces.MoveableObject;
import com.mycompany.a3.sound.BgSound;
import com.mycompany.a3.sound.Sounds;
import com.mycompany.a3.strategy.DemolitionDerbyStrategy;
import com.mycompany.a3.strategy.PylonStrategy;

public class GameWorld extends Observable{
	private int width;
	private int height;
	private int x;
	private int y;
	private int clockTime;
	private int tick;
	private int lives;		
	private int lastPylon;
	private int pylonTotal;
	private double fuelLevel;
	private int steeringDir;
	private int damageLevel;
	private int heading;
	private boolean wasFlipped;
	private boolean sound;
	private boolean paused;
	private boolean soundFlag;
	private BgSound theme;
	public Sounds carCrash;
	public Sounds birdCrash;
	public Sounds fuelSlurp;
	public Sounds lifeLost;
	public Sounds gameOver;
	public Sounds[] allSound;
	
	private Random rand = new Random();
	public GameObjectCollection gameObjectsList;
	
	public void init(){
		//Sets Variables
		gameObjectsList = new GameObjectCollection();
		clockTime = 0;
		lives = 3;
		fuelLevel = 100;
		lastPylon = 1;
		sound = true;
		paused = false;
		wasFlipped = true;
		/*
		 * theme.wav is Delfino Plaza by Koji Kondo 
		 * I do not own the this song or any other
		 * sounds associated with this game.
		 */
		theme = new BgSound("theme.wav");
		carCrash = new Sounds("crash.wav");
		birdCrash = new Sounds("birb.wav");
		fuelSlurp = new Sounds("slurp.wav");
		lifeLost = new Sounds("lifeLost.wav");
		gameOver = new Sounds("gameOver.wav");
		allSound = new Sounds[]{carCrash, birdCrash, fuelSlurp, lifeLost, gameOver};
		setChanged();
        notifyObservers();
		
		//Construct Pylons
		pylonTotal = 4 + rand.nextInt((5 - 1) + 1) + 1;
		Pylon startPylon = new Pylon(1);
		gameObjectsList.add(startPylon);
		for(int i=2; i < pylonTotal; i++){
			gameObjectsList.add(new Pylon(i));
		}
		
		//Construct Fuel Can
		for(int i=0; i < 2; i++){
			gameObjectsList.add(new FuelCan());
		}
		
		//Construct Birds
		for(int i=0; i < 3; i++){
			gameObjectsList.add(new Bird());
		}
		
		//create Player Car
		gameObjectsList.add(new Car(startPylon.getLocationX(), startPylon.getLocationY()));
		
		//Create 3 NPCs with strategy
		NonPlayerCar NPC1 = new NonPlayerCar((double)(startPylon.getLocationX() + 100), (double)(startPylon.getLocationY() + 100));
		NPC1.setStrategy(new DemolitionDerbyStrategy(NPC1, gameObjectsList));
		gameObjectsList.add(NPC1);
		NonPlayerCar NPC2 = new NonPlayerCar((double)(startPylon.getLocationX() + 200), (double)(startPylon.getLocationY() + 200));
		NPC2.setStrategy(new PylonStrategy(NPC2, gameObjectsList));
		gameObjectsList.add(NPC2);
		NonPlayerCar NPC3 = new NonPlayerCar((double)(startPylon.getLocationX() + 300), (double)(startPylon.getLocationY() + 300));
		NPC3.setStrategy(new DemolitionDerbyStrategy(NPC3, gameObjectsList));
		gameObjectsList.add(NPC3);
		
		theme.play();
	}

	// Sets the Clock Time after each tick and moves moveable objects
	public void setClockTime(){
		tick = tick +1;
		if(tick == 20){
			clockTime = clockTime + 1;
			tick = 0;
		}
		moveObj();
		collision();
		changeHeading();
		setChanged();
        notifyObservers();
	}
	//Get Clock Time
	public int getClockTime(){
		return (int) clockTime;
	}
	// Sets map width variable
	public void setWidth(int newWidth){
		width = newWidth;
	}
	//Get map width
	public int getWidth(){
		return width;
	}
	// Sets map height variable
	public void setHeight(int newHeight){
		height = newHeight;
	}
	//Get map height
	public int getHeight(){
		return height;
	}
	// Sets map X
	public void setOrginX(int newX){
		x = newX;
	}
	//Get map X
	public int getOrginX(){
		return x;
	}
	// Sets map Y
	public void setOrginY(int newY){
		y = newY;
	}
	//Get map Y
	public int getOrginY(){
		return y;
	}
	//set paused    
    public void setPaused(boolean newPaused) {
        paused = newPaused;
        setChanged();
        notifyObservers();
    }
	//get paused
	public boolean getPaused() {
        return paused;
    }    
	//Set Lives
	public void setLives(int newLives){
		lives = newLives;
	}
	//Get Lives
	public int getLives(){
		return lives;
	}
	//get method for steering direction
	public int getSteering(){
		return steeringDir;
	}
	//Set Pylon
	public void setLastPylon(int newPylon){
		lastPylon = newPylon;
	}
	//Get Pylon
	public int getLastPylon(){
		return lastPylon;
	}
	//Get Pylon Total
	public int getPylonTotal(){
		return pylonTotal;
	}
	//Set Fuel Level
	public void setFuelLevel(double newFuel){
		fuelLevel = newFuel;
	}
	//Get Fuel Level
	public double getFuelLevel(){
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
	//Set Heading
	public void setHeading(int newHeading){
		heading = newHeading;
	}
	//Get heading
	public int getHeading(){
		return heading;
	}
	//keeps track of sound state
	public boolean getWasFlipped() {
		return wasFlipped;
	}
	//keeps track of sound state
	public void setWasFlipped(boolean wasFlipped) {
        this.wasFlipped = wasFlipped;
        setChanged();
        notifyObservers();
    } 
	//Returns Array of objects
	public GameObjectCollection objectList(){
		return gameObjectsList;
	}
	//
	public IIterator getCollectionIterator(){
		return gameObjectsList.getIterator();
	}
	//Set sound Flag
	public void setSoundFlag(boolean newSoundFlag){
		soundFlag = newSoundFlag;
	}
	
	//Set sound
	public void setSound(){
        if(getPaused()){
        	theme.pause();
        	for (Sounds s : allSound) {
                s.pause();
            }
        }else{
			if (soundFlag){
	        	theme.play();
	        	this.sound = true;
	        }else{    
	        	theme.pause();
	        	this.sound = false;
	        	for (Sounds s : allSound) {
	                s.pause();
	            }
	        }    
	        setChanged();
	        notifyObservers();
        }    
	}
	
	//Get sound
	public boolean getSound(){
		return sound;
	}
	
	//changes color of all game objects
	public void changeColor(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof GameObject) {
				((GameObject) currentObject).changeColor();
			}
		}
	}
	
	//code for accelerating or decelerating
	public void changeSpeed(int changeSpeed){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				((Car) currentObject).changeSpeed(changeSpeed + ((Car) currentObject).getSpeed());
			}
		}
	}
	
	//method for changing direction of steering wheel
	public void changeWheel(int newDir){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				((Car) currentObject).setSteering(newDir);
			}
		}
	}
		
	//change heading of Car
	public void changeHeading(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car) {
				((Car) currentObject).setHeading(((Car) currentObject).getHeading() % 360 +((Car) currentObject).getSteering());
			}
		}
	}

	//changes strategy of NPCs
	public void changeStrategy(GameObjectCollection gameObjectsList){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof NonPlayerCar) {
				if(((NonPlayerCar) currentObject).getStrategy() instanceof PylonStrategy){
					((NonPlayerCar) currentObject).setStrategy(new DemolitionDerbyStrategy((NonPlayerCar) currentObject, gameObjectsList));
				}else{
					((NonPlayerCar) currentObject).setStrategy(new PylonStrategy((NonPlayerCar) currentObject, gameObjectsList));
				}
			}
		}
	}
	
	//method for changing position of selectable objects
	public void changePosition(double newLocationX, double newLocationY){
		newLocationX = newLocationX - getOrginX();
		newLocationY = newLocationY - getOrginY();

		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof ISelectable) {
				if(((ISelectable) currentObject).isSelected()){
					((GameObject) currentObject).setLocationX(newLocationX);
					((GameObject) currentObject).setLocationY(newLocationY);
					setChanged();
					notifyObservers();
				}
			}
		}
	}
	
	//method for consuming fuel
	public void fuelConsume(){		
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		double fuelMinus = 0;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				fuelMinus = ((Car) currentObject).getFuelLevel() - (((Car) currentObject).getSpeed()/20);
				((Car) currentObject).setFuelLevel(fuelMinus);
				setFuelLevel(fuelMinus);
				if(((Car) currentObject).getFuelLevel() <= 0){
					setLives(getLives()-1);
					if (sound) lifeLost.play();
					((Car) currentObject).setFuelLevel(100);
					setFuelLevel(100);
				}
			}
		}
	}
	
	//Moves objects
	public void moveObj(){
		//makes all moveable objects move
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof MoveableObject) {
				if(currentObject instanceof Car){	
					if(!(currentObject instanceof NonPlayerCar)){
						((Car) currentObject).move(((Car) currentObject).getSpeed(), (GameWorld) this);
						fuelConsume();
					}else if(currentObject instanceof NonPlayerCar){
						((NonPlayerCar) currentObject).invokeStrategy();
						((NonPlayerCar) currentObject).move(((NonPlayerCar) currentObject).getSpeed(), (GameWorld) this);
					}
				}else{
					((MoveableObject) currentObject).move(((MoveableObject) currentObject).getSpeed(), (GameWorld) this);
				}
			}
		}
	}
	
	public void collision(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		Object currentObject2;
		while(iterator.hasNext()) {
			IIterator it = gameObjectsList.getIterator();
			currentObject = iterator.getNext();
			if (currentObject instanceof ICollider) {
				while(it.hasNext()) {
					currentObject2 = it.getNext();
					if(currentObject2 != currentObject){	
						if(((Car) currentObject).collidesWith(currentObject2)){
							if(!((Car) currentObject).getList().contains(currentObject2)){
								((Car) currentObject).handleCollision(currentObject2, (GameWorld) this);
								((Car) currentObject).getList().add((GameObject) currentObject2);
								if(currentObject2 instanceof NonPlayerCar){
									((Car) currentObject2).handleCollision(currentObject, (GameWorld) this);
									((Car) currentObject2).getList().add((GameObject) currentObject);
								}
							}	
						}else{
							if(((Car) currentObject).getList().contains(currentObject2)){
								((Car) currentObject).getList().remove(currentObject2);
								if(currentObject2 instanceof NonPlayerCar){
									((Car) currentObject2).getList().remove(currentObject);
								}
							}
						}
					}	
				}
			}
		}	
	}
	
	//Displays Map
	public void map(){
		IIterator iterator = gameObjectsList.getIterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.getNext().toString());
		}
		System.out.println(" ");
	}

}

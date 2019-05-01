package com.mycompany.a2.game;
import java.util.Observable;
import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.mycompany.a2.gameObjectCollection.GameObjectCollection;
import com.mycompany.a2.gameObjects.*;
import com.mycompany.a2.gameObjects.interfaces.IIterator;
import com.mycompany.a2.gameObjects.interfaces.MoveableObject;
import com.mycompany.a2.strategy.DemolitionDerbyStrategy;
import com.mycompany.a2.strategy.PylonStrategy;

public class GameWorld extends Observable{
	private int clockTime;	
	private int lives;		
	private int lastPylon;
	private int pylonTotal;
	private int fuelLevel;
	private int steeringDir;
	private int damageLevel;
	private int heading;
	private boolean sound;
	private int recentPylonNPC;
	
	private Random rand = new Random();
	private GameObjectCollection gameObjectsList;
	
	public void init(){
		//Sets Variables
		gameObjectsList = new GameObjectCollection();
		clockTime = 0;
		lives = 3;
		fuelLevel = 100;
		lastPylon = 1;
		sound = true;
		recentPylonNPC = 1;
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
		NonPlayerCar NPC1 = new NonPlayerCar(startPylon.getLocationX() - 10, startPylon.getLocationY() - 10);
		NPC1.setStrategy(new DemolitionDerbyStrategy(NPC1, gameObjectsList));
		gameObjectsList.add(NPC1);
		NonPlayerCar NPC2 = new NonPlayerCar(startPylon.getLocationX() - 20, startPylon.getLocationY() - 20);
		NPC2.setStrategy(new PylonStrategy(NPC2, gameObjectsList));
		gameObjectsList.add(NPC2);
		NonPlayerCar NPC3 = new NonPlayerCar(startPylon.getLocationX() - 30, startPylon.getLocationY() - 30);
		NPC3.setStrategy(new DemolitionDerbyStrategy(NPC3, gameObjectsList));
		gameObjectsList.add(NPC3);
	}

	// Sets the Clock Time after each tick and moves moveable objects
	public void setClockTime(int newClockTime){
		clockTime = newClockTime;
		fuelConsume();
		changeHeading();
		moveObj();
		setChanged();
        notifyObservers();
        
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
	//Set Lives
	public void setRecentPylonNPC(int pylonTotal){
		recentPylonNPC = rand.nextInt((pylonTotal - 1) + 1) + 1;
	}
	//Get Lives
	public int getRecentPylonNPC(){
		return recentPylonNPC;
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
	//Set Heading
	public void setHeading(int newHeading){
		heading = newHeading;
	}
	//Get heading
	public int getHeading(){
		return heading;
	}
	//Set sound
	public void setSound(boolean newSound){
		sound = newSound;
		setChanged();
        notifyObservers();
	}
	//Get sound
	public boolean getSound(){
		return sound;
	}
	//Returns Array of objects
	public GameObjectCollection objectList(){
		return gameObjectsList;
	}
	
	//changes color of all game objects
	public void colorChange(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof GameObject) {
				int myColor = ColorUtil.rgb(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
				((MoveableObject) currentObject).changeColor(myColor);
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
	
	//code for accelerating or decelerating
	public void changeSpeed(int changeSpeed){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				((Car) getPlayerCar()).changeSpeed(changeSpeed + ((Car) getPlayerCar()).getSpeed());
			}else if(currentObject instanceof NonPlayerCar){
				((NonPlayerCar) currentObject).changeSpeed(changeSpeed + ((NonPlayerCar) currentObject).getSpeed());
			}
		}
	}
	
	//method for changing direction of steering wheel
	public void changeWheel(int newDir){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car) {
				((Car) getPlayerCar()).setSteering(newDir);
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
				if(currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)){
					((Car) getPlayerCar()).move(((Car) getPlayerCar()).getSpeed());
				}else if(currentObject instanceof NonPlayerCar){
					//((NonPlayerCar) currentObject).invokeStrategy();
					((NonPlayerCar) currentObject).move(((NonPlayerCar) currentObject).getSpeed());
					if(((NonPlayerCar) currentObject).getLocationX() == ((Car) getPlayerCar()).getLocationX()){
						if(((NonPlayerCar) currentObject).getLocationY() == ((Car) getPlayerCar()).getLocationY()){
							collisionCar();
						}
					}
				}
				else{
					((MoveableObject) currentObject).move(((MoveableObject) currentObject).getSpeed());
				}
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
				((Car) getPlayerCar()).setHeading(((Car) getPlayerCar()).getHeading() % 360 +((Car) getPlayerCar()).getSteering());
			}
		}
	}

	//method for colliding with Pylon object
	public Object getPlayerCar(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				return currentObject;
			}
		}
		return null;
	}
	
	//method for colliding with Pylon object
		public void collisionPylon(int pylonNumber){
			IIterator iterator = gameObjectsList.getIterator();
			Object currentObject;
			
			while(iterator.hasNext()) {
				currentObject = iterator.getNext();
				if (currentObject instanceof Pylon) {
					if(getLastPylon() == (pylonNumber - 1)){
						setLastPylon(pylonNumber);
					}
				}
			}
		}
	
	//method for colliding with Fuel Can object
	public void collisionFuel(){
		//removes fuel after setting fuel level
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		int newFuel = 0;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof FuelCan) {
				newFuel = ((FuelCan) currentObject).getSize();
				setFuelLevel(((Car) getPlayerCar()).getFuelLevel() + newFuel);
				((Car) getPlayerCar()).setFuelLevel(((Car) getPlayerCar()).getFuelLevel() + newFuel);
			}
		}	
		
		gameObjectsList.add(new FuelCan());
	}
		
	//method for consuming fuel
	public void fuelConsume(){		
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		int fuelMinus = 0;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				fuelMinus = ((Car) getPlayerCar()).getFuelLevel() - (((Car) getPlayerCar()).getSpeed()/5);
				((Car) getPlayerCar()).setFuelLevel(fuelMinus);
				setFuelLevel(fuelMinus);
				if(((Car) getPlayerCar()).getFuelLevel() <= 0){
					setLives(getLives()-1);
					((Car) getPlayerCar()).setFuelLevel(100);
					setFuelLevel(100);
				}
			}
		}
	}
	
	//method for colliding with another car
	public void collisionCar(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		int newDamage = 0;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof NonPlayerCar) {
				newDamage = ((Car) getPlayerCar()).getDamageLevel() + 50;
				if(newDamage >= 100){
					setLives(getLives()-1);
					newDamage = 0;
				}
				((Car) getPlayerCar()).setDamageLevel(newDamage);
				setDamageLevel(newDamage);
				break;
			}
		}
	}
	
	//method for colliding with Bird
	public void collisionBird(){
		IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		int newDamage = 0;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Bird){
				newDamage = ((Car) getPlayerCar()).getDamageLevel() + 25;
				if(newDamage >= 100){
					setLives(getLives()-1);
					newDamage = 0;
				}
				((Car) getPlayerCar()).setDamageLevel(newDamage);
				setDamageLevel(newDamage);
				break;
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

package com.mycompany.a3.gameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Rectangle;
import com.mycompany.a3.game.GameWorld;
import com.mycompany.a3.gameObjectCollection.GameObjectCollection;
import com.mycompany.a3.gameObjects.interfaces.IStrategy;
import com.mycompany.a3.strategy.DemolitionDerbyStrategy;
import com.mycompany.a3.strategy.PylonStrategy;

public class NonPlayerCar extends Car{
	protected IStrategy curStrategy;
	private int maxSpeedNPC = 30;
	private int damageLevelNPC = 0;
	private int size = 60;
	private int currentPylon;
	
	//Constructor sets heading, speed, location
	public NonPlayerCar(double x, double y){
		super(x,y);
		super.setSize(size);
		super.setColor(ColorUtil.rgb(255, 0, 0));
		this.setHeading(0);
		this.setSpeed(10);
		super.setLocationX(x);
		super.setLocationY(y);
	}
	
	public void setPylonNum(int newPylon) {
		currentPylon = newPylon;
	}
	
	public int getPylonNum() {
		return currentPylon;
	}
	//set method for damage level
	@Override
	public void setDamageLevel(int newDamage){
		damageLevelNPC = (int) (newDamage * .75);
		maxSpeedNPC = (int)(maxSpeedNPC * (1 - (damageLevelNPC * .001)));		//adjusts max speed based off damage level
		if(getSpeed() > maxSpeedNPC)		//if current speed is greater than max speed
			changeSpeed(maxSpeedNPC);		//change speed to max speed
		
	}
	
	//get method for max speed
	@Override
	public int getMaxSpeed(){
		return maxSpeedNPC;
	}

	@Override
	public void handleCollision(Object otherObject, GameWorld gw) {
    	int newDamage = 0;
    	if(otherObject instanceof Pylon){
        	if(this.getPylonNum() == (((Pylon) otherObject).getSeqNum() - 1)){
				this.setPylonNum(((Pylon) otherObject).getSeqNum());
        	}	
        	
        }else if(otherObject instanceof FuelCan){
        	double newFuel = 0;
        	if(!(((FuelCan) otherObject).getIsEmpty())){
				newFuel = ((FuelCan) otherObject).getSize();
				newFuel = newFuel + this.getFuelLevel();
				this.setFuelLevel(newFuel);
				((FuelCan) otherObject).setIsEmpty(true);
				((FuelCan) otherObject).changeColor();
	    		gw.gameObjectsList.add(new FuelCan());
        	}
        }else if(otherObject instanceof Bird){
			newDamage = this.getDamageLevel() + 25;
			this.setDamageLevel(newDamage);
			
        }else if(otherObject instanceof Car){
			newDamage = this.getDamageLevel() + 50;
			this.setDamageLevel(newDamage);
        }    
    }
	
	//sets strategy
	public void setStrategy(IStrategy strategy){
		curStrategy = strategy;
			
	}

	//gets current strategy
	public IStrategy getStrategy(){
		return this.curStrategy;
		
	}
	
	//invokes strategy
	public void invokeStrategy(){
		curStrategy.invokeStrategy();
		
	}
	
	public void changeStrategy(GameObjectCollection gameObjectsList) {
        if (getStrategy() instanceof DemolitionDerbyStrategy){
            setStrategy(new PylonStrategy(this, gameObjectsList));
        }else if (getStrategy() instanceof PylonStrategy){
            setStrategy(new DemolitionDerbyStrategy(this, gameObjectsList));
        }
    }

	
	@Override
    public String toString(){
        return this.getClass() + " loc = (" + this.getLocationX() + ", " + this.getLocationY() + ")" 
        		+ " color = " + this.getColor() + " heading = " + this.getHeading() + " speed = " + this.getSpeed() 
        		+ " width = " + this.getWidth() + " length = " + this.getLength() + " maxSpeed = " + this.getMaxSpeed() 
        		+ " Current Strategy: " + this.getStrategy()
        		+ " Damage Level: " + this.getDamageLevel()+ " Recent Pylon: " + this.getPylonNum();
    }
	
	@Override
    public void draw(Graphics g, Point pCmpRelPrnt) {
        int xLoc = pCmpRelPrnt.getX() + (int)this.getLocationX()-(super.getSize()/2);
        int yLoc = pCmpRelPrnt.getY() + (int)this.getLocationY()-(super.getSize()/2);
        g.setColor(super.getColor());
        g.drawArc(xLoc, yLoc, super.getSize(), super.getSize(), 0, 360);
        //for debug
        super.setBounds(new Rectangle(xLoc, yLoc, super.getSize(), super.getSize()));
        //g.drawRect(xLoc, yLoc, super.getSize(), super.getSize());
    }

    public boolean contains(Point pPtrRelPrnt, Point PCmpRelPrnt) {
        int xLoc = pPtrRelPrnt.getX();
        int yLoc = pPtrRelPrnt.getY();
        return super.getBounds().intersects(new Rectangle(xLoc, yLoc, 1, 1));
    }
}

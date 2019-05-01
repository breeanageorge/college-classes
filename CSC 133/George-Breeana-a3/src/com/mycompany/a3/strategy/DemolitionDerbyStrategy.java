package com.mycompany.a3.strategy;

import com.codename1.util.MathUtil;
import com.mycompany.a3.gameObjectCollection.GameObjectCollection;
import com.mycompany.a3.gameObjects.Car;
import com.mycompany.a3.gameObjects.NonPlayerCar;
import com.mycompany.a3.gameObjects.interfaces.IIterator;
import com.mycompany.a3.gameObjects.interfaces.IStrategy;

public class DemolitionDerbyStrategy implements IStrategy {
    private NonPlayerCar NPC;
    private Car player;

    public DemolitionDerbyStrategy(NonPlayerCar npCar, GameObjectCollection gameObjectsList) {
        NPC = npCar;
         
        IIterator iterator = gameObjectsList.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				player = (Car) currentObject;
			}
		}
    }

    public void invokeStrategy() {
        double dy = NPC.getLocationY() - player.getLocationY();
        double dx = player.getLocationX() - NPC.getLocationX();
        
        NPC.setHeading((int) Math.toDegrees(MathUtil.atan2(dy, dx)));
    }

	public void setStrategy() {
		// TODO Auto-generated method stub
		
	}

}
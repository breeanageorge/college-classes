package com.mycompany.a2.strategy;

import com.codename1.util.MathUtil;
import com.mycompany.a2.gameObjectCollection.GameObjectCollection;
import com.mycompany.a2.gameObjects.Car;
import com.mycompany.a2.gameObjects.NonPlayerCar;
import com.mycompany.a2.gameObjects.Pylon;
import com.mycompany.a2.gameObjects.interfaces.IIterator;
import com.mycompany.a2.gameObjects.interfaces.IStrategy;

public class PylonStrategy implements IStrategy {
    private GameObjectCollection gameObjectsListNPC;
    private NonPlayerCar NPC;
    private Pylon pylon;

    public PylonStrategy(NonPlayerCar npCar, GameObjectCollection gameObjectsList) {
        NPC = npCar;
        npCar.getRecentPylon();
        gameObjectsListNPC = gameObjectsList;
        
    }

    private void getPylon() {
        IIterator iterator = gameObjectsListNPC.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Pylon) {
				if(((Pylon) currentObject).getSeqNum() == (NPC.getRecentPylon() +1)){
					pylon = (Pylon) currentObject;
				}
			}
		}
    }

    private double getPlayerSpeed() {
        double speed = 0;
        IIterator iterator = gameObjectsListNPC.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Car && !(currentObject instanceof NonPlayerCar)) {
				speed = ((Car) currentObject).getSpeed();
			}
		}
        
        return speed;
    }

    public double invokeStrategy() {
        getPylon();
        NPC.setRecentPylon(pylon.getSeqNum());
        
        double dy = NPC.getLocationY() - pylon.getLocationY();
        double dx = NPC.getLocationX() - pylon.getLocationX();

        double newHeading = Math.toDegrees(MathUtil.atan2(dy, dx));
        NPC.setHeading((int) newHeading);

        return getPlayerSpeed() / 100;
    }

	public void setStrategy() {
		// TODO Auto-generated method stub
		
	}

}
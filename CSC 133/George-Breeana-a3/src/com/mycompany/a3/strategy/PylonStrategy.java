package com.mycompany.a3.strategy;

import com.codename1.util.MathUtil;
import com.mycompany.a3.gameObjectCollection.GameObjectCollection;
import com.mycompany.a3.gameObjects.NonPlayerCar;
import com.mycompany.a3.gameObjects.Pylon;
import com.mycompany.a3.gameObjects.interfaces.IIterator;
import com.mycompany.a3.gameObjects.interfaces.IStrategy;

public class PylonStrategy implements IStrategy {
    private GameObjectCollection gameObjectsListNPC;
    private NonPlayerCar NPC;
    private Pylon pylon;

    public PylonStrategy(NonPlayerCar npCar, GameObjectCollection gameObjectsList) {
        NPC = npCar;
        npCar.getPylonNum();
        gameObjectsListNPC = gameObjectsList;
        
    }

    private void findPylon() {
        IIterator iterator = gameObjectsListNPC.getIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof Pylon) {
				if((((Pylon) currentObject).getSeqNum()) == (NPC.getPylonNum()+1)){
					pylon = ((Pylon) currentObject);
				}
			}
		}
    }
    
   

    public void invokeStrategy() {
    	findPylon();
        
    	//System.out.println(pylon.getSeqNum());
    	
        double dy = NPC.getLocationY() - pylon.getLocationY();
        double dx = pylon.getLocationX() - NPC.getLocationX();

        NPC.setHeading((int) Math.toDegrees(MathUtil.atan2(dy, dx)));

    }

	public void setStrategy() {
		// TODO Auto-generated method stub
		
	}


}
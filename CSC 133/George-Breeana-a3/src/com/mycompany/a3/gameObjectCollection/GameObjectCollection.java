package com.mycompany.a3.gameObjectCollection;

import java.util.ArrayList;

import com.mycompany.a3.gameObjects.GameObject;
import com.mycompany.a3.gameObjects.interfaces.ICollection;
import com.mycompany.a3.gameObjects.interfaces.IIterator;

public class GameObjectCollection implements ICollection {
	
	private ArrayList<GameObject> objects;
	
	public GameObjectCollection(){
		objects = new ArrayList<GameObject>();
	}
	
	public boolean add(Object newObject) {
		return objects.add((GameObject) newObject);	
	}

	public IIterator getIterator() { 
		return new GameObjectIterator();
	}

	//Iterator class for collection
	private class GameObjectIterator implements IIterator{ 
		private int currIndex;
		
		public GameObjectIterator()
		{
			currIndex = -1;
		}
		
		public boolean hasNext()
		{
			if(objects.size() <= 0) return false;
			if(currIndex == objects.size() - 1) return false;
			return true;
		}
		
		public Object getNext() 
		{
			currIndex ++;
			return(objects.get(currIndex));
		}


	}

}

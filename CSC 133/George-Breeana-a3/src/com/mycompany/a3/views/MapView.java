package com.mycompany.a3.views;

import java.util.Observable;
import java.util.Observer;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.mycompany.a3.game.GameWorld;
import com.mycompany.a3.gameObjects.GameObject;
import com.mycompany.a3.gameObjects.interfaces.IIterator;
import com.mycompany.a3.gameObjects.interfaces.ISelectable;
//import com.mycompany.a3.gameObjects.interfaces.ISelectable;

public class MapView extends Container implements Observer {  
	private GameWorld gw;
	private boolean paused;
	
	public void update (Observable o, Object arg) {   
		gw = (GameWorld) o;
		gw.map();
		repaint();
	}
	
    @Override
	public void paint(Graphics g) { 
        super.paint(g);
        Point coor = new Point(super.getX(),super.getY());
        
        IIterator iterator = gw.getCollectionIterator();
		Object currentObject;
		
		while(iterator.hasNext()) {
			currentObject = iterator.getNext();
			if (currentObject instanceof GameObject) {
                GameObject obj = (GameObject) currentObject;
                obj.draw(g, coor);
            }
        }
    }
    
    public boolean getPaused() {
        return paused;
    }
	
    public void setPaused(boolean paused) {
        this.paused = paused;
        if(paused){
        	IIterator iterator = gw.objectList().getIterator();
    		Object currentObject;
    		
    		while(iterator.hasNext()) {
    			currentObject = iterator.getNext();
    			if (currentObject instanceof ISelectable) {
                    ISelectable obj= (ISelectable) currentObject;
                        obj.setSelected(false);
                }
            }
            repaint();
       }
    }
    
    @Override
    public void pointerPressed(int x, int y){
        if(paused){
            x = x - getParent().getAbsoluteX();
            y = y - getParent().getAbsoluteY();
            Point pPtrRelPrnt = new Point(x,y);
            Point PCmpRelPrnt = new Point(getX(),getY());
            
            IIterator iterator = gw.objectList().getIterator();
    		Object currentObject;
    		
    		while(iterator.hasNext()) {
    			currentObject = iterator.getNext();
    			if (currentObject instanceof ISelectable) {
                    ISelectable obj = (ISelectable) currentObject;
                    if(obj.contains(pPtrRelPrnt, PCmpRelPrnt)){
                        obj.setSelected(true);
                    }else{
                        obj.setSelected(false);
                    }
                }
            }
            repaint();
        }
    }
} 
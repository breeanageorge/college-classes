package com.mycompany.a2.views;

import java.util.Observable;
import java.util.Observer;

import com.codename1.ui.Container;
import com.codename1.ui.layouts.FlowLayout;
import com.mycompany.a2.game.GameWorld;

public class MapView extends Container implements Observer {  
	//private Label lblMap;
	
	public MapView(Observable gameWorld){
		gameWorld.addObserver(this);
		initLayout(gameWorld);
	}
	
	public void initLayout(Observable gameWorld){
		//GameWorld gw = (GameWorld)gameWorld;
		
		//create container
		Container mapViewContainer = new Container(new FlowLayout(CENTER));
		//mapViewContainer.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.BLUE));
		
		//Adds labels to container
		//mapViewContainer.add(lblMap);
				
		this.add(mapViewContainer);
	}
	
	public void update (Observable o, Object arg) {   
		// code here to call the method in GameWorld (Observable) that output the  
		// game object information to the console
		GameWorld gameObjects = (GameWorld)o;
		gameObjects.map();
	}
	
} 
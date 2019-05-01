package com.mycompany.a2.views;

import java.util.Observable;
import java.util.Observer;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.mycompany.a2.game.Game;
import com.mycompany.a2.game.GameWorld;
import com.mycompany.a2.style.myLabel;

public class ScoreView extends Container implements Observer {  
	
	//Container and Labels
	private myLabel lblTime;
	private myLabel lblLastPylon;
	private myLabel lblLives;
	private myLabel lblFuel;
	private myLabel lblSound;
	private myLabel lblDamage;
	
	public ScoreView(Observable gameWorld){
		gameWorld.addObserver(this);
		initLayout(gameWorld);
	}
	
	public void initLayout(Observable gameWorld){
		GameWorld gw = (GameWorld)gameWorld;
		
		//create container
		Container scoreViewContainer = new Container(new FlowLayout(CENTER));
		scoreViewContainer.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.BLUE));
		
		lblTime = new myLabel("Time: "+gw.getClockTime()+"  ");
		lblLastPylon = new myLabel("Highest Player Pylon: "+gw.getLastPylon()+"  ");
		lblLives = new myLabel("Lives Left: "+gw.getLives()+"  ");
		lblFuel = new myLabel("Player Fuel Remaining: "+gw.getFuelLevel()+"  ");
		lblDamage = new myLabel("Player Damage Level: "+gw.getDamageLevel()+"  ");
		if(gw.getSound()==true){
			lblSound = new myLabel("Sound: ON");
		}else{
			lblSound = new myLabel("Sound: OFF");
		}
		
		//Adds labels to container
		scoreViewContainer.add(lblTime);
		scoreViewContainer.add(lblLastPylon);
		scoreViewContainer.add(lblLives);
		scoreViewContainer.add(lblFuel);
		scoreViewContainer.add(lblDamage);
		scoreViewContainer.add(lblSound);
				
		this.add(scoreViewContainer);
	}
	
	//updates variables
	public void update (Observable o, Object arg) {   
		GameWorld gw = (GameWorld)o;
		if(gw.getLives()<=0){
			Boolean exit = Dialog.show("GAME OVER", "Would you like to exit this app?", "Quit", "Restart");
	        if(exit){
	            Display.getInstance().exitApplication();
	        }else{
	        	new Game();
	        }
		}
		lblTime.setText("Time: "+gw.getClockTime()+"  ");
		lblLastPylon.setText("Highest Player Pylon: "+gw.getLastPylon()+"  ");
		lblLives.setText("Lives Left: "+gw.getLives()+"  ");
		lblFuel.setText("Player Fuel Remaining: "+gw.getFuelLevel()+"  ");
		lblDamage.setText("Player Damage Level: "+gw.getDamageLevel()+"  ");
		if(gw.getSound()){
			lblSound.setText("Sound: ON");
		}else{
			lblSound.setText("Sound: OFF");
		}
		
		if(gw.getLives()==0){
					
		}
	}
	
}
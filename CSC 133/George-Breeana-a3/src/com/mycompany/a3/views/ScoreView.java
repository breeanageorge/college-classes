package com.mycompany.a3.views;

import java.util.Observable;
import java.util.Observer;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.mycompany.a3.game.GameWorld;
import com.mycompany.a3.sound.Sounds;
import com.mycompany.a3.style.myLabel;

public class ScoreView extends Container implements Observer {  
	
	//Container and Labels
	private myLabel lblTime;
	private myLabel lblLastPylon;
	private myLabel lblLives;
	private myLabel lblFuel;
	private myLabel lblSound;
	private myLabel lblDamage;
	private Sounds gameOver;
	
	public ScoreView(Observable gameWorld){
		gameOver = new Sounds("gameOver.wav");
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
			if (gw.getSound()) gameOver.play();
			Boolean exit = Dialog.show("GAME OVER", "Would you like to exit this app?", "Okay", "Cancel");
	        if(exit){
	            Display.getInstance().exitApplication();
	        }
		}
		if(gw.getLastPylon()== (gw.getPylonTotal() -1)){
			Boolean exit = Dialog.show("YOU WON", "Would you like to exit?", "Okay", "Cancel");
	        if(exit){
	            Display.getInstance().exitApplication();
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
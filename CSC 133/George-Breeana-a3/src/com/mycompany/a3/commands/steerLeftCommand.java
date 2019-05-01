package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.GameWorld;

public class steerLeftCommand extends Command{
	private GameWorld gameWorld;
	private static steerLeftCommand steerLeft;
	
	private steerLeftCommand(GameWorld gw){
		super("Steer Left");
		this.gameWorld = gw;
	}
	
	public static steerLeftCommand steerLeft(GameWorld gw){
		if(steerLeft == null){
			return new steerLeftCommand(gw);
		}
		return steerLeft;
	}
	
	public void target(GameWorld gw) {
	    this.gameWorld = gw;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gameWorld.changeWheel(-2);
		
	}
}
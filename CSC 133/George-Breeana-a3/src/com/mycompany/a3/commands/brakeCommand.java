package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.GameWorld;

public class brakeCommand extends Command{
	private GameWorld gameWorld;
	private static brakeCommand brake;
	
	private brakeCommand(GameWorld gw){
		super("Brake");
		this.gameWorld = gw;
	}
	
	public static brakeCommand brake(GameWorld gw){
		if(brake == null){
			return new brakeCommand(gw);
		}
		return brake;
	}
	
	public void target(GameWorld gw) {
	    this.gameWorld = gw;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gameWorld.changeSpeed(-5);
		
	}
}
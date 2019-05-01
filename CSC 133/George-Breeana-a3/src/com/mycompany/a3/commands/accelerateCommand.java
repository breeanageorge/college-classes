package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.GameWorld;

public class accelerateCommand extends Command{
	private GameWorld gameWorld;
	private static accelerateCommand accelerate;
	
	private accelerateCommand(GameWorld gw){
		super("Accelerate");
		this.gameWorld = gw;
	}
	
	public static accelerateCommand accelerate(GameWorld gw){
		if(accelerate == null){
			return new accelerateCommand(gw);
		}
		return accelerate;
	}
	
	public void target(GameWorld gw) {
	        this.gameWorld = gw;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gameWorld.changeSpeed(5);
		
	}
}
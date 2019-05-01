package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.GameWorld;

public class steerRightCommand extends Command{
	private GameWorld gameWorld;
	private static steerRightCommand steerRight;
	
	private steerRightCommand(GameWorld gw){
		super("Steer Right");
		this.gameWorld = gw;
	}
	
	public static steerRightCommand steerRight(GameWorld gw){
		if(steerRight == null){
			return new steerRightCommand(gw);
		}
		return steerRight;
	}
	
	public void target(GameWorld gw) {
	    this.gameWorld = gw;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gameWorld.changeWheel(2);
		
	}
}
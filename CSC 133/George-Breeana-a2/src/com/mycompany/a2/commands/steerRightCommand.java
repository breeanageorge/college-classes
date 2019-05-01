package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class steerRightCommand extends Command{
	private GameWorld gw;
	private static steerRightCommand steerRight;
	
	private steerRightCommand(GameWorld gw){
		super("Steer Right");
		this.gw = gw;
	}
	
	public static steerRightCommand steerRight(GameWorld gw){
		if(steerRight == null){
			return new steerRightCommand(gw);
		}
		return steerRight;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.changeWheel(5);
		
	}
}
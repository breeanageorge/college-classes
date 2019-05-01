package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class steerLeftCommand extends Command{
	private GameWorld gw;
	private static steerLeftCommand steerLeft;
	
	private steerLeftCommand(GameWorld gw){
		super("Steer Left");
		this.gw = gw;
	}
	
	public static steerLeftCommand steerLeft(GameWorld gw){
		if(steerLeft == null){
			return new steerLeftCommand(gw);
		}
		return steerLeft;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.changeWheel(-5);
		
	}
}
package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class brakeCommand extends Command{
	private GameWorld gw;
	private static brakeCommand brake;
	
	private brakeCommand(GameWorld gw){
		super("Brake");
		this.gw = gw;
	}
	
	public static brakeCommand brake(GameWorld gw){
		if(brake == null){
			return new brakeCommand(gw);
		}
		return brake;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.changeSpeed(-5);
		
	}
}
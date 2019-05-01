package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class accelerateCommand extends Command{
	private GameWorld gw;
	private static accelerateCommand accelerate;
	
	private accelerateCommand(GameWorld gw){
		super("Accelerate");
		this.gw = gw;
	}
	
	public static accelerateCommand accelerate(GameWorld gw){
		if(accelerate == null){
			return new accelerateCommand(gw);
		}
		return accelerate;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.changeSpeed(5);
		
	}
}
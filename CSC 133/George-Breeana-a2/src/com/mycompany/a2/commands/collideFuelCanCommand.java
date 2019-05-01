package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class collideFuelCanCommand extends Command{
	private GameWorld gw;
	private static collideFuelCanCommand collideFuelCan;
	
	private collideFuelCanCommand(GameWorld gw){
		super("Collide with Fuel Can");
		this.gw = gw;
	}
	
	public static collideFuelCanCommand collideFuelCan(GameWorld gw){
		if(collideFuelCan == null){
			return new collideFuelCanCommand(gw);
		}
		return collideFuelCan;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.collisionFuel();
		
	}
}
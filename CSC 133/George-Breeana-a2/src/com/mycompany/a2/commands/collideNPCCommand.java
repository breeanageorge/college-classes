package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class collideNPCCommand extends Command{
	private GameWorld gw;
	private static collideNPCCommand collideNPC;
	
	private collideNPCCommand(GameWorld gw){
		super("Collide with NPC");
		this.gw = gw;
	}
	
	public static collideNPCCommand collideNPC(GameWorld gw){
		if(collideNPC == null){
			return new collideNPCCommand(gw);
		}
		return collideNPC;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.collisionCar();
		
	}
}
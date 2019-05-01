package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class collideBirdCommand extends Command{
	private GameWorld gw;
	private static collideBirdCommand collideBird;
	
	private collideBirdCommand(GameWorld gw){
		super("Collide with Bird");
		this.gw = gw;
	}
	
	public static collideBirdCommand collideBird(GameWorld gw){
		if(collideBird == null){
			return new collideBirdCommand(gw);
		}
		return collideBird;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.collisionBird();
		
	}
}
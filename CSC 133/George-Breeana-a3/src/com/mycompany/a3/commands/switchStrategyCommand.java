package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.GameWorld;

public class switchStrategyCommand extends Command{
	private GameWorld gameWorld;
	private static switchStrategyCommand switchStrategy;
	
	private switchStrategyCommand(GameWorld gw){
		super("Switch Strategy");
		this.gameWorld = gw;
	}
	
	public static switchStrategyCommand switchStrategy(GameWorld gw){
		if(switchStrategy == null){
			return new switchStrategyCommand(gw);
		}
		return switchStrategy;
	}
	
	public void target(GameWorld gw) {
	    this.gameWorld = gw;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gameWorld.changeStrategy(gameWorld.objectList());
		
	}
}
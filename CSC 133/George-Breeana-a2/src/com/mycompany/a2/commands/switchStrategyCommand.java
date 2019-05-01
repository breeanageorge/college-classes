package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class switchStrategyCommand extends Command{
	private GameWorld gw;
	private static switchStrategyCommand switchStrategy;
	
	private switchStrategyCommand(GameWorld gw){
		super("Switch Strategy");
		this.gw = gw;
	}
	
	public static switchStrategyCommand switchStrategy(GameWorld gw){
		if(switchStrategy == null){
			return new switchStrategyCommand(gw);
		}
		return switchStrategy;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.changeStrategy(gw.objectList());
		
	}
}
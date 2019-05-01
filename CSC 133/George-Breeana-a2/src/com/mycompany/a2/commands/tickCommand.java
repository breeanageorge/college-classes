package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class tickCommand extends Command{
	private GameWorld gw;
	private static tickCommand tick;
	
	private tickCommand(GameWorld gw){
		super("Clock ticked");
		this.gw = gw;
	}
	
	public static tickCommand tick(GameWorld gw){
		if(tick == null){
			return new tickCommand(gw);
		}
		return tick;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		gw.setClockTime(gw.getClockTime() + 1);
		
	}
}
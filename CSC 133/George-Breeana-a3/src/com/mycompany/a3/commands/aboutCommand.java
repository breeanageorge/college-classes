package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;

public class aboutCommand extends Command{
	private static aboutCommand toggle;
	
	private aboutCommand(){
		super("About");
	}
	
	public static aboutCommand toggle(){
		if(toggle == null){
			return new aboutCommand();
		}
		return toggle;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		 Dialog.show("About", "This game was coded by Breeana George for CSC 133. \nThe objective of the game is to collect as many pylons \n as possible without hitting birds. hitting other cars, or running out of fuel.", "Close", null);
		
	}
}
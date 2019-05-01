package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;

public class exitCommand extends Command{
	private static exitCommand exit;
	
	private exitCommand(){
		super("Exit");
	}
	
	public static exitCommand exit(){
		if(exit == null){
			return new exitCommand();
		}
		return exit;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		Boolean exit = Dialog.show("Exit", "Would you like to exit this app?", "Quit", "Cancel");
        if(exit){
            Display.getInstance().exitApplication();
        }
	}
}
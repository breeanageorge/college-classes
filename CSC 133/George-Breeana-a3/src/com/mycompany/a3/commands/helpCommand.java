package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;

public class helpCommand extends Command{
	private static helpCommand toggle;
	
	private helpCommand(){
		super("Help?");
	}
	
	public static helpCommand toggle(){
		if(toggle == null){
			return new helpCommand();
		}
		return toggle;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		String test = ""; 
        test=test.concat("A = Accelerate\n");
        test=test.concat("B = Brake\n");
        //test=test.concat("C = Collide with NPC\n");
        //test=test.concat("F = Collide with Fuel Can\n");
        //test=test.concat("G = Collide with Bird\n");
        test=test.concat("L = Steer Left\n");
        test=test.concat("R = Steer Right\n");
        //test=test.concat("T = Tick Clock\n");
        test=test.concat("X = Exit");
        Dialog.show("Help", test, "Close", null);
		
	}
}
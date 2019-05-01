package com.mycompany.a3.commands;

import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.SideMenuBar;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.GameWorld;

public class soundCommand extends Command{
	private GameWorld gw;
	private static soundCommand toggle;

	private soundCommand(GameWorld gw){
		super("Sound Toggle");
		this.gw = gw;
	}
	
	public static soundCommand toggle(GameWorld gw){
		if(toggle == null){
			return new soundCommand(gw);
		}
		return toggle;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
    	if (((CheckBox)ev.getComponent()).isSelected()){
			gw.setSoundFlag(false);
			gw.setSound();
		}else{
			gw.setSoundFlag(true);
			gw.setSound();
		}
		SideMenuBar.closeCurrentMenu();
	}
}

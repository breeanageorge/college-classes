package com.mycompany.a2.commands;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a2.game.GameWorld;

public class collidePylonCommand extends Command{
	private GameWorld gw;
	private static collidePylonCommand collidePylon;
	
	private collidePylonCommand(GameWorld gw){
		super("Collide with Pylon");
		this.gw = gw;
	}
	
	public static collidePylonCommand collidePylon(GameWorld gw){
		if(collidePylon == null){
			return new collidePylonCommand(gw);
		}
		return collidePylon;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev){
		Command cOk = new Command("Ok");
		Command cCancel = new Command("Cancel");
		Command[] cmds = new Command[]{cOk, cCancel};
		TextField pylonNum = new TextField("", "#", 4, TextArea.NUMERIC);
		Command pylon = Dialog.show("What pylon would you like to go to?", pylonNum, cmds);
        //System.out.println("pylon entered: "+pylonNum.getAsInt(0));
		if(pylon==cOk){
            gw.collisionPylon(pylonNum.getAsInt(0));
        }
	}
}
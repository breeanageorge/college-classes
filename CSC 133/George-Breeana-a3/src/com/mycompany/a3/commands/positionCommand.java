package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.Game;
import com.mycompany.a3.game.GameWorld;

public class positionCommand extends Command{
	private Game game;
	private GameWorld gameWorld;
    private static positionCommand  position;
         
    private positionCommand(GameWorld gw, Game g){
        super("Position");
        this.game = g;
        this.gameWorld = gw;
    }
       
    public static positionCommand position(GameWorld gw, Game g){
        if(position == null)
            return new positionCommand(gw, g);
        return position;
    }
    
    public void target(GameWorld gw, Game g) {
        this.game = g;
        this.gameWorld = gw;
    }
        
    
    @Override
    public void actionPerformed(ActionEvent ev){
    	//game.setButton(true);
    	Command pointerAction = pointerCommand.pointer(gameWorld, game);
		game.addPointerPressedListener(pointerAction);
    }
}
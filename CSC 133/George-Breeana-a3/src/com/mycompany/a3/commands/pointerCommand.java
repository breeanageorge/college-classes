package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.Game;
import com.mycompany.a3.game.GameWorld;

public class pointerCommand extends Command{
	private GameWorld gameWorld;
	private Game game;
    private static pointerCommand  pointer;
         
    private pointerCommand(GameWorld gw, Game g){
        super("pointer");
        this.gameWorld = gw;
        this.game = g;
    }
       
    public static pointerCommand pointer(GameWorld gw, Game g){
        if(pointer == null)
            return new pointerCommand(gw, g);
        return pointer;
    }
    
    public void target(GameWorld gw, Game g) {
        this.gameWorld = gw;
        this.game = g;
    }
        
    
    @Override
    public void actionPerformed(ActionEvent ev){
    	double x = ev.getX();
		double y = ev.getY();
		
    	gameWorld.changePosition(x, y);
    	
    	//removes listener for screen, so that position button has to be pressed each time
        game.removePointerPressedListener(this);
    }
}	
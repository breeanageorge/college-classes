package com.mycompany.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a3.game.Game;
import com.mycompany.a3.game.GameWorld;

public class pauseCommand extends Command{
    private Game game;
    private boolean enabled;
    private static pauseCommand  pause;
         
    private pauseCommand(GameWorld gw, Game g){
        super("Pause");
        this.game = g;
        enabled = true;
    }
       
    public static pauseCommand pause(GameWorld gw, Game g){
        if(pause == null)
            return new pauseCommand(gw, g);
        return pause;
    }
        
    @Override
    public void actionPerformed(ActionEvent ev){
        if(enabled){
            game.removeKeyBindings();
            game.setTimer(false);
            game.setButtoneEnabled(false);
        }else{
            game.setKeyBindings();
            game.setTimer(true);
            game.setButtoneEnabled(true);
        }
        enabled = !enabled;
    }
}
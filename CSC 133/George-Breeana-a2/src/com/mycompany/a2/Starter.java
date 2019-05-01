/**
Breeana George
CSC 133 Section 3
Assignment 2
 */

package com.mycompany.a2;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.plaf.UIManager;
import com.mycompany.a2.game.Game;

public class Starter {
   
	private Form current;
	
    public void init(Object context) {
        UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature, uncomment if you have a pro subscription
        // Log.bindCrashProtection(true);
    }
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        new Game();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();

    }
    
    public void destroy() {
    }
}

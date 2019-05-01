package com.mycompany.a2.game;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.mycompany.a2.commands.*;
import com.mycompany.a2.style.myButton;
import com.mycompany.a2.views.MapView;
import com.mycompany.a2.views.ScoreView;

public class Game extends Form{

    private GameWorld gw;
    private MapView mv;
    private ScoreView sv;
    
    public Game(){
		gw = new GameWorld();	
		mv = new MapView(gw);
		sv = new ScoreView(gw);
		gw.addObserver(mv);
		gw.addObserver(sv);
		initLayout(gw, mv, sv);
        setKeyBindings();
        gw.init();
		this.show();	
	}
	
	private void initLayout(GameWorld gw, MapView mv, ScoreView sv){
		//form
		this.setLayout(new BorderLayout());
		
		//toolbar
			Toolbar myToolbar = new Toolbar();
			setToolbar(myToolbar);
			
			myToolbar.setTitle("Race Car Game");
			//Side Menu
			Command sideAccelerate = accelerateCommand.accelerate(gw);
			Command sideExit = exitCommand.exit();
			Command sideSound = soundCommand.toggle(gw);
			Command sideAbout = aboutCommand.toggle();
			Command sideHelp = helpCommand.toggle();
			
			CheckBox soundCheckBox = new CheckBox("Sound");
	        soundCheckBox.setCommand(soundCommand.toggle(gw));
	        soundCheckBox.getAllStyles().setBgTransparency(255);
	        soundCheckBox.getAllStyles().setBgColor(ColorUtil.LTGRAY);
	        sideSound.putClientProperty("SideComponent", soundCheckBox);
			
			myToolbar.addCommandToSideMenu(sideAccelerate);
			myToolbar.addCommandToSideMenu(sideExit);
			myToolbar.addCommandToSideMenu(sideSound);
			myToolbar.addCommandToSideMenu(sideAbout);
			myToolbar.addCommandToSideMenu(sideHelp);
			//Help Right Side
			Command help = helpCommand.toggle();
			myToolbar.addCommandToRightBar(help);
		
		//North
			this.add(BorderLayout.NORTH, sv);
		
		//South Container
			Container southContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
			southContainer.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.BLUE));
			
			//Create Buttons
			myButton collideNPC = new myButton(collideNPCCommand.collideNPC(gw));
			myButton collidePylon = new myButton(collidePylonCommand.collidePylon(gw));
			myButton collideBird = new myButton(collideBirdCommand.collideBird(gw));
			myButton fuelCan = new myButton(collideFuelCanCommand.collideFuelCan(gw));
			myButton tick = new myButton(tickCommand.tick(gw));
			
			//Add Buttons to South Container
			southContainer.add(collideNPC);
			southContainer.add(collidePylon);
			southContainer.add(collideBird);
			southContainer.add(fuelCan);
			southContainer.add(tick);
			
			this.add(BorderLayout.SOUTH, southContainer);
		
		//West Container
			Container westContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
			westContainer.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.BLUE));
			
			myButton switchStrategy = new myButton(switchStrategyCommand.switchStrategy(gw));	
			myButton accelerate = new myButton(accelerateCommand.accelerate(gw));
			myButton brake = new myButton(brakeCommand.brake(gw));
			myButton right = new myButton(steerRightCommand.steerRight(gw));
			myButton left = new myButton(steerLeftCommand.steerLeft(gw));
			myButton exit = new myButton(exitCommand.exit());
			
			
			westContainer.add(switchStrategy);
			westContainer.add(accelerate);
			westContainer.add(brake);
			westContainer.add(right);
			westContainer.add(left);
			westContainer.add(exit);
			
			this.add(BorderLayout.WEST, westContainer);
		
		
		//Center Container
			this.add(BorderLayout.CENTER, mv);
				
	}

	public void setKeyBindings(){
        addKeyListener('a', accelerateCommand.accelerate(gw));
        addKeyListener('b',brakeCommand.brake(gw));
        addKeyListener('r', steerRightCommand.steerRight(gw));
        addKeyListener('l',steerLeftCommand.steerLeft(gw));
        addKeyListener('f', collideFuelCanCommand.collideFuelCan(gw));
        addKeyListener('t', tickCommand.tick(gw));
        addKeyListener('x',exitCommand.exit());
    }
	
	public void removeKeyBindings(){
		removeKeyListener('a', accelerateCommand.accelerate(gw));
		removeKeyListener('b',brakeCommand.brake(gw));
		removeKeyListener('r', steerRightCommand.steerRight(gw));
		removeKeyListener('l',steerLeftCommand.steerLeft(gw));
		removeKeyListener('f', collideFuelCanCommand.collideFuelCan(gw));
		removeKeyListener('t', tickCommand.tick(gw));
		removeKeyListener('x',exitCommand.exit());
    }
	
	
	
}
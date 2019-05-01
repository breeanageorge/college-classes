package com.mycompany.a3.game;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.util.UITimer;
import com.mycompany.a3.commands.*;
import com.mycompany.a3.style.myButton;
import com.mycompany.a3.views.MapView;
import com.mycompany.a3.views.ScoreView;

public class Game extends Form implements Runnable{

    private GameWorld gw;
    private MapView mv;
    private ScoreView sv;
    UITimer timer;
    private int time;
    private Container westContainer;
    private Toolbar myToolbar;
    private myButton pause;
    private myButton position;
    private myButton exit;
    private Command sideAccelerate;
    
    public Game(){
		gw = new GameWorld();	
		mv = new MapView();
		sv = new ScoreView(gw);
		gw.addObserver(mv);
		gw.addObserver(sv);
		initLayout(gw, mv, sv);
        setKeyBindings();
        gw.init();
        
        this.show();
		//set map dimensions
		gw.setWidth(mv.getWidth());
		gw.setHeight(mv.getHeight());
		gw.setOrginX(mv.getAbsoluteX());
		gw.setOrginY(mv.getAbsoluteY());
		timer = new UITimer(this);
        timer.schedule(60, true, this);
    }	
	
	private void initLayout(GameWorld gw, MapView mv, ScoreView sv){
		//form
		this.setLayout(new BorderLayout());
		
		//toolbar
			myToolbar = new Toolbar();
			setToolbar(myToolbar);
			
			myToolbar.setTitle("Race Car Game");
			//Side Menu
			sideAccelerate = accelerateCommand.accelerate(gw);
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
			//myButton position = new myButton(positionCommand.position(gw));
			position = new myButton(positionCommand.position(gw ,this));
			position.setEnabled(false);
			pause = new myButton(pauseCommand.pause(gw, this));
			
			//Add Buttons to South Container
			southContainer.add(position);
			southContainer.add(pause);
			
			this.add(BorderLayout.SOUTH, southContainer);
		
		//West Container
			westContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
			westContainer.getAllStyles().setBorder(Border.createLineBorder(2, ColorUtil.BLUE));
			
			myButton switchStrategy = new myButton(switchStrategyCommand.switchStrategy(gw));	
			myButton accelerate = new myButton(accelerateCommand.accelerate(gw));
			myButton brake = new myButton(brakeCommand.brake(gw));
			myButton right = new myButton(steerRightCommand.steerRight(gw));
			myButton left = new myButton(steerLeftCommand.steerLeft(gw));
			exit = new myButton(exitCommand.exit());
			
			
			westContainer.add(switchStrategy);
			westContainer.add(accelerate);
			westContainer.add(brake);
			westContainer.add(right);
			westContainer.add(left);
			westContainer.add(exit);
			
			this.add(BorderLayout.WEST, westContainer);
		
		
		//Center Container
			this.add(BorderLayout.CENTER, mv);
			//this.add(BorderLayout.CENTER, centerContainer);
	}
	
	public void setButtoneEnabled(boolean set){
		westContainer.setEnabled(set);
		exit.setEnabled(true);
		//position.setEnabled(set);
        mv.setPaused(!set);
        if(set){
        	sideAccelerate.setEnabled(true);
        	position.setEnabled(false);
            pause.setText("Pause");
            gw.setPaused(false);
            gw.setSound();
        }else{
            pause.setText("Play");
            sideAccelerate.setEnabled(false);
            position.setEnabled(true);
            gw.setPaused(true);
            gw.setSound();
        }
    }

	public void setTimer(boolean set){
        if(!set){
            timer.cancel();
        }else{
            timer.schedule(time, true, this);
        }
    }
	
	public void setKeyBindings(){
        addKeyListener('a', accelerateCommand.accelerate(gw));
        addKeyListener('b',brakeCommand.brake(gw));
        addKeyListener('r', steerRightCommand.steerRight(gw));
        addKeyListener('l',steerLeftCommand.steerLeft(gw));
        //addKeyListener('f', collideFuelCanCommand.collideFuelCan(gw));
        //addKeyListener('t', tickCommand.tick(gw));
        addKeyListener('x',exitCommand.exit());
    }
	
	public void removeKeyBindings(){
		removeKeyListener('a', accelerateCommand.accelerate(gw));
		removeKeyListener('b',brakeCommand.brake(gw));
		removeKeyListener('r', steerRightCommand.steerRight(gw));
		removeKeyListener('l',steerLeftCommand.steerLeft(gw));
		//removeKeyListener('f', collideFuelCanCommand.collideFuelCan(gw));
		//removeKeyListener('t', tickCommand.tick(gw));
		//removeKeyListener('x',exitCommand.exit());
    }
	
	public void run() {
        gw.setClockTime();
        repaint();
    }
	
}
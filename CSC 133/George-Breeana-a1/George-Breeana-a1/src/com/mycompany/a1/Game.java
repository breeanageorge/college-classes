package com.mycompany.a1;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

public class Game {

    public Form current;
    private Form home;
    private Resources theme;
    private GameWorld gw;
        		
	public Game(){
		gw = new GameWorld();
		gw.init();		
		play();				
	}
	
	private void play(){
		if (current != null) {
            current.show();
            return;
		}
		//create and build the home Form
        home = new Form("Home");
        home.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
		Label myLabel=new Label("Enter a Command:");  
		home.addComponent(myLabel);
		final TextField myTextField=new TextField();  
		home.addComponent(myTextField);  
		home.show(); 
		  myTextField.addActionListener(new ActionListener(){ 
		   public void actionPerformed(ActionEvent evt) {
			   String sCommand=myTextField.getText().toString();   
			   myTextField.clear();   
			   switch (sCommand.charAt(0)) {    
			   					   
					//accelerate
					case 'a':
						System.out.println("accelerate");
						gw.changeSpeed(5);			
						break;
					
					//brake	
					case 'b':
						System.out.println("brake");
						gw.changeSpeed(-5);
						break;
						
					//steering left	
					case 'l':
						System.out.println("left");
						gw.changeWheel(-5);
						break;	
					
					//steering right	
					case 'r':
						System.out.println("right");
						gw.changeWheel(5);
						break;
										
					//collided with another car	
					case 'c':
						System.out.println("collided with another Car");
						gw.collisionCar();
						System.out.println("Lives Remaining = " + gw.getLives());
						break;
						
					//0-9 car collided with pylon	
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':	
						System.out.println("collided with Pylon: " + sCommand.charAt(0));
						//convert char ascii to int
						int inputPylon = sCommand.charAt(0) - 48;
						gw.collisionPylon(inputPylon);
						System.out.println("LastPylon = " + gw.getLastPylon());
						break;
					
					//collided with fuel can	
					case 'f':
						System.out.println("collided with Fuel Can: ");
						gw.collisionFuel();
						System.out.println("Current fuel level = " + gw.getFuelLevel());
						break;
					
					//collided with bird	
					case 'g':
						System.out.println("collided with Bird: ");
						gw.collisionBird();
						System.out.println("Lives Remaining = " + gw.getLives());
						break;
						
					//clock has ticked		
					case 't':
						gw.setClockTime(gw.getClockTime() + 1);			//calls game world method to increment clock time
						System.out.println("Clock Time = " + gw.getClockTime());
						break;
	
					//generate display	
					case 'd':
						System.out.println("Lives remaining = " + gw.getLives());
						System.out.println("Clock time = " + gw.getClockTime());
						System.out.println("Pylon = " + gw.getLastPylon());
						System.out.println("Fuel level = " + gw.getFuelLevel());
						System.out.println("Current damage = " + gw.getDamageLevel());
						break;
					
					//output a map	
					case 'm':
						gw.map();	//calls game worlds map method to display the map
						break;
					
					//exit	
					case 'x':
						System.out.println("Enter yes or no:");
						break;
						case 'y':
							System.out.println("Exiting Car Game");
							System.exit(0);
						case 'n':
							System.out.println("Continuing Game");
						break;
					
					//in case invalid input	
					default:
						System.out.println("Please enter a valid input: ");
			   }		
			   if(gw.getLives() == 0){
					System.out.println("Game Over");
					System.exit(0);
				}
		   }
		});	    
	}
}
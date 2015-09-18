/**
*
*
*
*/

import lejos.hardware.motor.*;
import lejos.robotics.navigation.DifferentialPilot;

class Motor{
    
    private DifferentialPilot pilot;
	
    public Motor(){
		pilot = new DifferentialPilot (5.7f, 11.5f, Motor.A, Motor.D, true);
		pilot.setTravelSpeed(30);
		 // Setter hastighet på roboten
        int speed1 = 130;
    	int speed2 = 130;
        Motor.A.setSpeed(speed1);
    	Motor.D.setSpeed(speed2);

    }
}
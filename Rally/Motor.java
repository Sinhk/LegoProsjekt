/**
*
*
*
*/

import lejos.hardware.motor.*;
import lejos.robotics.navigation.DifferentialPilot;

class Motor{
    
    Private DifferentialPilot pilot = new DifferentialPilot (5.7f, 11.5f, Motor.A, Motor.D, true);
    
    Public Motor(){
		pilot.setTravelSpeed(30);

    }
}
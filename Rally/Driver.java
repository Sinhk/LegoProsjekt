package Rally;

/**
*
*
*
*/

import lejos.hardware.motor.*;
import lejos.robotics.navigation.DifferentialPilot;

class Driver{
    
    private DifferentialPilot pilot;
    private final int SPEED = 20;
    private final int CORR_RATE = 10;
    private final int TURN_RATE = 50;
    private final int TURN_ANGLE = 15;
	
    public Driver(){
		System.out.println("Set pilot");
		pilot = new DifferentialPilot (5.7f, 11.5f, Motor.A, Motor.D);
		System.out.println("Set speed");
		pilot.setTravelSpeed(SPEED);
    }
    
    public void forward(int corr){ //corr 0, no correction, 1 correct left, 2 correct right
        if (corr == 0){
            pilot.forward(); 
        }else if(corr == 1){
            pilot.steer(CORR_RATE);
        }else if(corr == 2){
            pilot.steer(-CORR_RATE);
        }
    }
    
    public void stop(){
        pilot.stop();
    }
    
    public void turnLeft(){
        pilot.steer(TURN_RATE,TURN_ANGLE);
		forward(2);
    }
    
    public void turnRight(){
        pilot.steer(-TURN_RATE,TURN_ANGLE);
		forward(1);
    }
}
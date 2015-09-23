package Rally;

/**
*
*
*
*/
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

class Mover{
    
    private MovePilot pilot;
    private final int SPEED = 20;
    private final int CORR_RATE = 5;
    private final int TURN_RATE = 10;
    private Chassis chassis;
	
    public Mover(){
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.7).offset(6);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.7).offset(-6);
		chassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		chassis.setAcceleration(1.0, 1.0);
		//pilot = new MovePilot (myChassis);//(5.7f, 11.5f, Motor.A, Motor.D);
    }
    
    public void forward(int corr){ //corr 0, no correction, 1 correct left, 2 correct right
        if (corr == 0){
        	chassis.setVelocity(SPEED,0.0);
        }else if(corr == 1){
        	chassis.setVelocity(SPEED,CORR_RATE);
        }else if(corr == 2){
        	chassis.setVelocity(SPEED,-CORR_RATE);
        }
    }
    
    public void stop(){
        pilot.stop();
    }
    
    public void turnLeft(){
    	chassis.setVelocity(SPEED,TURN_RATE);
		forward(2);
    }
    
    public void turnRight(){
    	chassis.setVelocity(SPEED,-TURN_RATE);
		forward(1);
    }
}
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
	private final double SPEED = 5;
	private double linSpeed;
	private double maxLinSpeed;
	private double maxAngSpeed;
	private double linAcc;
	private double angAcc;
	private final int CORR_RATE = 5;
	private final int TURN_RATE = 100;
    private Chassis chassis;
	
    public Mover(){
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.7).offset(6);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.7).offset(-6);
		chassis = new WheeledChassis( new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		maxLinSpeed = chassis.getMaxLinearSpeed();
		maxAngSpeed = chassis.getMaxAngularSpeed();
		linAcc = chassis.getLinearAcceleration();
		angAcc = chassis.getAngularAcceleration();
		// System.out.println("Max Linear Speed: " + maxLinSpeed);
		// System.out.println("Max Angular Speed: " + maxAngSpeed);

		// System.out.println("Linear Acc: " + linAcc);
		// System.out.println("Angular Acc: " + angAcc);

		linSpeed = maxLinSpeed * (SPEED / 100);
		// chassis.setAcceleration(5.0, 5.0);
		//pilot = new MovePilot (myChassis);//(5.7f, 11.5f, Motor.A, Motor.D);
    }
    
    public void forward(int corr){ //corr 0, no correction, 1 correct left, 2 correct right
        if (corr == 0){
			chassis.setVelocity(linSpeed, 0.0);
        }else if(corr == 1){
			chassis.setVelocity(linSpeed, CORR_RATE);
        }else if(corr == 2){
			chassis.setVelocity(linSpeed, -CORR_RATE);
        }
    }
    
    public void stop(){
        pilot.stop();
    }
    
    public void turnLeft(){
		chassis.setVelocity(linSpeed, TURN_RATE);
    }
    
    public void turnRight(){
		chassis.setVelocity(linSpeed, -TURN_RATE);
    }

	public void calibrate() {
		chassis.setSpeed(1, 20);
		chassis.rotate(80);
		chassis.waitComplete();
		chassis.rotate(-160);
		chassis.waitComplete();
		chassis.rotate(80);
		chassis.waitComplete();
	}
}
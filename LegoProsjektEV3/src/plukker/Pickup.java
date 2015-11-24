package plukker;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

class Pickup {
	
	private static NXTRegulatedMotor lift = new NXTRegulatedMotor(MotorPort.C) ;
	private static EV3MediumRegulatedMotor claw = new EV3MediumRegulatedMotor(MotorPort.B);;
	private float liftSpeed = 125f;
	private float clawSpeed = 260f;
	
	public Pickup(){
	  // zero();
	}
	
	/**
	 * Pickup ball
	 */
	public void pickup() {
		lift.rotate(125);
		claw.rotate(-150);
		//Delay.msDelay(800);
		lift.rotateTo(20);
	}
	
	/**
	 * Drop ball
	 */
	public void drop() {
		lift.rotate(30);
		claw.rotateTo(-10);
		lift.rotate(-30);
		Delay.msDelay(200);
	}
	
	/**
	 * Move motors to start positions and set speed
	 */
	public void zero(){
	    lift.setSpeed(20);
	    lift.backward();
	    while (!lift.isStalled());
	    lift.stop();
	    lift.resetTachoCount();
	    lift.setSpeed(liftSpeed);
	    lift.rotateTo(20);
	    
	    claw.setSpeed(20);
	    claw.forward();
	    while (!claw.isStalled());
	    claw.stop();
	    claw.resetTachoCount();
	    claw.setSpeed(clawSpeed);
	    claw.rotateTo(-10);
	}
	
}





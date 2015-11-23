package plukker;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

class Pickup {
	
	private NXTRegulatedMotor lift = Motor.C;
	private EV3MediumRegulatedMotor claw = new EV3MediumRegulatedMotor(MotorPort.B);
	private float liftSpeed = 125f;
	private float clawSpeed = 260f;
	
	public Pickup(){
	   zero();
	}
	
	/**
	 * Pickup ball
	 */
	public void pickup() {
		lift.rotate(100);
		claw.rotate(-100);
		//Delay.msDelay(800);
		lift.rotate(-100);
	}
	
	/**
	 * Drop ball
	 */
	public void drop() {
		lift.rotate(30);
		claw.rotate(100);
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
	    
	    claw.setSpeed(20);
	    claw.forward();
	    while (!claw.isStalled());
	    claw.stop();
	    claw.resetTachoCount();
	    claw.setSpeed(clawSpeed);
	}
	
}





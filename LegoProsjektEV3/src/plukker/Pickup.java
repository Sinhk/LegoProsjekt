package plukker;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

class Pickup {

    private static NXTRegulatedMotor lift = new NXTRegulatedMotor(MotorPort.C);
    private static EV3MediumRegulatedMotor claw = new EV3MediumRegulatedMotor(MotorPort.B);
    private float liftSpeed = 200f;
    private float clawSpeed = 260f;

    public Pickup() {
	zero();
    }

    /**
     * Pickup ball
     */
    public boolean pickup() {
	lift.rotateTo(135);
	claw.backward();
	while(!claw.isStalled());
	lift.rotateTo(10);
	if(claw.getTachoCount()<-200){
	    claw.rotateTo(-10);
	    return false;
	}
	return true;
    }

    /**
     * Drop ball
     */
    public void drop() {
	lift.rotateTo(50);
	claw.rotateTo(-10);
	lift.rotateTo(20);
    }

    /**
     * Move motors to start positions and set speed
     */
    public void zero() {
	lift.setSpeed(20);
	lift.backward();
	while (!lift.isStalled()){
	}
	lift.stop();
	lift.resetTachoCount();
	lift.setSpeed(liftSpeed);
	lift.rotateTo(20);

	claw.setSpeed(20);
	claw.forward();
	while (!claw.isStalled())
	    ;
	claw.stop();
	claw.resetTachoCount();
	claw.setSpeed(clawSpeed);
	claw.rotateTo(-10);
    }

}

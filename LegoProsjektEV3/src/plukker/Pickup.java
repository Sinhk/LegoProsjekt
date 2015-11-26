package plukker;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;

class Pickup {

    private static NXTRegulatedMotor lift = new NXTRegulatedMotor(MotorPort.C);
    private static EV3MediumRegulatedMotor claw = new EV3MediumRegulatedMotor(MotorPort.B);
    private float liftSpeed = lift.getMaxSpeed() * 0.6f;
    private float clawSpeed = 300f;

    /**
     * Klasse for å styre kloen
     */
    public Pickup() {
	zero();
    }

    /**
     * Plukker opp ballen
     * @return true om ballen ble plukke opp
     */
    public boolean pickup() {
	lift.rotateTo(145);
	claw.backward();
	while (!claw.isStalled());
	if (claw.getTachoCount() < -200) {
	    claw.rotateTo(-10);
	    lift.setSpeed(liftSpeed / 4);
	    lift.rotateTo(10);
	    lift.setSpeed(liftSpeed);
	    return false;
	}
	lift.setSpeed(liftSpeed / 4);
	lift.rotateTo(10);
	lift.setSpeed(liftSpeed);
	claw.backward();
	while (!claw.isStalled())
	    ;
	if (claw.getTachoCount() < -200) {
	    claw.rotateTo(-10);
	    return false;
	}
	return true;
    }

    /**
     * Slipper ballen
     */
    public void drop() {
	lift.rotateTo(50);
	claw.rotateTo(-20);
	lift.rotateTo(20);
    }

    /**
     * Flytter kloen til startposisjon og setter motorhastighet
     */
    public void zero() {
	lift.setSpeed(20);
	lift.backward();
	while (!lift.isStalled()) {
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
	claw.setStallThreshold(100, 1000);
	claw.rotateTo(-20);
    }

}

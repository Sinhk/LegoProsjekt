package plukker;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Point;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;

class Mover extends Thread {

	//private double linSpeed;
	//private double maxLinSpeed;
	public static Chassis chassis;
	private Sensor sensor;
	//private float offset;
	Pickup pickUp = new Pickup();
	EV3 ev3 = (EV3) BrickFinder.getLocal();
	TextLCD lcd = ev3.getTextLCD();
	int numberOfTurns;
	int rightOrLeft;

	public Mover(Sensor sensor, double speed, float maxSteer) {

		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.6).offset(5.5);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.6).offset(-5.5);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		//maxLinSpeed = chassis.getMaxLinearSpeed();
		this.sensor = sensor;
		//setSpeed(speed);
		chassis.setAcceleration(chassis.getMaxLinearSpeed() / 1.5, chassis.getMaxAngularSpeed());
		// pilot = new MovePilot (myChassis);//(5.7f, 11.5f, Motor.A, Motor.D);

	}

	public void run() {

		PoseProvider pp = chassis.getPoseProvider();
		Pose pose = pp.getPose();

		do {

			chassis.setVelocity(20, 0);

			if (sensor.getRightValue() < 2) {
				turn(numberOfTurns);
				numberOfTurns++;
				
				if (sensor.getBall()) {
					chassis.stop();
					Pickup.pickup();
					goHome(pp, pose.getLocation());
					
				}
			}
		} while (!interrupted());
	}

	public void goHome(PoseProvider SK, Point ss) {
		Pose ps = SK.getPose();
		System.out.println(ps);
		rotate(-(ps.angleTo(ss)));
		System.out.println(ps.angleTo(ss));
		chassis.travel(ps.distanceTo(ss));
		chassis.waitComplete();
		Pickup.drop();
	}
	
	public void goBack() {
		rotate(180);
		chassis.travel(12*numberOfTurns);
		rotate(rightOrLeft*90);
	}
	
	
	public void rotate(float f) {
		// chassis.setSpeed(speed, 40);
		chassis.rotate(f);
		chassis.waitComplete();
	}

	
	public void turn(int i) {
		if (i%2 == 0) {
			rotate(90);
			chassis.travel(12);
			rotate(90);
			rightOrLeft = 1;
		}
		else {
			rotate(-90);
			chassis.travel(12);
			rotate(-90);
			rightOrLeft = -1;
		}
	}
	
	/*
	public double getSpeed() {
		return linSpeed;
	}

	public void setSpeed(double speed) {
		linSpeed = maxLinSpeed * (speed / 100);
	}

	public float getOffset() {
		return offset;
	}
*/
	
}
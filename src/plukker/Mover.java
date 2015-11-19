package plukker;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Point;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;

class Mover extends Thread {

	// private double linSpeed;
	// private double maxLinSpeed;
	public static Chassis chassis;
	private static Sensor sensor = new Sensor();
	// private float offset;
	Pickup pickUp = new Pickup();
	EV3 ev3 = (EV3) BrickFinder.getLocal();
	TextLCD lcd = ev3.getTextLCD();
	int numberOfTurns;
	int rightOrLeft;
	Pose ps;

	public Mover(Sensor sensor, double speed, float maxSteer) {

		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.6).offset(5.5);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.6).offset(-5.5);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
	}

	public void run() {

		PoseProvider pp = chassis.getPoseProvider();
		Pose pose = pp.getPose();
		Button.ENTER.waitForPressAndRelease();

		do {

			chassis.setVelocity(20, 0);
			lcd.drawString("" + sensor.getRightValue(), 1, 3);
			lcd.drawString("" + numberOfTurns, 1, 4);
			if (sensor.getBall()) {
				chassis.stop();
				Delay.msDelay(800);
				Pickup.pickup();
				Delay.msDelay(400);
				goHome(pp, pose.getLocation());

			} else if (sensor.getRightValue() < 0.18) {
				chassis.setLinearAcceleration(1000);
				chassis.stop();
				chassis.setLinearAcceleration(10);
				chassis.travel(-3);
				chassis.waitComplete();
				turn(numberOfTurns);
				numberOfTurns++;

			}

		} while (!interrupted());
	}

	public void goHome(PoseProvider SK, Point ss) {
		ps = SK.getPose();
		rotate((ps.angleTo(ss)));
		chassis.travel(ps.distanceTo(ss));
		chassis.waitComplete();
		Pickup.drop();
		rotate(180);
		chassis.travel(ps.distanceTo(ss));
		chassis.waitComplete();
		rotate(-(ps.angleTo(ss)));
		chassis.waitComplete();

	}

	public void rotate(float f) {
		chassis.setLinearSpeed(20);
		chassis.stop();
		chassis.rotate(f);
		chassis.waitComplete();
	}

	public void turn(int i) {
		if (i % 2 == 0) {
			rotate(90);
			chassis.travel(12);
			chassis.waitComplete();
			rotate(90);
			rightOrLeft = 1;
		} else {
			rotate(-90);
			chassis.travel(12);
			chassis.waitComplete();
			rotate(-90);
			rightOrLeft = -1;
		}
	}
}
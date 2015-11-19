package plukker;

import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.utility.Delay;

//import java.io.File;

//import lejos.hardware.Sound;

public class Pickup {

	double linSpeed;
	static double maxLinSpeed;
	float MAX_STEER;
	static Chassis chassis;
	float offset;
	static float speed = 20;

	public static void pickup() {
		Motor.B.setSpeed(260);
		Motor.C.setSpeed(125);
		Motor.C.rotate(80);
		Motor.B.rotate(200);
		Delay.msDelay(800);
		Motor.C.rotate(-80);
	}

	public static void drop() {
		Motor.B.rotate(-200);
	}
}

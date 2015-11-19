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
		Motor.C.rotate(100);
		Motor.B.rotate(-100);
		Delay.msDelay(800);
		Motor.C.rotate(-100);
	}

	public static void drop() {
		Motor.C.rotate(30);
		Motor.B.rotate(100);
		Motor.C.rotate(-30);
		Delay.msDelay(200);
	}
}

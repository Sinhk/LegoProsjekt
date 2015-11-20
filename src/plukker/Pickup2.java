package plukker;

import javafx.scene.layout.GridPane;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.internal.ev3.EV3MotorPort;
import lejos.internal.ev3.EV3Port;
import lejos.hardware.lcd.*;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Point;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;

@SuppressWarnings("restriction")
public class Pickup2 extends Thread {
	
	private static final EV3LargeRegulatedMotor lift = EV3

	/*
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
*/

}

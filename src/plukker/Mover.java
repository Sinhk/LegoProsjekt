package plukker;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
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

class Mover extends Thread {
    // private double linSpeed;
    // private double maxLinSpeed;
    public static Chassis chassis;
    MovePilot pilot;
    private Sensor sensor;
    // private float offset;
    Pickup pickUp = new Pickup();
    EV3 ev3 = (EV3) BrickFinder.getLocal();
    TextLCD lcd = ev3.getTextLCD();
    int numberOfTurns;
    int rightOrLeft;
    Pose ps;

    public Mover(Sensor sensor, double speed, float maxSteer) {
	double wheelSize = 5.66;
	double wheelOffset = 5.31;
	Wheel leftWheel = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.A), wheelSize).offset(wheelOffset);
	Wheel rightWheel = WheeledChassis.modelWheel(new EV3LargeRegulatedMotor(MotorPort.D), wheelSize).offset(-wheelOffset);
	chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
	pilot = new MovePilot(chassis);
	pilot.setLinearSpeed(pilot.getMaxLinearSpeed()*(speed/100));
	pilot.setAngularSpeed(45);
	this.sensor = sensor;
    }
    @Override
    public void run() {
	OdometryPoseProvider pp = new OdometryPoseProvider(pilot);
	Pose pose = pp.getPose();
	LCD.drawString("Press enter to start", 0, 4);
	//testRotate();
	Button.ENTER.waitForPressAndRelease();
	pilot.forward();
	do {
	    lcd.drawString("" + sensor.getRightValue(), 1, 3);
	    lcd.drawString("" + numberOfTurns, 1, 4);
	    if (sensor.getBall()) {
		pilot.stop();
		Delay.msDelay(800);
		Pickup.pickup();
		Delay.msDelay(400);
		goHome(pp, pose.getLocation());
		pilot.forward();

	    } else if (sensor.getRightValue() < 0.18) {
		//pilot.setLinearAcceleration(1000);
		pilot.stop();
		//pilot.setLinearAcceleration(10);
		pilot.travel(-3);
		turn(numberOfTurns);
		numberOfTurns++;
		pilot.forward();
		
	    }

	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		break;
	    }
	} while (!interrupted());
    }

    public void goHome(PoseProvider SK, Point ss) {
	ps = SK.getPose();
	rotate((ps.angleTo(ss)));
	pilot.travel(ps.distanceTo(ss));
	Pickup.drop();
	rotate(180);
	pilot.travel(ps.distanceTo(ss));
	rotate(-(ps.angleTo(ss)));
    }
    public void testRotate(){
	pilot.rotate(360);
	Delay.msDelay(5000);
	pilot.rotate(-360);
	pilot.rotate(360);
	pilot.rotate(-360);
	pilot.rotate(360);
	pilot.rotate(-360);
	pilot.rotate(360);
	pilot.rotate(-360);
	pilot.rotate(360);
	pilot.rotate(-90);
	pilot.rotate(-90);
	pilot.rotate(180);
	pilot.rotate(-90);
	pilot.rotate(-90);
	pilot.rotate(-90);
	pilot.rotate(-90);
	
    }

    public void rotate(float f) {
	pilot.stop();
	pilot.rotate(f);
    }

    public void turn(int i) {
	if (i % 2 == 0) {
	    rotate(90);
	    pilot.travel(12);
	    rotate(90);
	    rightOrLeft = 1;
	} else {
	    rotate(-90);
	    pilot.travel(12);
	    rotate(-90);
	    rightOrLeft = -1;
	}
    }
}
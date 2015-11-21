package plukker;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.NXTUltrasonicSensor.DistanceMode;
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
    public Chassis chassis;
    MovePilot pilot;
    private Sensor sensor;
    // private float offset;
    Pickup pickUp = new Pickup();
    EV3 ev3 = (EV3) BrickFinder.getLocal();
    TextLCD lcd = ev3.getTextLCD();
    int numberOfTurns;
    int rightOrLeft;
    private Pose was;
    private boolean dropped = false;
    private float myHeading;
   

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
	//Pose was = pp.getPose();
	
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
		pilot.stop();
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
	Pose ps = SK.getPose();
	//ps.setHeading(myHeading);
	//SK.setPose(was);
	rotate((ps.angleTo(ss)));
	pilot.forward();
	while (!dropped) {
		if (sensor.getRightValue() < 0.18) {  //finn linje
			pilot.stop();
			pilot.rotateRight();
			if (sensor.getLeftValue() <0.18) { //roter til lineup
				pilot.stop();
				pilot.forward();
				if (sensor.getBall()) {  //kjør til avstand
					pilot.stop();
					Pickup.drop();
					dropped = true;
				}
			}
		}
	}
	pilot.travel(-20); //rygg ut
	//start sorterer
	dropped = false;
	/**
	 *  her må vi finna på noke for å reset home,
	 *  men legga den i en temp til den har kjørt tilbake.
	 */
	pilot.rotate((ps.angleTo(was.getLocation())));
	pilot.travel(ps.distanceTo(was.getLocation()));
	pilot.rotate(myHeading);
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
}
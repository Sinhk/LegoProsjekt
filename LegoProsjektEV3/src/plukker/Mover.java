package plukker;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Point;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;

class Mover extends Thread {
    // private double linSpeed;
    // private double maxLinSpeed;
    private Chassis chassis;
    private MovePilot pilot;
    private Sensor sensor;
    private Pickup pickUp = new Pickup();
    private EV3 ev3 = (EV3) BrickFinder.getLocal();
    private TextLCD lcd = ev3.getTextLCD();
    private EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
    private EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
    int numberOfTurns;
    int rightOrLeft;
    // private Pose was;
    // private boolean dropped = false;
    private OdometryPoseProvider pp;
    private Pose startPose;
    private Pose searchPose;
    private volatile boolean searching;
    private double wheelSize = 5.66;
    private double wheelOffset = 5.31;

    public Mover(Sensor sensor, double speed, float maxSteer) {
	Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, wheelSize).offset(wheelOffset);
	Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, wheelSize).offset(-wheelOffset);
	chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
	pilot = new MovePilot(chassis);
	pilot.setLinearSpeed(pilot.getMaxLinearSpeed() * (speed / 100));
	pilot.setAngularSpeed(45);
	pp = new OdometryPoseProvider(pilot);
	startPose = pp.getPose();

	this.sensor = sensor;
    }

    @Override
    public void run() {
	LCD.drawString("Set robot at X:0 Y:0 θ:0" , 0, 2);
	LCD.drawString("Press enter to start", 0, 4);
	// testRotate();
	Button.ENTER.waitForPressAndRelease();
	LCD.clear();
	pilot.travel(10);
	pilot.rotate(90);
	pilot.forward();
	searching = true;
	do {
	    // TODO Slow down while searching, speed up for return. Less speed
	    // and acceleration = more accuracy
	    while (searching) {
		lcd.drawString("" + sensor.getRightValue(), 1, 3);
		lcd.drawString("" + numberOfTurns, 1, 4);

		if (sensor.getRight()) {
		    pilot.stop();
		    align();
		    //TODO Add correction to poseprovider. Set heading, if accurate
		    turn(numberOfTurns);
		    numberOfTurns++;
		    pilot.forward();
		}
	    }
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		break;
	    }

	} while (!interrupted());
    }

    public void align() {
	if (sensor.getRight()) {
	    pilot.arcForward(-wheelOffset);
	    while (!sensor.getLeft()) {
	    }
	    pilot.stop();
	} else if (sensor.getLeft()) {
	    pilot.arcForward(wheelOffset);
	    while (!sensor.getRight()) {
	    }
	    pilot.stop();
	}
    }

    public void alignReverse() {
	if (sensor.getRight()) {
	    pilot.arcBackward(-wheelOffset);
	    while (!sensor.getLeft()) {
	    }
	    pilot.stop();
	} else if (sensor.getLeft()) {
	    pilot.arcBackward(wheelOffset);
	    while (!sensor.getRight()) {
	    }
	    pilot.stop();
	}
    }

    public void goHome() {
	searching = false;
	pilot.stop();
	searchPose = pp.getPose();
	Point homePoint = startPose.getLocation();
	// ps.setHeading(myHeading);
	// SK.setPose(was);
	rotate((searchPose.relativeBearing(homePoint)));
	pilot.forward();
	// finn linje
	while (!sensor.getRight() && sensor.getLeft()) {
	}
	;
	pilot.stop();
	// roter til lineup
	align();
	pilot.rotate(-90);
	// kjør til avstand
	pilot.forward();
	while (!sensor.getBall()) {
	}
	pilot.stop();
	pickUp.drop();
	pilot.travel(-20);
    }

    public void resumeSearch() {
	pilot.rotate(-90);
	pilot.backward();
	while (!sensor.getRight() && sensor.getLeft()) {
	}
	;
	pilot.stop();
	alignReverse();
	pp.setPose(startPose);

	pilot.rotate(pp.getPose().relativeBearing(searchPose.getLocation()));
	pilot.travel(pp.getPose().distanceTo(searchPose.getLocation()));
	pilot.rotate(searchPose.getHeading() - pp.getPose().getHeading());
	searching = true;
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

    public void testRotate() {
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
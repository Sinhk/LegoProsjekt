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
	private Pickup pickUp;
	private EV3 ev3 = (EV3) BrickFinder.getLocal();
	private TextLCD lcd = ev3.getTextLCD();
	private EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	private EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
	int numberOfTurns = 0;
	// int rightOrLeft;
	// private Pose was;
	// private boolean dropped = false;
	private OdometryPoseProvider pp;
	private Pose startPose;
	private Pose searchPose;
	private volatile boolean searching;
	private double wheelSize = 5.56;
	private double wheelOffset = 5.28;
	private volatile boolean running;
	private double speed;
	private boolean dropped = false;

	public Mover(Sensor sensor, Pickup pickUp, double speed) {
		Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, wheelSize).offset(wheelOffset);
		Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, wheelSize).offset(-wheelOffset);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
		this.speed = speed;
		setSpeeds();
		pp = new OdometryPoseProvider(pilot);
		startPose = pp.getPose();

		this.sensor = sensor;
		this.pickUp = pickUp;
		// testRotate();
	}

	public void setSpeeds() {
		pilot.setLinearSpeed(pilot.getMaxLinearSpeed() * (speed / 100));
		pilot.setAngularSpeed(30);// pilot.getMaxAngularSpeed()* 0.1);
		// pilot.setAngularAcceleration(pilot.getAngularAcceleration());
		// pilot.setLinearAcceleration(pilot.getLinearAcceleration());
	}

	@Override
	public void run() {
		LCD.drawString("Set robot at X:0 Y:0 θ:0", 0, 2);
		LCD.drawString("Press enter to start", 0, 4);
		// testRotate();
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
		pilot.travel(20);
		pilot.rotate(-90);
		pilot.forward();
		searching = true;
		running = true;
		do {
			// TODO Slow down while searching, speed up for return.
			// Less speed and acceleration = more accuracy
			while (running && searching) {
				lcd.drawString("" + sensor.getRightValue(), 1, 3);
				lcd.drawString("" + numberOfTurns, 1, 4);

				if (sensor.getRight() || sensor.getLeft()) {
					pilot.stop();
					align();
					// TODO Add correction to poseprovider. Set heading, if
					// accurate
					// pilot.travel(-3);
					turn(numberOfTurns);
					numberOfTurns++;
					pilot.forward();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				running = false;
			}

		} while (running);
		pilot.stop();
	}

	public void align() {
		if (sensor.getRight() && sensor.getLeft()) {

		} else {
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
	}

	public void alignReverse() {
	    if (sensor.getRight() && sensor.getLeft()) {
		
	    }else if (sensor.getRight()) {
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
		Point homePoint = new Point(0, -20);// startPose.getLocation();
		// homePoint.pointAt(20f, 270f);
		// ps.setHeading(myHeading);
		// SK.setPose(was);

		System.out.println(searchPose.relativeBearing(homePoint) + ", " + searchPose.getHeading());
		rotate((searchPose.relativeBearing(homePoint)));
		System.out.println(pp.getPose().getHeading());
		pilot.forward();
		// finn linje
		while (!sensor.getRight() && !sensor.getLeft()) {
		}
		pilot.stop();
		// roter til lineup
		align();
		pilot.rotate(-90);
		while (!dropped) {
		if (!sensor.getLeft()) {
			// kjør til avstand
			pilot.forward();
			while (!sensor.getBall()) {
			}
			pilot.stop();
			pickUp.drop();
			pilot.travel(-20);
			dropped = true;
		} else {
			pilot.rotate(45, sensor.getLeft());
			pilot.rotate(-90, sensor.getLeft());
		}
		dropped = false;
		}
	}

	public void resetHome() {
		pilot.rotate(-90);
		pilot.backward();
		while (!sensor.getRight() && sensor.getLeft()) {
		}
		pilot.stop();
		alignReverse();
		pp.setPose(startPose);
		sensor.resetGyro();

	}

	public void resumeSearch() {
		pilot.rotate(-90);
		pilot.backward();
		while (!sensor.getRight() && sensor.getLeft()) {
		}

		pilot.stop();
		alignReverse();
		pp.setPose(startPose);
		sensor.resetGyro();
		pilot.rotate(pp.getPose().relativeBearing(searchPose.getLocation()));
		pilot.travel(pp.getPose().distanceTo(searchPose.getLocation()));
		pilot.rotate(searchPose.getHeading() - pp.getPose().getHeading());
		searching = true;
	}

	/**
	 * Tries to pick up ball at Point
	 * 
	 * @param point
	 *            Point location of ball
	 * @return true if ball is grabbed, false if ball is lost
	 */
	public boolean fetchBall(Point point) {
		float ERR = 2f;
		pilot.rotate(pp.getPose().relativeBearing(point));
		pilot.forward();
		while (pp.getPose().distanceTo(point) > 30f)
			;
		float estDist = pp.getPose().distanceTo(point);
		System.out.println(estDist);
		if (correctAim(estDist)) {
			pilot.forward();
			float lastDistance = estDist;
			while (true) {
				if (sensor.getBall()) {
					pilot.stop();
					if (pickUp.pickup()) {
						return true;
					} else
						return false;
				}

				float distance = sensor.getDistance();
				if (distance >= lastDistance) {
					if (!correctAim(lastDistance)) {
						return false;
					}
					pilot.forward();
				}
				lastDistance = distance;
				Delay.msDelay(200);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param distance
	 *            expected distance
	 */
	public boolean correctAim(float distance) {
		float ERR = 20f;
		float angleBase = 30f;
		int run = 1;
		// System.out.println("Outside: " +sensor.getDistance());
		while (Math.abs(distance - sensor.getDistance()) > ERR) {
			float angle = (float) (angleBase * run * ((Math.pow(-1, run + 1))));
			pilot.rotate(angle, true);
			while (pilot.isMoving() && Math.abs(distance - sensor.getDistance()) > ERR) {
				// System.out.println("Inside: " +sensor.getDistance());
				// Thread.yield();
			}
			pilot.stop();
			if (angle >= 180)
				return false;
			run++;
		}
		return true;
	}

	public void rotate(float f) {
		pilot.stop();
		pilot.rotate(f);
	}

	public void slowSpin(double angle) {
		pilot.setAngularSpeed(20);
		pilot.rotate(angle, true);
	}

	public void turn(int i) {
		if (i % 2 == 0) {
			rotate(90);
			pilot.travel(12);
			rotate(90);
		} else {
			rotate(-90);
			pilot.travel(12);
			rotate(-90);
		}
	}

	public void terminate() {
		running = false;
	}

	public boolean isMoving() {
		return pilot.isMoving();
	}

	public Point getPointAt(float distance, float bearing) {
		return pp.getPose().pointAt(distance, bearing);
	}

	public float getHeading() {
		return pp.getPose().getHeading();
	}

	public MovePilot getPilot() {
		return pilot;
	}

	public void gyroRotateTo(double angle) {
		pilot.rotate(angle - sensor.getGyro());
	}

	public void setPose0() {
		pp.setPose(startPose);

	}

	public void goToPoint(Point point) {
		pilot.rotate(pp.getPose().relativeBearing(point));
		pilot.travel(pp.getPose().distanceTo(point));
	}

	public void goToCentre(float searchRadius) {
		goToPoint(startPose.pointAt(searchRadius, -45));
		gyroRotateTo(0);
	}
}
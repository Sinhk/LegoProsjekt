package Rally;

/**
*
*
*
*/
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;

class Mover extends Thread {

	private final double SPEED = 5;
	private double linSpeed;
	private double maxLinSpeed;
	private double maxAngSpeed;
	private double linAcc;
	private double angAcc;
	private final int CORR_RATE = 5;
	private final int TURN_RATE = 100;
	private Chassis chassis;
	private boolean running;
	private boolean calibrate = false;
	private Sensor sensor;

	private float kP;
	private float kI;
	private float kD;
	private float kC = 0;

	public Mover(Sensor sensor) {
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.7).offset(6);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.7).offset(-6);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		maxLinSpeed = chassis.getMaxLinearSpeed();
		maxAngSpeed = chassis.getMaxAngularSpeed();
		linAcc = chassis.getLinearAcceleration();
		angAcc = chassis.getAngularAcceleration();
		// System.out.println("Max Linear Speed: " + maxLinSpeed);
		// System.out.println("Max Angular Speed: " + maxAngSpeed);

		// System.out.println("Linear Acc: " + linAcc);
		// System.out.println("Angular Acc: " + angAcc);

		linSpeed = maxLinSpeed * (SPEED / 100);
		// chassis.setAcceleration(5.0, 5.0);
		// pilot = new MovePilot (myChassis);//(5.7f, 11.5f, Motor.A, Motor.D);

		// PID konstanter
		kP = (float) (maxAngSpeed / 2);
		kI = 0;
		kD = 0;
	}

	public Mover(Sensor sensor, boolean calibrate) {
		this(sensor);
		this.calibrate = calibrate;
	}

	public void run() {
		running = true;
		if (calibrate)
			calibrate();
		float integral = 0;
		float prevError = 0;

		if (kC != 0) {
			float pC = 1;
			kP = 0.60f * kC;
			kI = (2 * kP * 0.05f) / pC;
			kD = (kP * pC) / (8 * 0.05f);
		}
		while (running) {
			float farge = sensor.getFargeValue();
			float lys = sensor.getLysValue();
			float error = lys - farge;

			integral += error;
			float output = kP * error + kI * integral + kD * (error - prevError);
			prevError = error;

			chassis.setVelocity(linSpeed, output);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void forward(int corr) { // corr 0, no correction, 1 correct left, 2
									// correct right
		if (corr == 0) {
			chassis.setVelocity(linSpeed, 0.0);
		} else if (corr == 1) {
			chassis.setVelocity(linSpeed, CORR_RATE);
		} else if (corr == 2) {
			chassis.setVelocity(linSpeed, -CORR_RATE);
		}
	}

	public void turnLeft() {
		chassis.setVelocity(linSpeed, TURN_RATE);
	}

	public void turnRight() {
		chassis.setVelocity(linSpeed, -TURN_RATE);
	}

	public void calibrate() {
		chassis.setSpeed(1, 20);
		chassis.rotate(45);
		chassis.waitComplete();
		chassis.rotate(-90);
		chassis.waitComplete();
		chassis.rotate(45);
		chassis.waitComplete();
	}

	public double getSpeed() {
		return linSpeed;
	}

	public void setSpeed(double speed) {
		linSpeed = maxLinSpeed * (speed / 100);
	}

	public float getkP() {
		return kP;
	}

	public float getkI() {
		return kI;
	}

	public float getkD() {
		return kD;
	}

	public void setkP(float kP) {
		this.kP = kP;
	}

	public void setkI(float kI) {
		this.kI = kI;
	}

	public void setkD(float kD) {
		this.kD = kD;
	}
}
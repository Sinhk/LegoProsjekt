package rally;

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

	private double speed;
	private double linSpeed;
	private double maxLinSpeed;
	// private double maxAngSpeed;
	// private double linAcc;
	// private double angAcc;
	private int MAX_STEER;
	private Chassis chassis;
	private Sensor sensor;
	private float offset;

	private float kP;
	private float kI;
	private float kD;
	private float kC = 0;
	private int teller;

	public Mover(Sensor sensor, double speed, int maxSteer, float kP, float kI, float kD) {
		this.speed = speed;
		MAX_STEER = maxSteer;
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.6).offset(6);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.6).offset(-6);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		maxLinSpeed = chassis.getMaxLinearSpeed();
		// maxAngSpeed = chassis.getMaxAngularSpeed();
		// linAcc = chassis.getLinearAcceleration();
		// angAcc = chassis.getAngularAcceleration();
		this.sensor = sensor;
		// System.out.println("Max Linear Speed: " + maxLinSpeed);
		// System.out.println("Max Angular Speed: " + maxAngSpeed);

		// System.out.println("Linear Acc: " + linAcc);
		// System.out.println("Angular Acc: " + angAcc);

		setSpeed(speed);
		// chassis.setAcceleration(5.0, 5.0);
		// pilot = new MovePilot (myChassis);//(5.7f, 11.5f, Motor.A, Motor.D);

		// PID konstanter
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	public void run() {
		float integral = 0;
		float prevError = 0;
		long prevTime = System.currentTimeMillis();

		// PoseProvider pp = chassis.getPoseProvider();
		// Pose pose = pp.getPose();

		if (kC != 0) {
			float pC = 0.3f;
			kP = 0.60f * kC;
			kI = (2 * kP * 0.05f) / pC;
			kD = (kP * pC) / (8 * 0.05f);
		}
		teller = 0;
		// int speedTeller = 0;
		do {
			if (sensor.isBlackL()) {
				if (prevTime < (System.currentTimeMillis() - 3000)) {
					teller++;
					if (teller == 1 || (teller - 1) % 3 == 0) {
						chassis.setVelocity(linSpeed * 2, -10);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
					if (teller % 3 == 0) {
						chassis.setVelocity(linSpeed * 2, 0);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {

						}
					}

				}
				prevTime = System.currentTimeMillis();
			}

			float error = sensor.getRightValue();
			integral += error;
			if (integral * kI > (MAX_STEER / 2))
				integral = (float) (MAX_STEER / 2 / kI);
			if (integral * kI < (-MAX_STEER / 2))
				integral = (float) (-MAX_STEER / 2 / kI);
			float output = kP * error + kI * integral + kD * (error - prevError);
			// if (output > 30 || output < -30) {
			// setSpeed(speed / 3);
			// speedTeller = 0;
			// } else {
			// speedTeller++;
			// if (speedTeller > 10)
			// setSpeed(speed);
			// }
			if (output > MAX_STEER)
				output = MAX_STEER;
			if (output < -MAX_STEER)
				output = -MAX_STEER;
			offset = output;
			// offset = System.currentTimeMillis() - prevTime;
			prevError = error;
			// prevTime = System.currentTimeMillis();
			chassis.setVelocity(linSpeed, output);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (!interrupted());
	}

	public void calibrate() throws InterruptedException {
		chassis.setSpeed(1, 40);
		chassis.rotate(-30);
		chassis.waitComplete();
		Thread.sleep(200);
		chassis.rotate(25);
		chassis.waitComplete();
		// chassis.rotate(45);
		// chassis.waitComplete();
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

	public float getOffset() {
		return offset;
	}

	public void rotate(int i) {
		chassis.setSpeed(1, 40);
		chassis.rotate(i);
		chassis.waitComplete();
	}

	public int getTeller() {

		return teller;
	}
}

/*
 * public void run() { running = true; if (calibrate) calibrate(); float
 * integral = 0; float prevError = 0;
 * 
 * if (kC != 0) { float pC = 0.5f; kP = 0.60f * kC; kI = (2 * kP * 0.01f) / pC;
 * kD = (kP * pC) / (8 * 0.01f); } while (running) { float farge =
 * (sensor.getFargeValue() + 0.02f) * 2; float lys = (sensor.getLysValue()) *
 * 2;// - 0.3f) * 1.7f; float error = lys - farge;
 * 
 * integral += error; if (integral > (maxAngSpeed / 2)) integral = (float)
 * (maxAngSpeed / 2); if (integral < (-maxAngSpeed / 2)) integral = (float)
 * (-maxAngSpeed / 2); float output = kP * error + kI * integral + kD * (error -
 * prevError); offset = output; prevError = error;
 * 
 * chassis.setVelocity(linSpeed, output); try { Thread.sleep(10); } catch
 * (InterruptedException e) { e.printStackTrace(); } } }
 */
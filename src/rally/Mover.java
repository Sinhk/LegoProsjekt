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
	private final float MAX_STEER;
	private Chassis chassis;
	private Sensor sensor;

	private float kP;
	private float kI;
	private float kD;

	public Mover(Sensor sensor) {
		speed = 53;
		MAX_STEER = 160;
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 5.6).offset(6);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.D, 5.6).offset(-6);
		chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
		maxLinSpeed = chassis.getMaxLinearSpeed();
		this.sensor = sensor;
		setSpeed(speed);
		chassis.setAcceleration(chassis.getMaxLinearSpeed() / 1.5, chassis.getMaxAngularSpeed());

		// PID konstanter
		kP = 90;
		kI = 1;
		kD = 400;
	}

	public void run() {
		float integral = 0;
		float prevError = 0;
		long prevTime = System.currentTimeMillis();

		int teller = 0;
		int speedCount = 0;
		do {
			if (sensor.isBlackL()) {
				if (prevTime < (System.currentTimeMillis() - 3000)) {
					teller++;
					if (teller % 3 == 0) {
						chassis.setVelocity(linSpeed * 1.5, 0);
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
				integral = (MAX_STEER / 2 / kI);
			if (integral * kI < (-MAX_STEER / 2))
				integral = (-MAX_STEER / 2 / kI);
			float output = kP * error + kI * integral + kD * (error - prevError);
			prevError = error;
			if (output > MAX_STEER)
				output = MAX_STEER;
			if (output < -MAX_STEER)
				output = -MAX_STEER;
			if (output > 85 || output < -85) {
				setSpeed(30);
				speedCount = 0;
			} else {
				speedCount++;
				if (speedCount > 60)
					setSpeed(speed);
			}
			chassis.setVelocity(linSpeed, output);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}

		} while (!interrupted());
	}

	public void setSpeed(double speed) {
		linSpeed = maxLinSpeed * (speed / 100);
	}
}
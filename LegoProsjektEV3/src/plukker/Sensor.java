package plukker;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

class Sensor {
    // SampleProviders
    private SampleProvider distance;
    private SampleProvider rightSignal;
    private SampleProvider leftSignal;
    private SampleProvider gyro;
    // Sensors
    private EV3UltrasonicSensor dist;
    private EV3ColorSensor rightsensor;
    private EV3ColorSensor leftsensor;
    private EV3GyroSensor gyroSensor;
    // Constants
    private final float BLACK = 0.18f;

    public Sensor() {
	dist = new EV3UltrasonicSensor(SensorPort.S2);
	distance = dist.getDistanceMode();
	rightsensor = new EV3ColorSensor(SensorPort.S4);
	rightSignal = rightsensor.getRedMode();
	leftsensor = new EV3ColorSensor(SensorPort.S3);
	leftSignal = leftsensor.getRedMode();
	gyroSensor = new EV3GyroSensor(SensorPort.S1);
	gyro = gyroSensor.getAngleMode();
	gyroSensor.reset();
    }

    public boolean getBall() {
	if (getDistance() <= 10.0 && getDistance() != 0.0) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Read right sensor
     * 
     * @return true hvis sort, ellers false
     */
    public boolean getRight() {
	float[] sample = new float[rightSignal.sampleSize()];
	rightSignal.fetchSample(sample, 0);
	if (sample[0] < BLACK) {
	    return true;
	} else
	    return false;
    }

    /**
     * Read left sensor
     * 
     * @return true hvis sort, ellers false
     */
    public boolean getLeft() {
	float[] sample = new float[leftSignal.sampleSize()];
	leftSignal.fetchSample(sample, 0);
	if (sample[0] < BLACK) {
	    return true;
	} else
	    return false;
    }

    public float getRightValue() {
	float[] sample = new float[rightSignal.sampleSize()];

	rightSignal.fetchSample(sample, 0);
	return sample[0];
    }

    public float getLeftValue() {
	float[] sample = new float[leftSignal.sampleSize()];

	leftSignal.fetchSample(sample, 0);
	return sample[0];
    }

    public void close() {
	dist.close();
	rightsensor.close();
	leftsensor.close();
    }

    public float getDistance() {
	float[] sample = new float[distance.sampleSize()];
	distance.fetchSample(sample, 0);
	return ((sample[0] == 0.0) ? Float.POSITIVE_INFINITY : sample[0])*100f;
    }

    public float getGyro() {
	float[] sample = new float[gyro.sampleSize()];
	gyro.fetchSample(sample, 0);
	return sample[0];
    }

    public void resetGyro() {
	gyroSensor.reset();
	Delay.msDelay(5000);
    }
}
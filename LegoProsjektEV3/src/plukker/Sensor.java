package plukker;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;
import lejos.robotics.Gyroscope;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.GyroDirectionFinder;

class Sensor {
    private SampleProvider distance;
    private SampleProvider rightLeser;
    private SampleProvider leftLeser;
    private EV3UltrasonicSensor dist;
    private EV3ColorSensor rightsensor;
    private EV3ColorSensor leftsensor;
    private final float BLACK = 0.18f;
    private EV3GyroSensor gyroSensor;
    private SampleProvider gyro;

    public Sensor() {
	Brick brick = BrickFinder.getDefault();
	Port s1 = brick.getPort("S1");
	Port s2 = brick.getPort("S2"); 
	Port s3 = brick.getPort("S3"); 
	Port s4 = brick.getPort("S4");

	dist = new EV3UltrasonicSensor(s2);
	distance = dist.getDistanceMode();
	rightsensor = new EV3ColorSensor(s4);
	rightLeser = rightsensor.getRedMode();
	leftsensor = new EV3ColorSensor(s3);
	leftLeser = leftsensor.getRedMode();
	gyroSensor = new EV3GyroSensor(s1);
	gyro = gyroSensor.getAngleMode();
	gyroSensor.reset();
    }

    public boolean getBall() {
	float[] sample = new float[distance.sampleSize()];
	distance.fetchSample(sample, 0);
	if (sample[0] <= 0.1 && sample[0] != 0.0) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Read right sensor
     * 
     * @return true if black, false if not
     */
    public boolean getRight() {
	float[] sample = new float[rightLeser.sampleSize()];
	rightLeser.fetchSample(sample, 0);
	if (sample[0] < BLACK) {
	    return true;
	} else
	    return false;
    }

    /**
     * Read left sensor
     * 
     * @return true if black, false if not
     */
    public boolean getLeft() {
	float[] sample = new float[leftLeser.sampleSize()];
	leftLeser.fetchSample(sample, 0);
	if (sample[0] < BLACK) {
	    return true;
	} else
	    return false;
    }

    public float getRightValue() {
	float[] sample = new float[rightLeser.sampleSize()];

	rightLeser.fetchSample(sample, 0);
	return sample[0];
    }

    public float getLeftValue() {
	float[] sample = new float[leftLeser.sampleSize()];

	leftLeser.fetchSample(sample, 0);
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
	return sample[0];
    }

    public float getGyro(){
	float[] sample = new float[gyro.sampleSize()];
	gyro.fetchSample(sample, 0);
	return sample[0];
    }

    public void resetGyro() {
	gyroSensor.reset();
	Delay.msDelay(5000);	
    }
}
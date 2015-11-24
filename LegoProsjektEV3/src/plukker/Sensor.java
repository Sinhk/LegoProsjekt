package plukker;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;
import lejos.robotics.Gyroscope;
import lejos.robotics.SampleProvider;
import lejos.utility.GyroDirectionFinder;

class Sensor {
    private SampleProvider distance;
    private SampleProvider rightSignal;
    private SampleProvider leftSignal;
    private EV3UltrasonicSensor dist;
    private EV3ColorSensor rightsensor;
    private EV3ColorSensor leftsensor;
    private final float BLACK = 0.18f;
    private GyroDirectionFinder gyro;

    public Sensor() {
	Brick brick = BrickFinder.getDefault();
	Port s1 = brick.getPort("S1");
	Port s2 = brick.getPort("S2"); 
	Port s3 = brick.getPort("S3"); 
	Port s4 = brick.getPort("S4");

	dist = new EV3UltrasonicSensor(s2);
	distance = dist.getDistanceMode();
	rightsensor = new EV3ColorSensor(s4);
	rightSignal = rightsensor.getRedMode();
	leftsensor = new EV3ColorSensor(s3);
	leftSignal = leftsensor.getRedMode();
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
	return sample[0];
    }

}
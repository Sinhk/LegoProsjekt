package rally;

/**
*
*
*
*/

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

class Sensor {
	private SampleProvider leftLeser;
	private SampleProvider rightLeser;
	private EV3ColorSensor leftsensor;
	private EV3ColorSensor rightsensor;

	public Sensor() {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //
		leftsensor = new EV3ColorSensor(s1);
		leftLeser = leftsensor.getRedMode();

		rightsensor = new EV3ColorSensor(s4);
		rightLeser = rightsensor.getRedMode();
		// MeanFilter rightMean = new MeanFilter(rightLeser, 25);
		// SampleThread rightThread = new SampleThread(rightLeser, 500);
	}

	public boolean isBlackL() {
		float[] sample = new float[leftLeser.sampleSize()];
		leftLeser.fetchSample(sample, 0);
		if (sample[0] <= 0.1 && sample[0] != 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public float getRightValue() {
		float[] sample = new float[rightLeser.sampleSize()];
		double error;
		double offset = 0;
		leftLeser.fetchSample(sample, 0);
		error = sample[0] - offset;
		// error = Math.round((error * 100)) / 100.0;
		// if (error > 0)
		// error *= 1.6;
		return (float) error;
	}

	public void close() {
		leftsensor.close();
		rightsensor.close();
	}

}
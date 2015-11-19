package plukker;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.*;
import lejos.robotics.SampleProvider;

class Sensor {
	private SampleProvider distance;
	private SampleProvider rightLeser;
	private EV3UltrasonicSensor dist;
	private EV3ColorSensor rightsensor;

	public Sensor() {
		Brick brick = BrickFinder.getDefault();
		Port s2 = brick.getPort("S2"); //
		Port s4 = brick.getPort("S4"); //
		dist = new EV3UltrasonicSensor(s2);
		distance = dist.getDistanceMode();

		rightsensor = new EV3ColorSensor(s4);
		rightLeser = rightsensor.getRedMode();
		// MeanFilter rightMean = new MeanFilter(rightLeser, 25);
		// SampleThread rightThread = new SampleThread(rightLeser, 500);
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

	public float getRightValue() {
		float[] sample = new float[rightLeser.sampleSize()];
		double error;
		double offset = 0.095;
		rightLeser.fetchSample(sample, 0);
		error = sample[0] - offset;
		error *= 10.0;
		if (error < 0.1 && error > -0.1)
			error = 0;
		// error = Math.round((error * 100)) / 100.0;
		// if (error > 0)
		// error *= 1.6;
		return (float) error;
	}

	public void close() {
		dist.close();
		rightsensor.close();
	}

}
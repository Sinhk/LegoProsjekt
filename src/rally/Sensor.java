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
	private SampleProvider leftReader;
	private SampleProvider rightReader;
	private EV3ColorSensor leftsensor;
	private EV3ColorSensor rightsensor;

	public Sensor() {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //
		leftsensor = new EV3ColorSensor(s1);
		leftReader = leftsensor.getRedMode();

		rightsensor = new EV3ColorSensor(s4);
		rightReader = rightsensor.getRedMode();
	}

	public boolean isBlackL() {
		float[] sample = new float[leftReader.sampleSize()];
		leftReader.fetchSample(sample, 0);
		if (sample[0] <= 0.1 && sample[0] != 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public float getRightValue() {
		float[] sample = new float[rightReader.sampleSize()];
		double error;
		double offset = 0.095;
		rightReader.fetchSample(sample, 0);
		error = sample[0] - offset;
		error *= 10.0;
		if (error < 0.1 && error > -0.1)
			error = 0;
		return (float) error;
	}

	public void close() {
		leftsensor.close();
		rightsensor.close();
	}

}
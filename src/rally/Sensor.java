package rally;

/**
*
*
*
*/

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MaximumFilter;
import lejos.robotics.filter.MinimumFilter;

class Sensor {
	private SampleProvider leftLeser;
	private SampleProvider rightLeser;
	private float[] white;
	private float[] black;
	private NXTLightSensor leftsensor;
	private NXTLightSensor rightsensor;
	private boolean autoCalibrate;
	private boolean rgbMode;
	private SampleProvider rightFilter;
	private autoAdjustFilter rightAutoFilter;
	private MaximumFilter rightMax;
	private MinimumFilter rightMin;

	private float lysMaxValue = 0;
	private float lysMinValue = 0;

	public Sensor(boolean autoCalibrate) {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //
		this.autoCalibrate = autoCalibrate;
		leftsensor = new NXTLightSensor(s1);
		leftLeser = leftsensor.getRedMode();

		rightsensor = new NXTLightSensor(s4);
		rightLeser = rightsensor.getRedMode();
		rgbMode = false;
		white = new float[rightLeser.sampleSize()];
		black = new float[rightLeser.sampleSize()];

		if (autoCalibrate) {
			rightAutoFilter = new autoAdjustFilter(rightLeser);
			rightFilter = rightAutoFilter;

		} else
			rightFilter = rightLeser;

		rightMax = new MaximumFilter(rightLeser, 2000);
		rightMin = new MinimumFilter(rightLeser, 2000);

	}

	/*
	 * public void run() { do { try { fargeFilter.fetchSample(fargeSample, 0);
	 * fargeValue = fargeSample[0]; lysFilter.fetchSample(lysSample, 0);
	 * lysValue = lysSample[0]; Thread.sleep(5); } catch (InterruptedException
	 * e) { e.printStackTrace(); } } while (!isInterrupted()); }
	 */

	public boolean isBlackL() {
		float[] sample = new float[leftLeser.sampleSize()];
		leftLeser.fetchSample(sample, 0);
		if (sample[0] <= 0.4 && sample[0] != 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public double getLysValue() {
		float[] sample = new float[rightFilter.sampleSize()];
		double error;
		rightFilter.fetchSample(sample, 0);
		if (rgbMode) {
			double intensity = 0;
			for (int i = 0; i < sample.length; i++)
				intensity += Math.pow((sample[i] - black[i]) / ((double) white[i] - black[i]), 2);
			error = 2 * (Math.sqrt(intensity) - .5);
		} else if (autoCalibrate) {
			error = sample[0] - .5;
		} else {

			error = sample[0] - lysMinValue - (lysMaxValue - lysMinValue) / 2;
		}
		return error;
	}

	public float getLysMaxValue() {
		return lysMaxValue;
	}

	public float getLysMinValue() {
		return lysMinValue;
	}

	public void calibrate(Mover mover) throws InterruptedException {
		if (autoCalibrate) {
			mover.calibrate();
		} else if (rgbMode) {
			rightFilter.fetchSample(black, 0);
			mover.rotate(-35);
			rightFilter.fetchSample(white, 0);
		} else {
			float[] sample = new float[rightFilter.sampleSize()];
			Thread t = new Thread() {
				public void run() {
					do {
						float[] sample = new float[rightFilter.sampleSize()];
						rightMin.fetchSample(sample, 0);
						rightMax.fetchSample(sample, 0);
						// System.out.println(lysSample[0]);
					} while (!interrupted());
				}
			};
			t.start();

			t.interrupt();
			rightMin.fetchSample(sample, 0);
			lysMinValue = sample[0];
			// System.out.println(lysMinValue);
			rightMax.fetchSample(sample, 0);
			lysMaxValue = sample[0];
			// System.out.println(lysMaxValue);
		}
	}

}
/*
 * // Old calibration fargeKalibrering.setOffsetCalibration(1);
 * lysKalibrering.setOffsetCalibration(1);
 * fargeKalibrering.setTimeConstant(0.5f); lysKalibrering.setTimeConstant(0.5f);
 * fargeKalibrering.startCalibration(); lysKalibrering.startCalibration(); //
 * mover.calibrate(); for (int i = 0; i < 100; i++) {
 * fargeKalibrering.fetchSample(fargeSample, 0);
 * lysKalibrering.fetchSample(lysSample, 0); } // Thread.sleep(2000);
 * fargeKalibrering.stopCalibration(); lysKalibrering.stopCalibration(); float[]
 * offsetC = fargeKalibrering.getOffsetCorrection(); float[] scaleC =
 * fargeKalibrering.getScaleCorrection(); float[] loffsetC =
 * lysKalibrering.getOffsetCorrection(); float[] lscaleC =
 * lysKalibrering.getScaleCorrection(); // System.out.println(offsetC[0] + ", "
 * + loffsetC[0]); // System.out.println(scaleC[0] + ", " + lscaleC[0]);
 * fargeKalibrering.save("farge.cal"); lysKalibrering.save("lys.cal"); }
 */
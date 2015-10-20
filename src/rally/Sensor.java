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
import lejos.robotics.filter.MaximumFilter;
import lejos.robotics.filter.MinimumFilter;

class Sensor {
	private SampleProvider leftLeser;
	private SampleProvider rightLeser;
	private float[] white;
	private float[] black;
	private EV3ColorSensor leftsensor;
	private EV3ColorSensor rightsensor;
	private boolean autoCalibrate;
	private boolean rgbMode;
	private SampleProvider rightFilter;
	private autoAdjustFilter rightAutoFilter;
	private MaximumFilter rightMax;
	private MinimumFilter rightMin;

	private float lysMaxValue = 0;
	private float lysMinValue = 0;

	public Sensor(boolean autoCalibrate, boolean rgbMode) {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //
		this.autoCalibrate = autoCalibrate;
		leftsensor = new EV3ColorSensor(s1);
		leftLeser = leftsensor.getRedMode();

		rightsensor = new EV3ColorSensor(s4);
		rightLeser = rightsensor.getRedMode();
		// MeanFilter rightMean = new MeanFilter(rightLeser, 25);
		// SampleThread rightThread = new SampleThread(rightLeser, 500);
		this.rgbMode = rgbMode;
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
		if (sample[0] <= 0.1 && sample[0] != 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public float getRightValue() {
		float[] sample = new float[rightFilter.sampleSize()];
		double error;
		rightFilter.fetchSample(sample, 0);
		/*
		 * if (rgbMode) { double intensity = 0; for (int i = 0; i <
		 * sample.length; i++) intensity += Math.pow((sample[i] - black[i]) /
		 * ((double) white[i] - black[i]), 2); error = 2 * (Math.sqrt(intensity)
		 * - .5); } else if (autoCalibrate) {
		 */
		error = sample[0] - .4;
		error = Math.round((error * 100)) / 100.0;
		if (error > -0.1 && error < 0.1) {
			error = 0;
		}
		// if (error > 0)
		// error *= 1.6;
		if (error > .5)
			error = .5;
		if (error < -.5)
			error = -.5;

		// } else {
		// error = (sample[0] - lysMinValue / (lysMaxValue - lysMinValue) -
		// .5f);
		// }

		return (float) error;
	}

	public float getLysMaxValue() {
		return lysMaxValue;
	}

	public float getLysMinValue() {
		return lysMinValue;
	}

	public void calibrate(Mover mover) throws InterruptedException {
		if (autoCalibrate) {
			rightAutoFilter.reset();
			Thread t = new Thread() {
				public void run() {
					do {
						float[] sample = new float[rightFilter.sampleSize()];
						rightFilter.fetchSample(sample, 0);

						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
						// System.out.println(lysSample[0]);
					} while (!interrupted());
				}
			};
			t.start();
			mover.calibrate();
			t.interrupt();
		} else if (rgbMode) {
			float[] sample = new float[rightFilter.sampleSize()];
			MinimumFilter min = new MinimumFilter(rightFilter, 100);
			for (int i = 0; i < 100; i++) {
				min.fetchSample(sample, 0);
			}
			min.fetchSample(black, 0);
			mover.rotate(-45);
			MaximumFilter max = new MaximumFilter(rightFilter, 100);
			for (int i = 0; i < 100; i++) {
				max.fetchSample(sample, 0);
			}
			max.fetchSample(white, 0);
			mover.rotate(30);
		} else {
			float[] sample = new float[rightFilter.sampleSize()];
			Thread t = new Thread() {
				public void run() {
					do {
						float[] sample = new float[rightFilter.sampleSize()];
						rightMin.fetchSample(sample, 0);
						rightMax.fetchSample(sample, 0);

						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						// System.out.println(lysSample[0]);
					} while (!interrupted());
				}
			};
			t.start();
			mover.calibrate();
			t.interrupt();
			rightMin.fetchSample(sample, 0);
			lysMinValue = sample[0];
			// System.out.println(lysMinValue);
			rightMax.fetchSample(sample, 0);
			lysMaxValue = sample[0];
			// System.out.println(lysMaxValue);
		}
	}

	public void close() {
		leftsensor.close();
		rightsensor.close();
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
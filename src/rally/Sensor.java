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
import lejos.robotics.filter.LowPassFilter;
import lejos.robotics.filter.MaximumFilter;
import lejos.robotics.filter.MinimumFilter;

class Sensor {
	private SampleProvider fargeLeser;
	private SampleProvider lysLeser;
	private float[] fargeSample;
	private float[] lysSample;
	private NXTLightSensor fargesensor;
	private NXTLightSensor lyssensor;
	private float fargeValue;
	private float lysValue;
	private LowPassFilter fargeFilter;
	private LowPassFilter lysFilter;
	private MaximumFilter fargeMax;
	private MaximumFilter lysMax;
	private MinimumFilter lysMin;
	private MinimumFilter fargeMin;

	private float lysMaxValue = 0;
	private float lysMinValue = 0;

	public Sensor() {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //

		fargesensor = new NXTLightSensor(s1);
		fargeLeser = fargesensor.getRedMode();

		lyssensor = new NXTLightSensor(s4);
		lysLeser = lyssensor.getRedMode();

		fargeFilter = new LowPassFilter(fargeLeser, 0.5f);
		lysFilter = new LowPassFilter(lysLeser, 0.5f);

		fargeMax = new MaximumFilter(fargeFilter, 200);
		fargeMin = new MinimumFilter(fargeFilter, 200);
		lysMax = new MaximumFilter(lysFilter, 2000);
		lysMin = new MinimumFilter(lysFilter, 2000);

		fargeFilter = new LowPassFilter(fargeLeser, 0.0f);
		lysFilter = new LowPassFilter(lysLeser, 0.0f);

		fargeSample = new float[fargeFilter.sampleSize()];
		lysSample = new float[lysFilter.sampleSize()];
	}

	/*
	 * public void run() { do { try { fargeFilter.fetchSample(fargeSample, 0);
	 * fargeValue = fargeSample[0]; lysFilter.fetchSample(lysSample, 0);
	 * lysValue = lysSample[0]; Thread.sleep(5); } catch (InterruptedException
	 * e) { e.printStackTrace(); } } while (!isInterrupted()); }
	 */

	public boolean isBlackL() {

		if (fargeValue <= 0.1 && fargeValue != 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isBlackR() {
		if (lysValue <= 0.1 && lysValue != 0.0) {
			return true;
		} else {
			return false;
		}
	}

	public float getFargeValue() {
		fargeFilter.fetchSample(fargeSample, 0);
		fargeValue = fargeSample[0];
		return fargeValue;
	}

	public float getLysValue() {
		lysFilter.fetchSample(lysSample, 0);
		lysValue = lysSample[0];
		return lysValue;
	}

	public float getLysMaxValue() {
		return lysMaxValue;
	}

	public float getLysMinValue() {
		return lysMinValue;
	}

	public void calibrate(Mover mover) throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				do {
					lysMin.fetchSample(lysSample, 0);
					lysMax.fetchSample(lysSample, 0);
					// System.out.println(lysSample[0]);
				} while (!interrupted());
			}
		};
		t.start();
		mover.calibrate();
		t.interrupt();
		lysMin.fetchSample(lysSample, 0);
		lysMinValue = lysSample[0];
		// System.out.println(lysMinValue);
		lysMax.fetchSample(lysSample, 0);
		lysMaxValue = lysSample[0];
		// System.out.println(lysMaxValue);

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
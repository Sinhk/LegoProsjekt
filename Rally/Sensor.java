package Rally;

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
import lejos.robotics.filter.LinearCalibrationFilter;

class Sensor {
	private SampleProvider fargeLeser;
	private SampleProvider lysLeser;
	private float[] fargeSample;
	private float[] lysSample;
	private NXTLightSensor fargesensor;
	private NXTLightSensor lyssensor;
	private float fargeValue;
	private float lysValue;
	private autoAdjustFilter fargeFilter;
	private autoAdjustFilter lysFilter;
	private LinearCalibrationFilter fargeKalibrering;
	private LinearCalibrationFilter lysKalibrering;

	public Sensor() {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //

		fargesensor = new NXTLightSensor(s1);
		fargeLeser = fargesensor.getRedMode();

		lyssensor = new NXTLightSensor(s4);
		lysLeser = lyssensor.getRedMode();

		fargeKalibrering = new LinearCalibrationFilter(fargeLeser);
		lysKalibrering = new LinearCalibrationFilter(lysLeser);
		fargeKalibrering.setTimeConstant(0.5f);

		fargeFilter = new autoAdjustFilter(fargeKalibrering);
		lysFilter = new autoAdjustFilter(lysKalibrering);

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

	public void calibrate() {
		fargeKalibrering.setOffsetCalibration(1);
		lysKalibrering.setOffsetCalibration(1);
		fargeKalibrering.setTimeConstant(0.5f);
		lysKalibrering.setTimeConstant(0.5f);
		fargeKalibrering.startCalibration();
		lysKalibrering.startCalibration();
		// mover.calibrate();
		for (int i = 0; i < 100; i++) {
			fargeFilter.fetchSample(fargeSample, 0);
			lysFilter.fetchSample(lysSample, 0);
		}
		// Thread.sleep(2000);
		fargeKalibrering.stopCalibration();
		lysKalibrering.stopCalibration();
		float[] offsetC = fargeKalibrering.getOffsetCorrection();
		float[] scaleC = fargeKalibrering.getScaleCorrection();
		float[] loffsetC = lysKalibrering.getOffsetCorrection();
		float[] lscaleC = lysKalibrering.getScaleCorrection();
		System.out.println(offsetC[0] + ", " + loffsetC[0]);
		System.out.println(scaleC[0] + ", " + lscaleC[0]);
		fargeKalibrering.save("farge.cal");
		lysKalibrering.save("lys.cal");
	}
}

// Old calibration

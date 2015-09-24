package Rally;

/**
*
*
*
*/

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;

class Sensor extends Thread {
	private SampleProvider fargeLeser;
	private SampleProvider lysLeser;
	private float[] fargeSample;
	private float[] lysSample;
	private EV3ColorSensor fargesensor;
	private NXTLightSensor lyssensor;
	private float fargeValue;
	private float lysValue;
	private autoAdjustFilter fargeFilter;
	private autoAdjustFilter lysFilter;

	public Sensor() {
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); //
		Port s4 = brick.getPort("S4"); //

		fargesensor = new EV3ColorSensor(s1);
		fargeLeser = fargesensor.getRedMode();

		lyssensor = new NXTLightSensor(s4);
		lysLeser = lyssensor.getRedMode();

		fargeFilter = new autoAdjustFilter(fargeLeser);
		lysFilter = new autoAdjustFilter(lysLeser);

		fargeSample = new float[fargeFilter.sampleSize()];
		lysSample = new float[lysFilter.sampleSize()];
	}

	public void run() {
		do {
			try {
				fargeFilter.fetchSample(fargeSample, 0);
				fargeValue = fargeSample[0];
				lysFilter.fetchSample(lysSample, 0);
				lysValue = lysSample[0];
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!isInterrupted());
	}

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
		return fargeValue;
	}

	public float getLysValue() {
		return lysValue;
	}

}

// Old calibration

// public Sensor(boolean calib, Mover mover) {
// Brick brick = BrickFinder.getDefault();
// Port s1 = brick.getPort("S1"); //
// Port s4 = brick.getPort("S4"); //
// fargesensor = new EV3ColorSensor(s1);
// fargeLeser = fargesensor.getRedMode();
// fargeSample = new float[fargeLeser.sampleSize()];
// lyssensor = new NXTLightSensor(s4);
// lysLeser = lyssensor.getRedMode();
// lysSample = new float[lysLeser.sampleSize()];
// fargeFilter = new LinearCalibrationFilter(fargeLeser);
// lysFilter = new LinearCalibrationFilter(lysLeser);
// if (calib) {
// this.start();
// Skjerm skjerm = new Skjerm(this);
// skjerm.start();
// calibrate(mover);
// }
// lysFilter.open("lys.cal");
// fargeFilter.open("farge.cal");
// try {
// Thread.sleep(5000);
// } catch (InterruptedException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// }

// public void calibrate(Mover mover) {
// System.out.println(fargeFilter.getCalibrationType());
// fargeFilter.setScaleCalibration(0, 1);
// lysFilter.setScaleCalibration(0, 1);
//
// fargeFilter.setOffsetCalibration(1);
// lysFilter.setOffsetCalibration(1);
//
// fargeFilter.setCalibrationType(1);
// lysFilter.setCalibrationType(1);
//
// fargeFilter.startCalibration();
// lysFilter.startCalibration();
//
// mover.calibrate();
// fargeFilter.stopCalibration();
// lysFilter.stopCalibration();
// float[] offsetC = fargeFilter.getOffsetCorrection();
// float[] scaleC = fargeFilter.getScaleCorrection();
// float[] loffsetC = lysFilter.getOffsetCorrection();
// float[] lscaleC = lysFilter.getScaleCorrection();
// System.out.println(offsetC[0] + ", " + loffsetC[0]);
// System.out.println(scaleC[0] + ", " + lscaleC[0]);
// fargeFilter.save("farge.cal");
// lysFilter.save("lys.cal");
// }

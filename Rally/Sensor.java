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
import lejos.robotics.filter.LinearCalibrationFilter;


class Sensor{
	private SampleProvider fargeLeser;
	private SampleProvider lysLeser;
	private float[] fargeSample;
	private float[] lysSample;
	private EV3ColorSensor fargesensor;
	private NXTLightSensor lyssensor;
	private double fargeValue = 0.0;
	private double lysValue = 0.0;
   
    public Sensor(){
        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); //
        Port s4 = brick.getPort("S4"); //
        fargesensor = new EV3ColorSensor(s1);
		fargeLeser = fargesensor.getRGBMode();
		fargeSample = new float[fargeLeser.sampleSize()];
        lyssensor = new NXTLightSensor(s4);
		lysLeser = lyssensor.getAmbientMode();
		lysSample = new float[lysLeser.sampleSize()];
		//LinearCalibrationFilter filter = 
		//LinearCalibrationFilter kan brukes for å kalibrere sensorene.
    }
    
    public boolean isBlackL() {
		fargeLeser.fetchSample(fargeSample, 0);
		fargeValue = fargeSample[0];
		if (fargeSample[0] <= 0.01 && fargeSample[0] != 0.0) {
			return true;
        } else {
            return false;
        }
    }
    
    public boolean isBlackR() {
        lysLeser.fetchSample(lysSample, 0);
        lysValue = lysSample[0];
		if (lysSample[0] <= 0.01 && lysSample[0] != 0.0) {
            return true;
        } else {
            return false;
        }
    }
    public double getFargeValue() {
    	return fargeValue;
    	}
    public double getLysValue() {
    	return lysValue;
    	}
    
}
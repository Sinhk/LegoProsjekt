/**
*
*
*
*/

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;
import java.util.ArrayList;


class Sensor{
	private SampleProvider fargeLeser1;
	private SampleProvider fargeLeser2;
	private float[] fargeSample1;
	private float[] fargeSample2;
   
    public Sensor(){
        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); //
        Port s2 = brick.getPort("S2"); //
        Port s3 = brick.getPort("S3"); //
        Port s4 = brick.getPort("S4"); //
        System.out.println("Init Sensor 1");
		EV3ColorSensor fargesensor1 = new EV3ColorSensor(s1);
		fargeLeser1 = fargesensor1.getRGBMode();
		fargeSample1 = new float[fargeLeser1.sampleSize()];
		//ArrayList<String> list = new ArrayList<String>();
		//ArrayList<String> modes1 = fargesensor1.getAvailableModes();
		//System.out.println(modes1);
        //fargesensor1.setCurrentMode(1);
        System.out.println("Init Sensor 2");
		NXTLightSensor fargesensor2 = new NXTLightSensor(s4);
		fargeLeser2 = fargesensor2.getAmbientMode();
		fargeSample2 = new float[fargeLeser2.sampleSize()];
		//ArrayList<String> modes2 = fargesensor2.getAvailableModes();
		//System.out.println(modes2);
        //fargesensor2.setCurrentMode(1);
    }
    
    public boolean isBlackL() {
		fargeLeser1.fetchSample(fargeSample1, 0);
		if (fargeSample1[0] <= 0.01 && fargeSample1[0] != 0.0) {
			System.out.println("Sensor 1: Svart: " + fargeSample1[0]);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isBlackR() {
		System.out.println(fargeSample2[0]);
        fargeLeser2.fetchSample(fargeSample2, 0);
		if (fargeSample2[0] <= 0.01 && fargeSample2[0] != 0.0) {
            System.out.println("Sensor 2: Svart: "+ fargeSample2[0]);
            return true;
        } else {
            return false;
        }
    }
    
    
}
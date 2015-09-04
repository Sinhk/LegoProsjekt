/**
* Vasker.java    ost
*
* Styreprogram for Hitratunnelvasker
*/

import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;

class Vasker{
	public static void main (String[] arg) throws Exception  {
		try{
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // lydsensor
		Port s2 = brick.getPort("S2"); // trykksensor
		Port s3 = brick.getPort("S3"); // fargesensor
			
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
			
		//lydsensor
		NXTSoundSensor lydsensor = new NXTSoundSensor(s1);
		
		
		
		//trykksensor
		SampleProvider trykksensor = new EV3TouchSensor(s2);
			float[] trykkSample = new float[trykksensor.sampleSize()];
		
		
		//fargesensor
		EV3ColorSensor fargesensor = new EV3ColorSensor(s3);
		SampleProvider fargeLeser = fargesensor.getColorIDMode();
		
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi
		
		boolean fortsett  = true;
		
		while(fortsett) {
			
			fargeLeser.fetchSample(fargeSample, 0);  // hent verdi fra fargesensor
      		lcd.drawString("Farge: " + fargeSample[0], 0, 3);
			
			if (trykkSample != null && trykkSample.length > 0){
				trykksensor.fetchSample(trykkSample, 0);
				if (trykkSample[0] > 0){
					System.out.println("Avslutter");
					fortsett = false;
	 			}
  	 		}
		}			
			
			
			
		}catch(Exception e){
			System.out.println("Feil: " + e);
		}	
		
	}
}
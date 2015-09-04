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
		Port lydPort = brick.getPort("S1"); // lydsensor
		Port trykkPort = brick.getPort("S3"); // trykksensor
		Port fargePort = brick.getPort("S2"); // fargesensor
			
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		//lydsensor
		SampleProvider lydsensor = new NXTSoundSensor(lydPort).getDBMode();
		float[] lydSample = new float[lydsensor.sampleSize()];
		
		//trykksensor
		SampleProvider trykksensor = new EV3TouchSensor(trykkPort);
		float[] trykkSample = new float[trykksensor.sampleSize()];
		
		
		//fargesensor
		EV3ColorSensor fargesensor = new EV3ColorSensor(fargePort);
		//.getColorIDMode();
		//NXTColorSensor fargesensor = new NXTColorSensor(fargePort);
		//fargesensor.getColorIDMode();
		SampleProvider fargeLeser = fargesensor.getColorIDMode();
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi
		
		
		boolean fortsett  = true;
		
		while(fortsett) {
			lydsensor.fetchSample(lydSample, 0);
			lcd.drawString("Lydnivaa: " + lydSample[0], 0, 2);
			
			
			fargeLeser.fetchSample(fargeSample, 0);  // hent verdi fra fargesensor
      		lcd.drawString("Farge: " + fargeSample[0], 0, 4);
			
			
			if (trykkSample != null && trykkSample.length > 0){
				trykksensor.fetchSample(trykkSample, 0);
				if (trykkSample[0] > 0){
					System.out.println("Avslutter");
					fortsett = false;
	 			}
  	 		}
			lcd.refresh();
			Thread.sleep(1000);
			lcd.clear();
		}			
			
			
			
		}catch(Exception e){
			System.out.println("Feil: " + e);
		}	
		
	}
}
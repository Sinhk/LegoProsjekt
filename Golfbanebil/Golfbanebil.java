/**
* Golfbanebil.java 
* 
* 
* 
* Styreprogram for Golfbanebil
*/

import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
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
import lejos.robotics.navigation.DifferentialPilot;

class Golfbanebil{
    public static void main (String[] arg) throws Exception  {
        boolean fortsett = true;
        Port s1 = brick.getPort("S1"); //
        Port s2 = brick.getPort("S2"); //
        Port s3 = brick.getPort("S3"); //
        Port s4 = brick.getPort("S4"); //
        
        EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		
		
		SampleProvider trykksensor1 = new EV3TouchSensor(s1);
		float[] trykkSample1 = new float[trykksensor1.sampleSize()];
		SampleProvider trykksensor2 = new EV3TouchSensor(s2);
		float[] trykkSample2 = new float[trykksensor2.sampleSize()];
		
		EV3ColorSensor ultrasensor = new EV3ColorSensor(s3);
		SampleProvider ultraLeser = ultrasensor.getDistanceMode();
		float[] ultraSample = new float[ultraLeser.sampleSize()];
        
        while(fortsett) {
            lcd.Clear()
            ultraLeser.fetchSample(ultraSample, 0);
			lcd.drawString("Avstand: " + ultraSample[0], 0,2);
            
            
            
            trykksensor1.fetchSample(trykkSample1, 0);
			trykksensor2.fetchSample(trykkSample2, 0);
			if(trykkSample2[0] > 0||trykkSample1[0] > 0){
				fortsett = false;
			}
            
        }
        
    }
}
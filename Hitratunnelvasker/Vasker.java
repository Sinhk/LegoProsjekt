package Hitratunnelvasker;

/**
* Vasker.java 
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
import lejos.robotics.navigation.DifferentialPilot;

class Vasker{
	public static void main (String[] arg) throws Exception  {
		try{
		Brick brick = BrickFinder.getDefault();
		DifferentialPilot  vaskar = new DifferentialPilot (2.1f, 4.4f, Motor.A, Motor.D, true);
		Port lydPort = brick.getPort("S1"); // lydsensor
		Port trykkPort1 = brick.getPort("S3"); // trykksensor1
		Port trykkPort2 = brick.getPort("S4"); // trykksensor2
		Port fargePort = brick.getPort("S2"); // fargesensor
		
		// Setter hastighet på roboten
		int speed1 = 100;
		int speed2 = 95;
		boolean right = true; 
		Motor.A.setSpeed(speed1);
		Motor.D.setSpeed(speed2);

		Motor.C.setSpeed(900);
			
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		//lydsensor
		SampleProvider lydsensor = new NXTSoundSensor(lydPort).getDBMode();
		float[] lydSample = new float[lydsensor.sampleSize()];
		
		//trykksensor
		SampleProvider trykksensor1 = new EV3TouchSensor(trykkPort1);
		float[] trykkSample1 = new float[trykksensor1.sampleSize()];
		SampleProvider trykksensor2 = new EV3TouchSensor(trykkPort2);
		float[] trykkSample2 = new float[trykksensor2.sampleSize()];
		
		//fargesensor
		EV3ColorSensor fargesensor = new EV3ColorSensor(fargePort);
		SampleProvider fargeLeser = fargesensor.getColorIDMode();
		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi
		
		
		boolean fortsett  = true;
		lcd.drawString("Press key to start", 0, 3);
		keys.waitForAnyPress();
		Motor.C.forward();
		while(fortsett) {
			Motor.A.forward();
			Motor.D.forward();			
			//Stop for lyd
			lydsensor.fetchSample(lydSample, 0);
			if (lydSample[0] > 0.6) {
				vaskar.stop();
				Thread.sleep(3000);
				Motor.D.forward();
				Motor.A.forward();
			}
			
			//Bruker trykksensor for kursjustering
			if (trykkSample1 != null && trykkSample1.length > 0){
				trykksensor1.fetchSample(trykkSample1, 0);
				if(trykkSample1[0] > 0){
					Motor.A.stop();	
					Thread.sleep(50);
					Motor.A.forward();					
	 			}
  	 		}
			if (trykkSample2 != null && trykkSample2.length > 0){
				trykksensor2.fetchSample(trykkSample2, 0);
				if(trykkSample2[0] > 0){
					Motor.D.stop();	
					Thread.sleep(50);
					Motor.D.forward();
	 			}
  	 		}
			
			//Snur i enden av tunnelen, fargesensor
			if(fargesensor.getColorID() != 6){
				Motor.C.stop();
				if (right){
					Motor.A.backward();
					Thread.sleep(3800);
					Motor.A.forward();
					Motor.A.setSpeed(speed2);
					Motor.D.setSpeed(speed1);
					right = false;
				}else{
					Motor.D.backward();
					Thread.sleep(3800);
					Motor.D.forward();
					Motor.A.setSpeed(speed1);
					Motor.D.setSpeed(speed2);
					right = true;
				}
				Motor.C.forward();
			}
			
			//avslutter når begge trykksensor aktivers samtidig
			trykksensor1.fetchSample(trykkSample1, 0);
			trykksensor2.fetchSample(trykkSample2, 0);
			if(trykkSample2[0] > 0 && trykkSample1[0] > 0){
				Motor.A.stop();	
				Motor.D.stop();	
				fortsett = false;
			}
		}				
			
		}catch(Exception e){
			System.out.println("Feil: " + e);
		}	
		
	}
}
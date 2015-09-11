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
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

class Golfbanebil{
    public static void main (String[] arg) throws Exception  {
        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); //
        Port s2 = brick.getPort("S2"); //
        Port s3 = brick.getPort("S3"); //
        Port s4 = brick.getPort("S4"); //
        
        DifferentialPilot pilot = new DifferentialPilot (5.7f, 11.5f, Motor.A, Motor.D, true);
		pilot.setTravelSpeed(30);
		
        EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		
		//Trykksensor
		SampleProvider trykksensor1 = new EV3TouchSensor(s1);
		float[] trykkSample1 = new float[trykksensor1.sampleSize()];
		SampleProvider trykksensor2 = new EV3TouchSensor(s4);
		float[] trykkSample2 = new float[trykksensor2.sampleSize()];
		
		
		//Ultralyssensor
		NXTUltrasonicSensor ultrasensor1 = new NXTUltrasonicSensor(s3);
		SampleProvider ultraLeser1 = ultrasensor1.getDistanceMode();
		float[] ultraSample1 = new float[ultraLeser1.sampleSize()];
		
		EV3UltrasonicSensor ultrasensor2 = new EV3UltrasonicSensor(s2);
		SampleProvider ultraLeser2 = ultrasensor2.getDistanceMode();
		float[] ultraSample2 = new float[ultraLeser2.sampleSize()];
       
        // Setter hastighet på roboten
        int speed1 = 70;
    	int speed2 = 70;
        Motor.A.setSpeed(speed1);
    	Motor.D.setSpeed(speed2);
    	
    	
        boolean fortsett = true;
        while(fortsett) {
           
            ultraLeser2.fetchSample(ultraSample2, 0);
			 ultraLeser1.fetchSample(ultraSample1, 0);
			if(ultraSample1[0] <= 0.1 && ultraSample2[0] <= 0.1) {
                Motor.A.backward();	
				Thread.sleep(1000);
            }else if(ultraSample2[0] <= 0.1) {
                Motor.A.stop();
                Thread.sleep(300);
                Motor.A.forward();
		
            }else if(ultraSample1[0] <= 0.1) {
                Motor.D.stop();
                Thread.sleep(300);
	        	Motor.D.forward();
            }
			Motor.A.forward();
			Motor.D.forward();
            
			//lcd.drawString("Avstand: " + ultraSample2[0], 0,2);
			//lcd.drawString("Avstand: " + ultraSample1[0], 0,5);
            //Thread.sleep(400);
            
            trykksensor1.fetchSample(trykkSample1, 0);
			trykksensor2.fetchSample(trykkSample2, 0);
			if(trykkSample2[0] > 0||trykkSample1[0] > 0){
		    	Motor.A.stop();	
				Motor.D.backward();
				Thread.sleep(300);
				//fortsett = false;
			}
       

        }
        
    }
}
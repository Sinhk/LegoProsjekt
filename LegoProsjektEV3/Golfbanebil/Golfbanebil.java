package Golfbanebil;


/**
* Golfbanebil.java 
* 
* 
* Styreprogram for Golfbanebil
*/
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;

class Golfbanebil{
    public static void main (String[] arg) throws Exception  {
        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); //
        Port s2 = brick.getPort("S2"); //
        Port s3 = brick.getPort("S3"); //
        Port s4 = brick.getPort("S4"); //
        
        DifferentialPilot pilot = new DifferentialPilot (5.7f, 11.5f, Motor.A, Motor.D, false);
		pilot.setTravelSpeed(10);
		pilot.setRotateSpeed(40);
		
		// Trykksensor
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
        int speed1 = 130;
    	int speed2 = 130;
        Motor.A.setSpeed(speed1);
    	Motor.D.setSpeed(speed2);
    	
    	
        boolean fortsett = true;
        while(fortsett) {
																
			trykksensor1.fetchSample(trykkSample1, 0);
			trykksensor2.fetchSample(trykkSample2, 0);
			if(trykkSample2[0] > 0){
			    pilot.backward();
			    Thread.sleep(500);
			    pilot.stop();
			    pilot.rotate(-30);
			    pilot.forward();
			} 
			if(trykkSample1[0] > 0){
			    pilot.backward();
			    Thread.sleep(300);
			    pilot.stop();
			    pilot.rotate(30);
			    pilot.forward();
			}
			
            ultraLeser2.fetchSample(ultraSample2, 0);
			ultraLeser1.fetchSample(ultraSample1, 0);            
            if(ultraSample2[0] <= 0.1) {
                if (ultraSample1[0] <= 0.15){
                    Motor.A.backward();	
				    Thread.sleep(1000);  
				    Motor.A.forward();
                }else{
					int i = 0;
                    while(ultraSample2[0] <= 0.1){
						Motor.A.stop();
						Thread.sleep(300);
						Motor.A.forward();
						if (i >= 5){
							pilot.backward();
							Thread.sleep(300);
							pilot.stop();
							pilot.rotate(30);
							pilot.forward();
						}
						i++;
						ultraLeser2.fetchSample(ultraSample2, 0);
					} 
                }
            }else if(ultraSample1[0] <= 0.1) {
                if (ultraSample2[0] <= 0.15){
                    Motor.A.backward();	
				    Thread.sleep(1000);
				    Motor.A.forward();
                }else{
					int i = 0;
                    while(ultraSample1[0] <= 0.1){
						Motor.D.stop();
						Thread.sleep(300);
						Motor.D.forward();
						if (i >= 5){
							pilot.backward();
							Thread.sleep(300);
							pilot.stop();
							pilot.rotate(-30);
							pilot.forward();
						}
						i++;
						ultraLeser1.fetchSample(ultraSample1, 0);
					}
                    
                }
            }
			Motor.A.forward();
			Motor.D.forward();
        }
        
    }
}
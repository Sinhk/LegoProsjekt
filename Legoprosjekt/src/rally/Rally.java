package rally;

/**
* Rally.java 
* 
* 
* 
* Styreprogram for IIE-rally
*/


import lejos.hardware.lcd.*;
import lejos.hardware.ev3.EV3;
import lejos.hardware.BrickFinder;


class Rally{
	public static void main (String[] arg) throws Exception  {

		System.out.println("Init Driver");
		Driver motor = new Driver();
		System.out.println("Init Sensor");
		Sensor sensor = new Sensor();
		int i=0;
		boolean fortsett = true;
		System.out.println("Start motor");
		motor.forward(1);
		while(fortsett) {
			
			if(i == 0 ||i== 2||i== 3||i== 5||i== 6||i == 8||i==9||i==11||i==12) {
				if(i==0||i==3||i==6||i==9||i==12) {
						motor.forward(1);
					if(sensor.isBlackL()) {
						motor.turnRight();
					}
					if(sensor.isBlackR()) {
						i++;
						motor.forward(0);
						Thread.sleep(2000);
					}
				}else if (i==2||i==5||i==8||i==11){
					motor.forward(2);
					if(sensor.isBlackR()) {
						motor.turnLeft();
					}
					if(sensor.isBlackL()) {
						i++;
						motor.forward(0);
						Thread.sleep(2000);
					}
					
				}
			}else {
				motor.forward(2);
				if(sensor.isBlackR()) {
					motor.turnLeft();
				}
				if(sensor.isBlackL()) {
					i++;
				}
			}
			
		}
	}
}

/**
* Rally.java 
* 
* 
* 
* Styreprogram for IIE-rally
*/


import lejos.hardware.lcd.*;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.hardware.sensor.SensorModes;


class Rally{
    public static void main (String[] arg) throws Exception  {

        EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
       
        // Setter hastighet på roboten
        int speed1 = 130;
    	int speed2 = 130;
        Motor.A.setSpeed(speed1);
    	Motor.D.setSpeed(speed2);
    	
    	
        boolean fortsett = true;
        while(fortsett) {
   			Motor.A.forward();
			Motor.D.forward();
            fortsett = false;
			}
       

        }
        
    }
}
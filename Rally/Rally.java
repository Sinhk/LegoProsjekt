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
import lejos.hardware.Keys;
import lejos.hardware.sensor.SensorModes;


class Rally{
    public static void main (String[] arg) throws Exception  {

        EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
       
       
    	
    	
        boolean fortsett = true;
        while(fortsett) {
            fortsett = false;
			
       

        }
        
    }
}
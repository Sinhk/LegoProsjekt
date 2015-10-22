package HelloWorld;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class HelloWorld {
	public static void main(String[] args)  throws Exception{
		Thread.sleep(500);

		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		lcd.drawString("Hello World!", 4, 4);
		
		
		
		
		
		
		
		lcd.drawString("Bye World!", 4, 4);
		
		
		keys.waitForAnyPress();
	}
}
/*    MinBilPilot.java G.S 2011 - 08 - 16
* Program som styrer en bil med 2 motorer. Bilen oppfører seg slik:
* 1. kjør framover
* 2. Rygg
* 3. Sving høyre
* 4. Endre hastighet på motorene
* 5. Kjør framover igjen
*
* Programmet bruker klassen TachoPilot som er egnet til å styre en robot med to motorer
*/
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class FirkantBil
{
	public static void main (String[] args)  throws Exception
	{
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		
		lcd.drawString("Hello World!", 4, 4);
		int distanse = 20;

		DifferentialPilot  Mustang = new DifferentialPilot (5.7f, 11.5f, Motor.A, Motor.C, true);
		Mustang.setTravelSpeed(450);
	 	Mustang.travel(distanse);  // kjør framover
	 	Mustang.rotate(90);
	 	Mustang.travel(distanse);  // kjør framover
	 	Mustang.rotate(90);
	 	Mustang.travel(distanse);  // kjør framover
	 	Mustang.rotate(90);
	 	Mustang.travel(distanse);  // kjør framover
	 	Mustang.rotate(90);
		
		lcd.drawString("Bye World!", 4, 4);

	}
}


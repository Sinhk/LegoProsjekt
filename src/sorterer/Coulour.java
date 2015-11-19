package sorterer;

import lejos.nxt.ColorSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.robotics.Color;
import lejos.nxt.addon.*;
//Button.RIGHT.isDown()

public class Coulour {
    private final static int RED = 0;
    private final static int BLUE = 7;// 2
    private static float speed = 350;			//setter heishastighet
    private static float speed2 = 200;			//setter roteringshastighet
    private static int vinkel1 = 365;			//setter heis rotasjon
    private static int vinkel2 = 30;
    private static int vinkel3 = 60;			// setter sortererens rotasjonsvinkel
    private static int vent = 1500;				// setter ventetid for hvorlenge sortereren skal vente p� at ballen skall lande i b�sen sin
    private static int vent2 = 1000;
    private static NXTRegulatedMotor heis = Motor.A;		//d�per motor A til heis
    private static NXTRegulatedMotor inndeler = Motor.B;	// d�per motor B til inndeler
    
    
    public static void main(String[] arg) throws Exception {
	LCD.drawString("Klar...", 0, 0);						// skriver ut at programmet er klart til � kj�re
	// Button.waitForAnyPress();
	BTConnectNXT btc = new BTConnectNXT();
	Thread btThread = new Thread(btc);
	btThread.start();
	heis.resetTachoCount();
	inndeler.resetTachoCount();
	heis.setSpeed(speed);									//setter heishastigheten
	inndeler.setSpeed(speed2);								//setter indeler hastigheten
	ColorHTSensor colorSensor = new ColorHTSensor(SensorPort.S2);		// forteller NXT I hvilken 
	// colorSensor.initBlackLevel();
	boolean fortsett = true;
	
	zero();
	
	while (fortsett) {										// starter l�kken for sortering og kalibrering 
	    if (Button.RIGHT.isDown()) {
		colorSensor.initBlackLevel();						// setter kalibrerer til svartniv�er
	    }
	    if (Button.LEFT.isDown()) {
	    colorSensor.initWhiteBalance();						// setter kalibrerer til hvitbalanse
	    }

	    if (btc.getReady() || Button.ENTER.isDown()) {		
		LCD.clear();
		LCD.drawString("sorterer", 0, 2);
		int color = colorSensor.getColorID();
		Color rgb = colorSensor.getColor();
		LCD.drawString("R: " + rgb.getRed() + " G: " + rgb.getGreen() + " B: " + rgb.getBlue(), 0, 5);		//skreiver RGB verdien p� LCD skjermen
		LCD.drawString("color" + color, 1, 4);																//skriver ut farge ID
		sortBall(color);
	    }
	    if (btc.getDone() || Button.ESCAPE.isDown()) {														// stopper l�kken om escape knappen blir brukt 
		fortsett = false;																							

	    }
	}
	btc.close();																							//stopper bluetooth tilkobling
	btThread.join();
	System.exit(0);
    }

    private static void sortBall(int color) {																//sorterer baser p� farge
	switch (color) {
	case BLUE:
	    inndeler.rotateTo(vinkel3);
	    break;
	case RED:
	    inndeler.rotateTo(-vinkel3);
	    break;
	default:
	    break;
	}
	heis.rotateTo(vinkel1 + vinkel2);																		//algoritmen for � returnere heisen til 0 possisjon
	Delay.msDelay(vent);
	inndeler.rotateTo(0, true);
	heis.rotateTo(0);
	inndeler.stop(true);
	heis.stop();
    }

    /* Exprimental. Try to reset motors */
    private static void zero() {
	heis.setSpeed(50);
	heis.backward();
	while (!heis.isStalled()) {
	    Delay.msDelay(100);
	}
	heis.stop();
	heis.resetTachoCount();
	heis.setSpeed(speed);
    }
}

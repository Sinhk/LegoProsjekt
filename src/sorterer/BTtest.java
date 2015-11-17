package sorterer;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

class BTtest {

    public static void main(String[] arg) throws Exception {
	EV3 ev3 = (EV3) BrickFinder.getLocal();
	TextLCD lcd = ev3.getTextLCD();
	BTConnectEV3 btc = new BTConnectEV3();
	btc.start();
	boolean fortsett = true;
	lcd.clear();
	lcd.drawString("Trykk venstre for sortering", 0, 2);
	lcd.drawString("Høyre for avslutt", 0, 4);
	while (fortsett) {

	    if (Button.LEFT.isDown()) {
		btc.setReady();

	    }
	    if (Button.RIGHT.isDown()) {
		btc.setDone();
		btc.close();
		Thread.sleep(200);
		fortsett = false;

	    }

	}

	// lcd.clear();
	// System.exit(0);
    }
}
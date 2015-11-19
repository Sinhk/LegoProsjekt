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
	Thread btThread = new Thread(btc);
	btThread.start();
	boolean fortsett = true;
	lcd.clear();
	lcd.drawString("Trykk venstre for sortering", 0, 2);
	lcd.drawString("Hoyre for avslutt", 0, 4);
	while (fortsett) {
	    int button = Button.waitForAnyPress();

	    if (button == Button.ID_LEFT) {
		btc.setReady();
	    } else if (button == Button.ID_RIGHT) {
		btc.setDone();
		btThread.join();
		fortsett = false;
	    }
	}
	lcd.clear();
	System.exit(0);
    }
}
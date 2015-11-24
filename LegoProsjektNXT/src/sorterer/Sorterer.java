package sorterer;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Sorterer {

    public static void main(String[] arg) throws Exception {
	LCD.drawString("Ready...", 0, 0);

	// Starter thread for bluetooth kommunikasjon
	BTConnectNXT btc = new BTConnectNXT();
	Thread btThread = new Thread(btc);
	btThread.start();

	Controller cont = new Controller();

	// colorSensor.initBlackLevel();
	boolean fortsett = true;

	// nullstill motorer

	while (fortsett) {
	    if (Button.RIGHT.isDown()) {
		// kalibrerer svartnivåer
		cont.initBlackLevel();
	    }
	    if (Button.LEFT.isDown()) {
		// kalibrerer hvitbalanse
		cont.initWhiteBalance();
	    }

	    if (btc.getReady() || Button.ENTER.isDown()) {
		cont.sortBall();
	    }

	    if (btc.getDone() || Button.ESCAPE.isDown()) {
		// stopper løkken
		fortsett = false;
	    }
	}
	// stopper bluetooth thread og venter til den avslutter
	System.out.println("Shutdown...");
	btc.close();
	btThread.join();
	System.exit(0);
    }

}

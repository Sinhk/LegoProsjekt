package sorterer;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.robotics.Color;
import lejos.nxt.addon.*;

public class Sorterer {
    // Konstanter for faregverdier
    private final static int BLACK = 1;
    private final static int RED = 2;
    private final static int BLUE = 3;
    // Grense for svartverdi
    private final static int THRESHOLD = 15;

    // Fartsvariabler
    private static float speedHeis = 450;
    private static float speedInndeler = 150;

    // rotasjonvinkler
    private static int vinkelHeis = 420;
    private static int vinkelKalibrering = 60;
    private static int vinkelInndeler = 50;

    // ventetid for sortereren
    private static int vent = 1000;

    // navngir motorer
    private static NXTRegulatedMotor heis = Motor.A;
    private static NXTRegulatedMotor inndeler = Motor.B;

    // Sensor
    private static ColorHTSensor colorSensor;

    public static void main(String[] arg) throws Exception {
	LCD.drawString("Klar...", 0, 0);

	// Starter thread for bluetooth kommunikasjon
	BTConnectNXT btc = new BTConnectNXT();
	Thread btThread = new Thread(btc);
	btThread.start();

	colorSensor = new ColorHTSensor(SensorPort.S2);
	// colorSensor.initBlackLevel();
	boolean fortsett = true;

	// nullstill motorer
		zero();
	while (fortsett) {
	    if (Button.RIGHT.isDown()) {
		// kalibrerer svartnivåer
		colorSensor.initBlackLevel();
	    }
	    if (Button.LEFT.isDown()) {
		// kalibrerer hvitbalanse
		colorSensor.initWhiteBalance();
	    }

	    if (btc.getReady() || Button.ENTER.isDown()) {
		int color = getColor();
		sortBall(color);
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

    /**
     * Leser farge og finner ut om ballen er rød, blå eller svart
     */
    private static int getColor() {
	Color rgb = colorSensor.getColor();
	// skriver ut farge verdier
	// TODO Ta vekk (kommenter ut) denne når vi er ferdig å teste
	LCD.drawString("R: " + rgb.getRed() + " G: " + rgb.getGreen() + " B: " + rgb.getBlue(), 0, 5);

	int red = rgb.getRed();
	int green = rgb.getGreen();
	int blue = rgb.getBlue();

	if (red < THRESHOLD && green < THRESHOLD && blue < THRESHOLD) {
	    return BLACK;
	} else if (red > blue) {
	    return RED;
	} else if (red < blue) {
	    return BLUE;
	}
	return 0;
    }

    /**
     * Sorterer ball basert på farge. Styrer motorer
     * 
     * @param color
     *            Farge på ball
     */
    private static void sortBall(int color) {
	switch (color) {
	case BLUE:
	    inndeler.rotateTo(vinkelInndeler, true);
	    break;
	case RED:
	    inndeler.rotateTo(-vinkelInndeler, true);
	    break;
	default:
	    break;
	}

	heis.rotateTo(vinkelHeis);
	Delay.msDelay(vent);
	inndeler.rotateTo(0, true);
	heis.rotateTo(10);
	inndeler.stop(true);
	heis.flt();
    }

    /**
     * Reset motorer til startposisjon
     */
    private static void zero() {
	inndeler.setStallThreshold(2, 10);
	inndeler.setSpeed(20);
	inndeler.forward();
	// Venter til motor møter motstand
	while (!inndeler.isStalled()) {
	}
	inndeler.stop();
	inndeler.setStallThreshold(50, 1000);
	inndeler.rotate(-vinkelKalibrering);
	inndeler.resetTachoCount();
	inndeler.setSpeed(speedInndeler);

	heis.setSpeed(50);
	heis.backward();
	// Venter til motor møter motstand
	while (!heis.isStalled()) {
	}
	heis.flt();
	heis.resetTachoCount();
	heis.setSpeed(speedHeis);
	heis.flt();
    }
}

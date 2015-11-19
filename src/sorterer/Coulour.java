package sorterer;

import lejos.nxt.ColorSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.addon.*;
import lejos.robotics.RegulatedMotor;
//Button.RIGHT.isDown()

public class Coulour {
    private final static int RED = 0;
    private final static int BLUE = 7;// 2
    private static float speed = 250;
    private static float speed2 = 200;
    private static int vinkel1 = 320;
    private static int vinkel2 = 30;
    private static int vinkel3 = 40;
    private static int vent = 1500;
    private static int vent2 = 1000;
    private static NXTRegulatedMotor heis = Motor.A;
    private static NXTRegulatedMotor inndeler = Motor.B;

    public static void main(String[] arg) throws Exception {
	LCD.drawString("Klar...", 0, 0);
	// Button.waitForAnyPress();
	BTConnectNXT btc = new BTConnectNXT();
	Thread btThread = new Thread(btc);
	btThread.start();
	heis.resetTachoCount();
	inndeler.resetTachoCount();
	heis.setSpeed(speed);
	inndeler.setSpeed(speed2);
	ColorHTSensor colorSensor = new ColorHTSensor(SensorPort.S2);
	colorSensor.initBlackLevel();
	boolean fortsett = true;

	while (fortsett) {
	    if (Button.RIGHT.isDown()) {
		colorSensor.initBlackLevel();
	    }

	    if (btc.getReady() || Button.ENTER.isDown()) {
		LCD.clear();
		LCD.drawString("sorterer", 0, 2);
		int color = colorSensor.getColorID();
		LCD.drawString("color" + color, 1, 4);
		sort(color);
	    }
	    if (btc.getDone() || Button.ESCAPE.isDown()) {
		fortsett = false;

	    }
	}
	btc.close();
	btThread.join();
	System.exit(0);
    }

    private static void sort(int color) {
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
	heis.rotateTo(vinkel1 + vinkel2);
	inndeler.rotateTo(0, true);
	heis.rotateTo(0);
	inndeler.stop(true);
	heis.stop();
    }

    /* Exprimental. Try to reset motors */
    private static void zero() {
	heis.backward();
	while (!heis.isStalled()) {
	    Delay.msDelay(100);
	}
	heis.stop();
	heis.resetTachoCount();
    }
}

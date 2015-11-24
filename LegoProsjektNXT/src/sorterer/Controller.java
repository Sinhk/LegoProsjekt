package sorterer;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.Color;
import lejos.util.Delay;

public class Controller {
    // Konstanter for faregverdier
    private final int BLACK = 1;
    private final int RED = 2;
    private final int BLUE = 3;
    // Grense for svartverdi
    private final int THRESHOLD = 15;

    // Fartsvariabler
    private float speedLift = 450;
    private float speedSwitcher = 150;

    // rotasjonvinkler
    private int angleLift = 420;
    private int angleCalibrate = 60;
    private int angleSwitcher = 50;

    // ventetid for sortereren
    private static int wait = 1000;

    // navngir motorer
    private NXTRegulatedMotor lift = Motor.A;
    private NXTRegulatedMotor switcher = Motor.B;

    // Sensor
    private ColorHTSensor colorSensor;

    public Controller() {
	colorSensor = new ColorHTSensor(SensorPort.S2);
	zero();
    }

    /**
     * Leser farge og finner ut om ballen er rød, blå eller svart
     */
    public int getColor() {
	Color rgb = colorSensor.getColor();
	// skriver ut farge verdier
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
     */
    public void sortBall() {
	int color = getColor();
	switch (color) {
	case BLUE:
	    switcher.rotateTo(angleSwitcher, true);
	    break;
	case RED:
	    switcher.rotateTo(-angleSwitcher, true);
	    break;
	default:
	    break;
	}

	lift.rotateTo(angleLift);
	Delay.msDelay(wait);
	switcher.rotateTo(0, true);
	lift.rotateTo(10);
	switcher.stop(true);
	lift.flt();
    }

    /**
     * Reset motorer til startposisjon
     */
    public void zero() {
	switcher.setStallThreshold(2, 10);
	switcher.setSpeed(20);
	switcher.forward();
	// Venter til motor møter motstand
	while (!switcher.isStalled()) {
	}
	switcher.stop();
	switcher.setStallThreshold(50, 1000);
	switcher.rotate(-angleCalibrate);
	switcher.resetTachoCount();
	switcher.setSpeed(speedSwitcher);

	lift.setSpeed(50);
	lift.backward();
	// Venter til motor møter motstand
	while (!lift.isStalled()) {
	}
	lift.flt();
	lift.resetTachoCount();
	lift.setSpeed(speedLift);
	lift.flt();
    }

    public void initBlackLevel() {
	colorSensor.initBlackLevel();
    }

    public void initWhiteBalance() {
	colorSensor.initWhiteBalance();
    }
}

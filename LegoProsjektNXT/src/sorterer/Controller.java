package sorterer;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorHTSensor;
import lejos.robotics.Color;
import lejos.util.Delay;

//TODO Find better name
public class Controller {
    // Konstanter for faregverdier
    private final  int BLACK = 1;
    private final  int RED = 2;
    private final  int BLUE = 3;
    // Grense for svartverdi
    private final  int THRESHOLD = 15;

    // Fartsvariabler
    private  float speedHeis = 450;
    private  float speedInndeler = 150;

    // rotasjonvinkler
    private  int vinkelHeis = 420;
    private  int vinkelKalibrering = 60;
    private  int vinkelInndeler = 50;

    // ventetid for sortereren
    private static int vent = 1000;

    // navngir motorer
    private  NXTRegulatedMotor heis = Motor.A;
    private  NXTRegulatedMotor inndeler = Motor.B;

    // Sensor
    private  ColorHTSensor colorSensor;
    
    public Controller(){
	colorSensor = new ColorHTSensor(SensorPort.S2);
	zero();
    }
    
    
    
    
    
    
    /**
     * Leser farge og finner ut om ballen er r�d, bl� eller svart
     */
    public int getColor() {
	Color rgb = colorSensor.getColor();
	// skriver ut farge verdier
	// TODO Ta vekk (kommenter ut) denne n�r vi er ferdig � teste
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
    public void zero() {
	inndeler.setStallThreshold(2, 10);
	inndeler.setSpeed(20);
	inndeler.forward();
	// Venter til motor m�ter motstand
	while (!inndeler.isStalled()) {
	}
	inndeler.stop();
	inndeler.setStallThreshold(50, 1000);
	inndeler.rotate(-vinkelKalibrering);
	inndeler.resetTachoCount();
	inndeler.setSpeed(speedInndeler);

	heis.setSpeed(50);
	heis.backward();
	// Venter til motor m�ter motstand
	while (!heis.isStalled()) {
	}
	heis.flt();
	heis.resetTachoCount();
	heis.setSpeed(speedHeis);
	heis.flt();
    }






    public void initBlackLevel() {
	colorSensor.initBlackLevel();
    }
    public void initWhiteBalance() {
	colorSensor.initWhiteBalance();
    }
}

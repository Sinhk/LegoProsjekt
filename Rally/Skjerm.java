package Rally;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class Skjerm extends Thread {
	TextLCD lcd;
	Sensor sensor;

	public Skjerm(Sensor sensor) {
		this.sensor = sensor;
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		lcd = ev3.getTextLCD();
	}

	public void run() {
		do {
			try {
				lcd.clear();
				lcd.drawString("Farge: " + sensor.getFargeValue(), 0, 2);
				lcd.drawString("Lys: " + sensor.getLysValue(), 0, 4);

				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		} while (!isInterrupted());
	}

}

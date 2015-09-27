package rally;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class Skjerm extends Thread {
	private TextLCD lcd;
	private Sensor sensor;
	private int i = 0;

	public Skjerm(Sensor sensor) {
		this.sensor = sensor;
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		lcd = ev3.getTextLCD();
	}

	public void run() {
		do {
			try {
				lcd.clear();
				lcd.drawString("Farge: " + sensor.getFargeValue(), 0, 0);
				lcd.drawString("Lys: " + sensor.getLysValue(), 0, 1);
				if (i != 0)
					lcd.drawString("i: " + i, 0, 6);

				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		} while (!isInterrupted());
	}

	public void setI(int i) {
		this.i = i;

	}
}

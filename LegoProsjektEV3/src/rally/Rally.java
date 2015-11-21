package rally;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

class Rally {

	public static void main(String[] arg) throws Exception {
		double speed = 53;
		int maxSteer = 160;
		float kP = 80;
		float kI = 1;
		float kD = 400;

		Sensor sensor = new Sensor();
		Mover motor = new Mover(sensor, speed, maxSteer, kP, kI, kD);
		motor.setDaemon(true);
		motor.start();
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		boolean fortsett = true;
		while (fortsett) {

			lcd.clear();
			lcd.drawString("Teller: " + motor.getTeller(), 0, 0);
			lcd.drawString("Right: " + sensor.getRightValue(), 0, 1);
			lcd.drawString("Speed: " + motor.getSpeed(), 0, 2);
			lcd.drawString("P: " + motor.getkP(), 0, 3);
			lcd.drawString("I: " + motor.getkI(), 0, 4);
			lcd.drawString("D: " + motor.getkD(), 0, 5);
			lcd.drawString("Output: " + motor.getOffset(), 0, 6);

			if (Button.LEFT.isDown()) {
				motor.setkP(motor.getkP() - 5);

			}
			if (Button.RIGHT.isDown()) {
				motor.setkP(motor.getkP() + 5);

			}
			if (Button.ESCAPE.isDown())
				fortsett = false;
			Thread.sleep(500);
		}
		motor.interrupt();
		sensor.close();
		// lcd.clear();
		// System.exit(0);
	}
}
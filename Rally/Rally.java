package Rally;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

class Rally {
	public static void main(String[] arg) throws Exception {
		Sensor sensor = new Sensor();
		sensor.start();
		Mover motor = new Mover(sensor, true);
		motor.start();
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		// Skjerm skjerm = new Skjerm(sensor);
		// skjerm.start();
		// motor.calibrate();
		boolean fortsett = true;
		motor.forward(1);
		while (fortsett) {

			lcd.clear();
			lcd.drawString("Farge: " + sensor.getFargeValue(), 0, 0);
			lcd.drawString("Lys: " + sensor.getLysValue(), 0, 1);
			lcd.drawString("Speed: " + motor.getSpeed(), 0, 2);
			lcd.drawString("P: " + motor.getkP(), 0, 3);
			if (Button.LEFT.isDown() && Button.RIGHT.isDown()) {

			}
			Thread.sleep(500);
		}
	}
}

// if (i == 0 || i == 2 || i == 3 || i == 5 || i == 6 || i == 8 || i == 9 || i
// == 11 || i == 12) {
// if (i == 0 || i == 3 || i == 6 || i == 9 || i == 12) {
// motor.forward(1);
// if (sensor.isBlackL()) {
// motor.turnRight();
// Thread.sleep(300);
// motor.forward(1);
// }
// if (sensor.isBlackR()) {
// i++;
// motor.forward(0);
// Thread.sleep(1000);
// }
// } else if (i == 2 || i == 5 || i == 8 || i == 11) {
// motor.forward(2);
// if (sensor.isBlackR()) {
// motor.turnLeft();
// Thread.sleep(300);
// motor.forward(2);
// }
// if (sensor.isBlackL()) {
// i++;
// motor.forward(0);
// Thread.sleep(1000);
// }
//
// }
// } else {
// motor.forward(2);
// if (sensor.isBlackR()) {
// motor.turnLeft();
// }
// if (sensor.isBlackL()) {
// i++;
// }
// }
// skjerm.setI(i);

package rally;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

class Rally {

	public static void main(String[] arg) throws Exception {
		Sensor sensor = new Sensor(true);
		Mover motor = new Mover(sensor);
		sensor.calibrate(motor);
		// sensor.start();
		motor.start();
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		// Skjerm skjerm = new Skjerm(sensor);
		// skjerm.start();
		// motor.calibrate();
		boolean fortsett = true;
		while (fortsett) {

			lcd.clear();
			lcd.drawString("Teller: " + motor.getTeller(), 0, 0);
			lcd.drawString("Lys: " + sensor.getLysValue(), 0, 1);
			lcd.drawString("Speed: " + motor.getSpeed(), 0, 2);
			lcd.drawString("P: " + motor.getkP(), 0, 3);
			lcd.drawString("I: " + motor.getkI(), 0, 4);
			lcd.drawString("D: " + motor.getkD(), 0, 5);
			lcd.drawString("Offset: " + motor.getOffset(), 0, 6);

			if (Button.LEFT.isDown()) {
				motor.setkP(motor.getkP() - 5);

			}
			if (Button.RIGHT.isDown()) {
				motor.setkP(motor.getkP() + 5);

			}
			if (Button.DOWN.isDown() && Button.ENTER.isDown())
				fortsett = false;
			Thread.sleep(500);
		}
		motor.interrupt();
		System.exit(0);
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

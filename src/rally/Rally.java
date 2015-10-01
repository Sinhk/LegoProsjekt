package rally;

import lejos.hardware.Button;

class Rally {

	public static void main(String[] arg) throws Exception {

		Sensor sensor = new Sensor();
		Mover motor = new Mover(sensor);
		motor.setDaemon(true);
		motor.start();
		Button.ESCAPE.waitForPress();
		motor.interrupt();
		sensor.close();
	}
}
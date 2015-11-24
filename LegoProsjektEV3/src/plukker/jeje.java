package plukker;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

class jeje extends Thread {

    // Sensor sensor = new Sensor();
    private static Sensor sensor = new Sensor();
    Pickup pickUp = new Pickup();

    public static void main(String[] arg) throws Exception {

	Button.ENTER.waitForPressAndRelease();
	do {
	    if (sensor.getBall()) {
		Delay.msDelay(80);
		Motor.C.setSpeed(100);
		Motor.B.setSpeed(260);
		Motor.C.rotate(120);
		Delay.msDelay(20);
		Motor.B.rotate(-200);
		Delay.msDelay(500);
		Motor.C.rotate(-120);
		Delay.msDelay(2500);
		Motor.C.rotate(50);
		Motor.B.rotate(200);
		Motor.C.rotate(-50);
		Button.ENTER.waitForPressAndRelease();
	    }

	} while (!interrupted());
    }
}

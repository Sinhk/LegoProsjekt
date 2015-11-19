package plukker;

import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

class jeje {

	private Sensor sensor;
	
	public static void main(String[] arg) throws Exception {
		Motor.C.setSpeed(100);
		Motor.B.setSpeed(260);
		Motor.C.rotate(80);
		Delay.msDelay(20);
		Motor.B.rotate(-200);
		Delay.msDelay(500);
		Motor.C.rotate(-80);
		Delay.msDelay(2500);
		Motor.B.rotate(200);
	}
}
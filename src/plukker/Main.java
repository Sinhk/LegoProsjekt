package plukker;

//import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
//import lejos.hardware.ev3.EV3;

class Main {

	public static void main(String[] arg) throws Exception {
		double speed = 53;
		int maxSteer = 160;
		

		Mover motor = new Mover(null, speed, maxSteer);
		motor.start();
		//EV3 ev3 = (EV3) BrickFinder.getLocal();
		boolean fortsett = true;
		while (fortsett) {
	
			if (Button.ESCAPE.isDown())
				fortsett = false;
			Thread.sleep(500);
		}
		motor.interrupt();

	}
}
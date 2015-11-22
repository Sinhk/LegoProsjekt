package plukker;

//import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
//import lejos.hardware.ev3.EV3;
import lejos.utility.Delay;

class Main {

	public static void main(String[] arg) throws Exception {
		double speed = 30;
		int maxSteer = 160;
		Sensor sensor = new Sensor();
		Mover motor = new Mover(sensor, speed, maxSteer);
		Pickup pickUp = new Pickup();
		BTConnectEV3 nxt = new BTConnectEV3();    
		Thread btThread = new Thread(nxt);
		btThread.start();
		
		motor.start();
		//EV3 ev3 = (EV3) BrickFinder.getLocal();
		boolean fortsett = true;
		while (fortsett) {
		    if (sensor.getBall()) {
			pickUp.pickup();
			//Delay.msDelay(400);
			motor.goHome();
			nxt.setReady();
			motor.resumeSearch();
		    }
			if (Button.ESCAPE.isDown())
			    fortsett = false;
			Thread.sleep(50);
		}
		motor.interrupt();
		nxt.setDone();
		motor.join();
		btThread.join();
		
		
	}
}
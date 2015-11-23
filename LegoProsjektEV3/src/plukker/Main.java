package plukker;

//import lejos.hardware.BrickFinder;
import lejos.hardware.Button;

class Main {

	public static void main(String[] arg) throws Exception {
		double speed = 30;
		int maxSteer = 160;
		Sensor sensor = new Sensor();
		Pickup pickUp = new Pickup();
		Mover motor = new Mover(sensor,pickUp, speed, maxSteer);
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
		motor.terminate();
		nxt.setDone();
		motor.join();
		btThread.join();
		
		
	}
}
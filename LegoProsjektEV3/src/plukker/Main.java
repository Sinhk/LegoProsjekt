package plukker;

//import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

class Main {
    public static void main(String[] arg) throws Exception {
	double speed = 30;
	float searchRadius = 1f;
	float ballSize = 5f;
	Sensor sensor = new Sensor();
	Pickup pickUp = new Pickup();
	Mover motor = new Mover(sensor, pickUp, speed);
	BTConnectEV3 nxt = new BTConnectEV3();
	Thread btThread = new Thread(nxt);
	// btThread.start();
	// motor.testRotate();
	Radar radar = new Radar(sensor, motor, searchRadius, ballSize);
	radar.findPoints();
	LCD.drawString("Press enter to start", 0, 4);
	Button.ENTER.waitForPressAndRelease();
	radar.navigate();

	// motor.start();
	// EV3 ev3 = (EV3) BrickFinder.getLocal();
	boolean fortsett = true;
	while (fortsett) {
	    if (sensor.getBall()) {
		pickUp.pickup();
		// Delay.msDelay(400);
		motor.goHome();
		nxt.setReady();
		motor.resumeSearch();
	    }
	    if (Button.ESCAPE.isDown())
		fortsett = false;
	    if (Button.ENTER.isDown()) {
		motor.setPose0();
		sensor.resetGyro();
		radar.findPoints();
		radar.navigate();
	    }
	    Thread.sleep(50);
	}
	motor.terminate();
	nxt.setDone();
	motor.join();
	btThread.join();

    }
}
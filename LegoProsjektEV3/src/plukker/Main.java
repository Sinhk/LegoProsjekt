package plukker;

import lejos.hardware.Button;
import lejos.utility.Delay;

class Main {
    public static void main(String[] arg) throws Exception {
	// Bestemmer hvor stort søkeområdet skal være
	float searchRadius = 60f;
	float ballSize = 40f;
	// Hvor mange baller som skal finnes
	int ballTarget = 4;
	int ballsFound = 0;

	Sensor sensor = new Sensor();
	Pickup pickUp = new Pickup();
	Mover mover = new Mover(sensor, pickUp);
	BTConnectEV3 nxt = new BTConnectEV3();
	// Starter tråd for bluetooth tilkobling
	Thread btThread = new Thread(nxt);
	btThread.start();
	Radar radar = new Radar(sensor, mover, searchRadius, ballSize);
	boolean fortsett = true;
	while (fortsett) {
	    // Leter etter nye baller, om det ikke er flere baller i listen
	    if (radar.getRemaining() == 0) {
		if (mover.goToCentre(searchRadius)) {
		    radar.findPoints();
		} else {
		    // hvis det ligger en ball mellom roboten og midten av
		    // søkefeltet, hentes den før roboten leter etter flere baller
		    radar.addPoint(mover.getPointAt(sensor.getDistance(), mover.getHeading()));
		}
		// Henter baller, den nærmeste først, og leverer den til sorterer. 
	    } else if (mover.fetchBall(radar.getClosestPoint(true))) {
		mover.goHome();
		// Aktiverer sorterer
		nxt.setReady();
		ballsFound++;
		if (ballsFound >= ballTarget) {
		    fortsett = false;
		    break;
		}
		mover.resetHome();
	    }
	    
	    if (Button.ESCAPE.isDown())
		fortsett = false;
	    Thread.sleep(50);
	}
	sensor.close();
	//Venter med å sende avslutt signalet til NXTen, så den rekker å sortere den siste ballen før den avlutter.
	Delay.msDelay(5000);
	nxt.setDone();
	btThread.join();
    }
}
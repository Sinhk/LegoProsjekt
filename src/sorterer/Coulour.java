package sorterer;

import lejos.nxt.ColorSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.addon.*;
//Button.RIGHT.isDown()

public class Coulour {
    public static void main(String[] arg) throws Exception {

	Button.waitForAnyPress();
	BTConnect btc = new BTConnect();
	btc.start();
	int speed = 250;
	int speed2 = 200;
	int vinkel1 = 320;
	int vinkel2 = 30;
	int vinkel3 = 35;
	int vent = 1500;
	int vent2 = 1000;

	boolean fortsett = true;

	while (fortsett) {

	    if (btc.getReady()) {
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed2);
		LCD.drawString("sorterer", 0, 0);

		Motor.A.rotate(vinkel1);
		LCD.clear();

		// ColorSensor cs = new ColorSensor(SensorPort.S2);
		// Color color = cs.getColor();

		ColorHTSensor colorSensor = new ColorHTSensor(SensorPort.S2);
		int color = colorSensor.getColorID();
		LCD.drawString("color" + color, 1, 1);

		// Button.waitForAnyPress();

		if (color == 7) {
		    Motor.B.rotate(vinkel3);
		    Delay.msDelay(vent);
		    Motor.A.rotate(vinkel2);
		    Delay.msDelay(vent2);
		    Motor.B.rotate(-vinkel3);
		    Motor.A.rotate((-vinkel1) + (-vinkel2));
		    Motor.B.stop();
		    Motor.A.stop();
		}

		if (color == 0) {
		    Motor.B.rotate(-vinkel2);
		    Delay.msDelay(vent);
		    Motor.A.rotate(vinkel2);
		    Delay.msDelay(vent2);
		    Motor.A.rotate(-(vinkel1 + vinkel2));
		    Motor.B.rotate(vinkel3);
		    Motor.B.stop();
		    Motor.A.stop();
		}

		if (color != 0 && color != 7) {
		    Delay.msDelay(vent);
		    Motor.A.rotate(vinkel2);
		    Delay.msDelay(vent2);
		    Motor.A.rotate(-(vinkel1 + vinkel2));
		    Motor.A.stop();
		    Motor.B.stop();
		}
	    }
	    if (btc.getDone()) {
		fortsett = false;
		btc.close();

	    }
	}
    }

}

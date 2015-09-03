import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.PilotProps;

public class HelloWorldAdvanced {
	static RegulatedMotor leftMotor;
	static RegulatedMotor rightMotor;
	
	public static void main(String[] args)  throws Exception{
			EV3 ev3 = (EV3) BrickFinder.getLocal();
			TextLCD lcd = ev3.getTextLCD();
			//Keys keys = ev3.getKeys();
			
			lcd.drawString("Hello World!", 4, 4);
			Thread.sleep(500);
			
			PilotProps pp = new PilotProps();
			pp.loadPersistentValues();
			
			float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.6"));
			float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "11.7"));
			
			
			leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "A"));
			rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "C"));
			boolean reverse = Boolean.parseBoolean(pp.getProperty(PilotProps.KEY_REVERSE,"false"));
			
			DifferentialPilot robot = new DifferentialPilot(5.7f, 11.5f, leftMotor, rightMotor, true);
			
			robot.setTravelSpeed(80); // cm/sec

			int distance = 20;

			
			
			robot.travel(distance);
			robot.rotate(90);
			robot.travel(distance);
			robot.rotate(90);
			robot.travel(distance);
			robot.rotate(90);
			robot.travel(distance);
			robot.rotate(90);
			
			lcd.drawString("Bye World!", 4, 4);
			Thread.sleep(5000);
			
			
			//keys.waitForAnyPress();
	}
}

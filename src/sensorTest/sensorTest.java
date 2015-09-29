package sensorTest;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.LowPassFilter;
import lejos.robotics.filter.MaximumFilter;
import lejos.robotics.filter.MinimumFilter;

public class sensorTest {

	public static void main(String[] args) {
		int samples = 100;
		Brick brick = BrickFinder.getDefault();
		Port s4 = brick.getPort("S4");

		EV3ColorSensor rightsensor = new EV3ColorSensor(s4);
		SampleProvider rightLeser = rightsensor.getRedMode();
		LowPassFilter lowPass = new LowPassFilter(rightLeser, .5f);
		MaximumFilter max = new MaximumFilter(rightLeser, samples);
		MinimumFilter min = new MinimumFilter(rightLeser, samples);
		float[] maxsample = new float[rightLeser.sampleSize()];
		float[] minsample = new float[rightLeser.sampleSize()];

		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();

		lcd.drawString("Sett sensor på svart og trykk en knapp", 0, 3);
		lcd.drawString("Sample size: " + rightLeser.sampleSize(), 0, 6);

		Button.waitForAnyPress();
		for (int i = 0; i < samples; i++) {
			max.fetchSample(maxsample, 0);
			min.fetchSample(minsample, 0);
		}
		float maxBlack = maxsample[0];
		float minBlack = minsample[0];
		lcd.clear();
		lcd.drawString("Sett sensor på hvit og trykk en knapp", 0, 3);
		Button.waitForAnyPress();
		for (int i = 0; i < samples; i++) {
			max.fetchSample(maxsample, 0);
			min.fetchSample(minsample, 0);
		}
		float maxWhite = maxsample[0];
		float minWhite = minsample[0];
		lcd.clear();
		lcd.drawString("Black:", 0, 0);
		lcd.drawString("Min: " + minBlack, 0, 1);
		lcd.drawString("Max: " + maxBlack, 0, 2);
		lcd.drawString("White:", 0, 4);
		lcd.drawString("Min: " + minWhite, 0, 5);
		lcd.drawString("Max: " + maxWhite, 0, 6);

		Button.waitForAnyPress();
		lcd.clear();
		lcd.drawString("Black:", 0, 0);
		lcd.drawString("Diff: " + (maxBlack - minBlack), 0, 1);
		lcd.drawString("Deviatoin : " + (0.5 * (maxBlack - minBlack)), 0, 2);
		lcd.drawString("White:", 0, 4);
		lcd.drawString("Diff: " + (maxWhite - minWhite), 0, 5);
		lcd.drawString("Deviatoin : " + (0.5 * (maxWhite - minWhite)), 0, 6);
		Button.waitForAnyPress();
		rightsensor.close();
	}

}

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
		int samples = 200;
		Brick brick = BrickFinder.getDefault();
		Port s4 = brick.getPort("S4");
		Port s1 = brick.getPort("S1");

		EV3ColorSensor rightsensor = new EV3ColorSensor(s4);
		SampleProvider rightLeser = rightsensor.getRedMode();

		EV3ColorSensor leftsensor = new EV3ColorSensor(s1);
		SampleProvider leftLeser = leftsensor.getRedMode();

		LowPassFilter lowPass = new LowPassFilter(rightLeser, .5f);

		MaximumFilter maxR = new MaximumFilter(rightLeser, samples);
		MinimumFilter minR = new MinimumFilter(rightLeser, samples);
		MaximumFilter maxL = new MaximumFilter(leftLeser, samples);
		MinimumFilter minL = new MinimumFilter(leftLeser, samples);

		float[] maxsampleR = new float[rightLeser.sampleSize()];
		float[] minsampleR = new float[rightLeser.sampleSize()];
		float[] maxsampleL = new float[leftLeser.sampleSize()];
		float[] minsampleL = new float[leftLeser.sampleSize()];

		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();

		lcd.drawString("Sett sensor på svart og trykk en knapp", 0, 3);
		lcd.drawString("Sample size: " + rightLeser.sampleSize(), 0, 6);

		Button.waitForAnyPress();
		float[] BlackR = new float[2];
		float[] BlackL = new float[2];
		getSamples(maxR, minR, maxsampleR, minsampleR, BlackR, samples);
		getSamples(maxL, minL, maxsampleL, minsampleL, BlackL, samples);
		lcd.clear();
		lcd.drawString("Sett sensor på hvit og trykk en knapp", 0, 3);
		Button.waitForAnyPress();

		float[] WhiteR = new float[2];
		float[] WhiteL = new float[2];
		getSamples(maxR, minR, maxsampleR, minsampleR, WhiteR, samples);
		getSamples(maxL, minL, maxsampleL, minsampleL, WhiteL, samples);

		lcd.clear();
		lcd.drawString("Black:" + "MinR: " + BlackR[1], 0, 0);
		lcd.drawString("MinL: " + BlackL[1], 0, 1);
		lcd.drawString("MaxR: " + BlackR[0], 0, 2);
		lcd.drawString("MaxL: " + BlackL[0], 0, 3);
		lcd.drawString("White:" + "MinR: " + WhiteR[1], 0, 4);
		lcd.drawString("MinL: " + WhiteL[1], 0, 5);
		lcd.drawString("MaxR: " + WhiteR[0], 0, 6);
		lcd.drawString("MaxL: " + WhiteL[0], 0, 7);

		Button.waitForAnyPress();
		lcd.clear();
		lcd.drawString("Black:", 0, 0);
		lcd.drawString("DiffR: " + (BlackR[0] - BlackR[1]), 0, 1);
		lcd.drawString("DiffL: " + (BlackL[0] - BlackL[1]), 0, 2);
		lcd.drawString("White:", 0, 4);
		lcd.drawString("DiffR: " + (WhiteR[0] - WhiteR[1]), 0, 5);
		lcd.drawString("DiffL: " + (WhiteL[0] - WhiteL[1]), 0, 6);
		Button.waitForAnyPress();
		rightsensor.close();
		leftsensor.close();
	}

	public static void getSamples(SampleProvider maxProvider, SampleProvider minProvider, float[] maxsample,
			float[] minsample, float[] result, int samples) {
		for (int i = 0; i <= samples; i++) {
			maxProvider.fetchSample(maxsample, 0);
			minProvider.fetchSample(minsample, 0);
		}
		result[0] = maxsample[0];
		result[1] = minsample[0];
	}

}

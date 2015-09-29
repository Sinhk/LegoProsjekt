package rally;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

/**
 * This filter dynamicaly adjust the samples value to a range of 0-1. The
 * purpose of this filter is to autocalibrate a light Sensor to return values
 * between 0 and 1 no matter what the light conditions. Once the light sensor
 * has "seen" both white and black it is calibrated and ready for use.
 * 
 * The filter could be used in a line following robot. The robot could rotate to
 * calibrate the sensor.
 * 
 * @author Aswin
 * 
 */
public class autoAdjustFilter extends AbstractFilter {
	/*
	 * These arrays hold the smallest and biggest values that have been "seen:
	 */
	private float[] minimum;
	private float[] maximum;
	long startTime = 0;

	public autoAdjustFilter(SampleProvider source) {
		super(source);
		/*
		 * Now the source and sampleSize are known. The arrays can be
		 * initialized
		 */
		minimum = new float[sampleSize];
		maximum = new float[sampleSize];
		reset();
	}

	public void reset() {
		/* Set the arrays to their initial value */
		for (int i = 0; i < sampleSize; i++) {
			minimum[i] = Float.POSITIVE_INFINITY;
			maximum[i] = Float.NEGATIVE_INFINITY;
			startTime = System.currentTimeMillis();
		}
	}

	/*
	 * To create a filter one overwrites the fetchSample method. A sample must
	 * first be fetched from the source (a sensor or other filter). Then it is
	 * processed according to the function of the filter
	 */
	public void fetchSample(float[] sample, int offset) {
		super.fetchSample(sample, offset);
		for (int i = 0; i < sampleSize; i++) {
			if (startTime > (System.currentTimeMillis() - 5000)) {
				if (minimum[i] > sample[offset + i])
					minimum[i] = sample[offset + i];
				if (maximum[i] < sample[offset + i])
					maximum[i] = sample[offset + i];
			}
			sample[offset + i] = (sample[offset + i] - minimum[i]) / (maximum[i] - minimum[i]);
		}
	}

}
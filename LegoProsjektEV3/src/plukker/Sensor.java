package plukker;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

class Sensor {
    // SampleProviders
    private SampleProvider distance;
    private SampleProvider rightSignal;
    private SampleProvider leftSignal;
    private SampleProvider gyro;
    // Sensors
    private EV3UltrasonicSensor dist;
    private EV3ColorSensor rightsensor;
    private EV3ColorSensor leftsensor;
    private EV3GyroSensor gyroSensor;
    // Constants
    private final float BLACK = 0.18f;

    /**
     * Klasse for alle sensorene, med metoder for å hente verdier fra hver av dem.
     */
    public Sensor() {
	// Setter ultralydsensor til distance mode 
	dist = new EV3UltrasonicSensor(SensorPort.S2);
	distance = dist.getDistanceMode();
	// Setter fargesensorene til red mode 
	rightsensor = new EV3ColorSensor(SensorPort.S4);
	rightSignal = rightsensor.getRedMode();
	leftsensor = new EV3ColorSensor(SensorPort.S3);
	leftSignal = leftsensor.getRedMode();
	// Setter gyrosensoren til angle mode og nullstiller vinkel 
	gyroSensor = new EV3GyroSensor(SensorPort.S1);
	gyro = gyroSensor.getAngleMode();
	gyroSensor.reset();
    }
    /**
     * Sjekker om det er noe forran roboten, 10cm eller mindre
     * Brukes til å finne ut om ballen kan plukkes opp.
     * @return true hvis det er no 10 cm eller nærmere
     */
    public boolean getBall() {
	if (getDistance() <= 10.0 && getDistance() != 0.0) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Leser verdi fra fargesensor på høyresiden og finner ut om den ser svart. 
     * @return true hvis det er svart
     */
    public boolean getRight() {
	float[] sample = new float[rightSignal.sampleSize()];
	rightSignal.fetchSample(sample, 0);
	if (sample[0] < BLACK) {
	    return true;
	} else
	    return false;
    }

    /**
     * Leser verdi fra fargesensor på venstresiden og finner ut om den ser svart. 
     * @return true hvis det er svart
     */
    public boolean getLeft() {
	float[] sample = new float[leftSignal.sampleSize()];
	leftSignal.fetchSample(sample, 0);
	if (sample[0] < BLACK) {
	    return true;
	} else
	    return false;
    }

    /**
     * Lukker alle sensorer
     */
    public void close() {
	dist.close();
	rightsensor.close();
	leftsensor.close();
	gyroSensor.close();
    }
    
    /**
     * Leser distanse fra ultralydsensor.
     * @return verdi i cm
     */
    public float getDistance() {
	float[] sample = new float[distance.sampleSize()];
	distance.fetchSample(sample, 0);
	return ((sample[0] == 0.0) ? Float.POSITIVE_INFINITY : sample[0])*100.0f;
    }

    /**
     * Leser vinkel fra gyrosensor.
     * @return vinkel i grader
     */
    public float getGyro() {
	float[] sample = new float[gyro.sampleSize()];
	gyro.fetchSample(sample, 0);
	return sample[0];
    }

    /**
     * Nullstiller gyrosensor til 0 grader.
     */
    public void resetGyro() {
	gyroSensor.reset();
	Delay.msDelay(2000);
    }
}
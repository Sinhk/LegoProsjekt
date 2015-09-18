/**
*
*
*
*/

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.robotics.SampleProvider;


class Sensor{
    Private EV3ColorSensor fargesensor1;
    Private NXTColorSensor fargesensor2;
    Public Sensor(){
        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); //
        Port s2 = brick.getPort("S2"); //
        Port s3 = brick.getPort("S3"); //
        Port s4 = brick.getPort("S4"); //
        fargesensor1 = new EV3ColorSensor(s1);
        fargesensor1.setCurrentMode(ColorIDMode);
        fargesensor2 = new NXTColorSensor(s2);
        fargesensor2.setCurrentMode(ColorIDMode);
    }
}
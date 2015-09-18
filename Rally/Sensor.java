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
    private EV3ColorSensor fargesensor1;
    private NXTColorSensor fargesensor2;
   
    public Sensor(){
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
    
    public boolean isBlackR() {
        if (fargesensor1.getColorID()==7) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isBlackL() {
        if (fargesensor2.getColorID()==7) {
            return true;
        } else {
            return false;
        }
    }
    
    
}
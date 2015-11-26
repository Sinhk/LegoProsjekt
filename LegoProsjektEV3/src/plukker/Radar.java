package plukker;

import java.util.ArrayList;
import java.util.List;

import lejos.robotics.geometry.Point;
import lejos.utility.Delay;

public class Radar {
    private Sensor sensor;
    private Mover mover;
    private List<Point> pointList = new ArrayList<Point>();
    private float radius;
    private final float ERR = 10f;
    private final float objectSize;

    /**
     * Klasse for å lete etter baller
     * @param sensor Sensor objekt
     * @param mover Mover objekt
     * @param searchRadius radius på søkområde
     * @param objectSize størrelse på ball
     */
    public Radar(Sensor sensor, Mover mover, float searchRadius, float objectSize) {
	this.sensor = sensor;
	this.mover = mover;
	radius = searchRadius;
	this.objectSize = objectSize;
    }

    /**
     * Finner baller og lagrer dem i liste
     */
    public void findPoints() {
	pointList.clear();
	List<float[]> points = new ArrayList<float[]>();
	mover.gyroRotateTo(0.0);
	//Roterer 360 grader og lagrer alle punkt som er inne i søkeområdet
	mover.slowSpin(360);
	while (mover.isMoving()) {
	    float distance = sensor.getDistance();
	    if (distance <= radius && distance > 0.0f) {
		float angle = sensor.getGyro();// mover.getHeading();
		angle = (angle>360.0f)?360.0f:angle;
		angle = (angle<0)?0:angle;
		points.add(new float[] { distance , angle });
	    }
	    Delay.msDelay(100);
	}
	mover.gyroRotateTo(360.0);
	float lastDistance = 0;
	float lastAngle = 0;
	float totalDistance = 0;
	float startAngle = 0;
	int matchCount = 0;
	int i = 0;
	boolean firstRound = true;
	boolean firstRemoved = false;
	//Slår sammen punkt som er nær hverandre og lagrer dem som baller.
	while(i < points.size()){
	    float distance = points.get(i)[0];
	    float angle = points.get(i)[1];
	    
	    if (Math.abs(distance - lastDistance) < ERR && (180.0f-Math.abs(Math.abs(angle - lastAngle)-180.0f)) < ERR) {
		totalDistance += distance;
		matchCount++;
		if(!firstRound&&pointList.size()!=0&&!firstRemoved){
		    firstRemoved = true;
		    pointList.remove(0);
		}
		
	    } else {
		if (matchCount > 1) {
		    float meanDistance = totalDistance / matchCount;
		    float coverAngle = 180.0f-Math.abs(Math.abs(lastAngle - startAngle)-180.0f);
		    float medianAngle = startAngle+(coverAngle/2.0f);
		    double expectedAngle = Math.toDegrees(2.0 * Math.atan((objectSize / 2.0) / meanDistance));
		    // Hvis punktene dekker mer en 10 grader og mindre enn forventet vinkel (utregnet fra ballstørrelse) legres de som en ball. 
		    if (coverAngle < expectedAngle&&coverAngle>=10.0f) {
			pointList.add(mover.getPointAt(meanDistance, medianAngle));
		    }
		    if(!firstRound)break;
		}
		totalDistance = distance;
		startAngle = angle;
		matchCount = 1;
	    }

	    lastDistance = distance;
	    lastAngle = angle;
	    i++;
	    
	    if (i == points.size()&&matchCount>1){
		i=0;
		firstRound = false;
	    }
	}
	mover.setSpeeds();
    }

    /**
     * Finner nærmeste ball
     * @param delete true hvis ballen skla slettes fra listen
     * @return point nærmeste ball
     */
    public Point getClosestPoint(boolean delete) {
	if (getRemaining()==0)return null;
	float minLength = Float.POSITIVE_INFINITY;
	int closest = -1;
	for (int i = 0; i < pointList.size(); i++) {
	    if (pointList.get(i).length() < minLength){
		minLength = pointList.get(i).length();
		closest = i;
	    }		
	}
	Point point = pointList.get(closest);
	if (delete) {
	    pointList.remove(closest);
	}
	return point;
    }

    /**
     * Sjekker hvor mange baller som finnes i listen
     * @return antall baller i listen
     */
    public int getRemaining() {
	return pointList.size();
    }
    
    /**
     * Legger til ball i listen 
     * @param point ballens plassering
     */
    public void addPoint(Point point){
	pointList.add(point);
    }
}

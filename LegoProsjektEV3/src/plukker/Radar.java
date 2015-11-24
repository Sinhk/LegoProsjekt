package plukker;

import java.util.ArrayList;
import java.util.List;

import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;

public class Radar {
    private Sensor sensor;
    private Mover mover;
    private List<Point> pointList = new ArrayList<Point>();
    private float radius;
    private float diameter;
    private final float ERR = 5f;
    private final float objectSize;

    /**
     * 
     * @param sensor
     *            Sensor object to use
     * @param mover
     *            Mover object to use
     * @param searchRadius
     *            radius to search inn
     * @param objectSize
     *            approximate size of object to search for. Side viewed by
     *            sensor
     */
    public Radar(Sensor sensor, Mover mover, float searchRadius, float objectSize) {
	this.sensor = sensor;
	this.mover = mover;
	radius = searchRadius;
	diameter = radius * 2;
	this.objectSize = objectSize;
    }

    public void findPoints() {
	pointList.clear();
	List<float[]> points = new ArrayList<float[]>();
	mover.slowSpin(360);
	while (mover.isMoving()) {
	    float distance = sensor.getDistance();
	    if (distance <= radius && distance > 0) {
		float angle = sensor.getGyro();// mover.getHeading();
		points.add(new float[] { distance * 100.0f, angle });
		System.out.println(distance + ", " + angle);
	    }
	}
	mover.gyroRotateTo(360.0);
	float lastDistance = 0;
	float lastAngle = 0;
	float totalAngle = 0;
	float totalDistance = 0;
	float startAngle = 0;
	int matchCount = 0;
	for (float[] p : points) {
	    float distance = p[0];
	    float angle = p[1];
	    // System.out.println(distance+", "+ angle + ", "+Math.abs(distance
	    // - lastDistance) +", " +Math.abs(angle - lastAngle) );
	    if (Math.abs(distance - lastDistance) < ERR && Math.abs(angle - lastAngle) < ERR) {
		totalAngle += angle;
		totalDistance += distance;
		matchCount++;
		// System.out.println(matchCount);
	    } else {
		if (matchCount > 1) {
		    float meanDistance = totalDistance / matchCount;
		    float meanAngle = totalAngle / matchCount;
		    float coverAngle = angle - startAngle;
		    System.out.println(meanDistance + ", " + meanAngle);
		    if (coverAngle < 2 * Math.atan((objectSize / 2) / meanDistance)) {
			pointList.add(mover.getPointAt(meanDistance, meanAngle));
		    }
		    ;

		}
		totalAngle = angle;
		totalDistance = distance;
		startAngle = angle;
		matchCount = 1;
	    }

	    lastDistance = p[0];
	    lastAngle = p[1];
	}
	if (matchCount > 1) {
	    float meanDistance = totalDistance / matchCount;
	    float meanAngle = totalAngle / matchCount;
	    System.out.println(meanDistance + ", " + meanAngle);
	    pointList.add(mover.getPointAt(meanDistance, meanAngle));
	}
	mover.setSpeeds();
	System.out.println("Found " + pointList.size() + " potential balls:");
	for (Point p : pointList) {
	    System.out.println("X: " + p.getX() + " Y: " + p.getY());
	}
    }
/**
 * 
 * @param delete if point should be removed from list
 * @return point closest to origo
 */
    public Point getClosestPoint(boolean delete) {
	float minLength = Float.POSITIVE_INFINITY;
	int closest = -1;
	for (int i = 0; i < pointList.size(); i++) {
	    if (pointList.get(i).length() < minLength)
		closest = i;
	}
	Point point = pointList.get(closest);
	if (delete){
	    pointList.remove(closest);
	}
	return point;
    }

    public void navigate() {
	Navigator navigator = new Navigator(mover.getPilot());
	for (Point p : pointList) {
	    navigator.addWaypoint(new Waypoint(p));
	}
	navigator.followPath();
	navigator.singleStep(false);
	// navigator.waitForStop();
    }

    public int getRemaining() {
	return pointList.size();
    }
}

package plukker;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.geometry.Point;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Pose;
import lejos.utility.Delay;

class Mover extends Thread {
    private Chassis chassis;
    private MovePilot pilot;
    private Sensor sensor;
    private Pickup pickUp;
    private EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
    private EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
    private OdometryPoseProvider pp;
    private Pose startPose;
    private double wheelSize = 5.56;
    private double wheelOffset = 5.28;
    
    /**
     * Klasse for navigasjon
     * @param sensor Sensor objekt for 
     * @param pickUp Pickup objekt
     */
    public Mover(Sensor sensor, Pickup pickUp) {
	Wheel leftWheel = WheeledChassis.modelWheel(leftMotor, wheelSize).offset(wheelOffset);
	Wheel rightWheel = WheeledChassis.modelWheel(rightMotor, wheelSize).offset(-wheelOffset);
	chassis = new WheeledChassis(new Wheel[] { leftWheel, rightWheel }, WheeledChassis.TYPE_DIFFERENTIAL);
	pilot = new MovePilot(chassis);
	setSpeeds();
	pp = new OdometryPoseProvider(pilot);
	startPose = pp.getPose();

	this.sensor = sensor;
	this.pickUp = pickUp;
    }
    
    /**
     * Setter hastigheter 
     */
    public void setSpeeds() {
	pilot.setLinearSpeed(10);
	pilot.setAngularSpeed(30);
    }

    /**
     * Retter opp roboten til linje forarn 
     */
    public void align() {
	if (sensor.getRight() && sensor.getLeft()) {

	} else {
	    if (sensor.getRight()) {
		pilot.arcForward(-wheelOffset);
		while (!sensor.getLeft()) {
		}
		pilot.stop();
	    } else if (sensor.getLeft()) {
		pilot.arcForward(wheelOffset);
		while (!sensor.getRight()) {
		}
		pilot.stop();
	    }
	}
    }
    
    /**
     * Retter opp roboten til linje bak
     */
    public void alignReverse() {
	pilot.backward();
	while (!sensor.getRight() && !sensor.getLeft()) {
	}
	pilot.stop();
	if (sensor.getRight() && sensor.getLeft()) {
	    
	} else if (sensor.getRight()) {
	    pilot.arcBackward(wheelOffset);
	    while (!sensor.getLeft()) {
	    }
	    pilot.stop();
	} else if (sensor.getLeft()) {
	    pilot.arcBackward(-wheelOffset);
	    while (!sensor.getRight()) {
	    }
	    pilot.stop();
	}
    }

    /**
     * Kjører hjem og leverer ball
     */
    public void goHome() {
	pilot.stop();
	// Setter hjem 20 cm til høyre for startposisjon
	Point homePoint = new Point(0, -20);
	//Roterer mot hjem og kjører fremover til linje er funnet.
	pilot.rotate((pp.getPose().relativeBearing(homePoint)));
	pilot.forward();
	while (!sensor.getRight() && !sensor.getLeft()) {
	}
	pilot.stop();
	
	//Roterer mot sorterer og kjører til den er 10 cm unna.
	align();
	pilot.rotate(-90);
	pilot.forward();
	while (!sensor.getBall()) {
	    // Følger linje
	    if (!sensor.getLeft()) {
		chassis.setVelocity(10, 15);
		while (!sensor.getLeft()){
		    if (sensor.getBall())break;
		}
		chassis.setVelocity(10, -5);
	    }
	}
	pilot.stop();
	// Slipper ballen og rygger unna 
	pickUp.drop();
	pilot.travel(-20);
    }

    /**
     * Nullstiller PoseProvider og gyro
     */
    public void resetHome() {
	pilot.rotate(-90);
	alignReverse();
	pp.setPose(startPose);
	sensor.resetGyro();

    }


    /**
     * Prøver å hente ball
     * @param point Ballens plassering
     * @return true hvis ballen ble plukket opp, false hvis det mislyktes
     */
    public boolean fetchBall(Point point) {
	//Roter til ballen og kjører til den er 30cm unna
	pilot.rotate(pp.getPose().relativeBearing(point));
	pilot.forward();
	while (pp.getPose().distanceTo(point) > 30f);
	// Ser etter ballen og forsetter fremover om den finnes
	float estDist = pp.getPose().distanceTo(point);
	if (correctAim(estDist)) {
	    pilot.forward();
	    float lastDistance = estDist;
	    while (true) {
		// Plukker opp ballen når den er nær nok
		if (sensor.getBall()) {
		    pilot.stop();
		    if (pickUp.pickup()) {
			return true;
		    } else
			return false;
		}
		
		//Sjekker om den fortsatt ser ballen 
		float distance = sensor.getDistance();
		if (distance > lastDistance) {
		    if (!correctAim(lastDistance)) {
			return false;
		    }
		    distance = sensor.getDistance();
		    pilot.forward();
		}
		lastDistance = distance;
		Delay.msDelay(200);
	    }
	}
	return false;
    }

    /**
     * Sikter mot ball som skal være en gitt avstand unna
     * @param distance forventet avstand til ball
     * @return true hvis den finner igjen ballen
     */

    public boolean correctAim(float distance) {
	float ERR = 20f;
	float angleBase = 15f;
	int run = 1;
	//Roterer til den finner noe, eller har rotert mer enn 90 grader.
	while (Math.abs(distance - sensor.getDistance()) > ERR) {
	    float angle = (float) (angleBase * run * ((Math.pow(-1, run))));
	    pilot.rotate(angle, true);
	    while (pilot.isMoving() && (Math.abs(distance - sensor.getDistance()) > ERR)&&(sensor.getDistance()>distance)) {
		//if(sensor.getDistance()<100f)System.out.println("Finding: " + sensor.getDistance());
		//Thread.yield();
	    }
	    pilot.stop();
	    if (angle >= 90)
		return false;
	    run++;
	}
	return true;
    }

    /**
     * Roterer sakte
     * @param angle vinkel å rotere
     */
    public void slowSpin(double angle) {
	pilot.setAngularSpeed(20);
	pilot.rotate(angle, true);
    }

    /**
     * Sjekker om roboten er i bevegelse.
     * @return true hvis roboten er i bevegelse
     */
    public boolean isMoving() {
	return pilot.isMoving();
    }

    /**
     * Finner punkt relativ til roboten.
     * @param distance avstand til punkt
     * @param bearing rettning i grader relativ til X-aksen
     * @return point
     */
    public Point getPointAt(float distance, float bearing) {
	return pp.getPose().pointAt(distance, bearing);
    }

    /**
     * Finner kursen til roboten relativ til startposisjon
     * @return kurs i grader
     */
    public float getHeading() {
	return pp.getPose().getHeading();
    }

    /**
     * Roterer til vinkel fra gyro
     * @param angle vinkel å rotere til
     */
    public void gyroRotateTo(double angle) {
	pilot.rotate(angle - sensor.getGyro());
    }

    /**
     * Kjører mot punkt 
     * @param point punkt 
     */
    public void goToPoint(Point point) {
	pilot.rotate(pp.getPose().relativeBearing(point));
	pilot.travel(pp.getPose().distanceTo(point),true);
    }

    /**
     * Kjører til midten av søkområdet
     * @param searchRadius radius på søkeområdet
     * @return false hvis det er en ball mellom roboten og midten
     */
    public boolean goToCentre(float searchRadius) {
	Point point = startPose.pointAt(searchRadius, -45);
	goToPoint(point);
	if (sensor.getDistance()<pp.getPose().distanceTo(point)){
	    pilot.stop();
	    return false;
	}
	while(pilot.isMoving()){
	    
	}
	gyroRotateTo(0);
	return true;
    }
}
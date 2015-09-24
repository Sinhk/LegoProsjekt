package Rally;

class Rally {
	public static void main(String[] arg) throws Exception {
		// int[] turnArray = {0,2,3,5,6,8,9,11,12};
		Mover motor = new Mover();
		Sensor sensor = new Sensor();
		sensor.start();
		Skjerm skjerm = new Skjerm(sensor);
		skjerm.start();
		// sensor.calibrate(motor);
		int i = 0;
		boolean fortsett = true;
		motor.forward(1);
		while (fortsett) {
			// System.out.println("Working...");
			// if (Arrays.asList(turnArray).contains(i)){
			if (i == 0 || i == 2 || i == 3 || i == 5 || i == 6 || i == 8 || i == 9 || i == 11 || i == 12) {
				if (i == 0 || i == 3 || i == 6 || i == 9 || i == 12) {
					motor.forward(1);
					if (sensor.isBlackL()) {
						motor.turnRight();
						Thread.sleep(300);
						motor.forward(1);
					}
					if (sensor.isBlackR()) {
						i++;
						motor.forward(0);
						Thread.sleep(1000);
					}
				} else if (i == 2 || i == 5 || i == 8 || i == 11) {
					motor.forward(2);
					if (sensor.isBlackR()) {
						motor.turnLeft();
						Thread.sleep(300);
						motor.forward(2);
					}
					if (sensor.isBlackL()) {
						i++;
						motor.forward(0);
						Thread.sleep(1000);
					}

				}
			} else {
				motor.forward(2);
				if (sensor.isBlackR()) {
					motor.turnLeft();
				}
				if (sensor.isBlackL()) {
					i++;
				}
			}
			skjerm.setI(i);
		}
	}
}

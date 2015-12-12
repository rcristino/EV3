package component;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3IRSensor;
import events.EventManager;
import events.EventMove;
import exec.RickRobot;

public class InfraredSensor extends Thread {

	public static enum Range {
		UNKNOWN, SHORT, MEDIUM, LONG
	}

	public final static int SHORT_RANGE = 10;
	public final static int MEDIUM_RANGE = 25;
	public final static int LONG_RANGE = 50;

	private static int NUM_SAMPLES = 10;
	private static EV3IRSensor irs = null;
	private static Range currentRange = Range.UNKNOWN;

	public InfraredSensor() {
		super("InfraredSensor");
	}

	public static Range getRage() {
		return currentRange;
	}

	@Override
	public void run() {
		boolean isRunning = true;
		if (irs == null) {
			irs = new EV3IRSensor(RickRobot.INFRARED_SENSOR_PORT);

		}
		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processIRS();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public void processIRS() {
		float[] sample = new float[NUM_SAMPLES];
		int offset = 0;

		irs.getDistanceMode().fetchSample(sample, offset);

		for (int i = 0; i < sample.length; i++) {
			float f = sample[i];

			if (f != 0) {

				if (f <= SHORT_RANGE) {
					if (currentRange != Range.SHORT) {
						currentRange = Range.SHORT;
						EventManager.addEvent(new EventMove(SHORT_RANGE * 10));
					}
				} else if (f <= MEDIUM_RANGE) {
					if (currentRange != Range.MEDIUM) {
						currentRange = Range.MEDIUM;
						EventManager.addEvent(new EventMove(MEDIUM_RANGE * 10));
					}
				} else if (f <= LONG_RANGE) {
					if (currentRange != Range.LONG) {
						currentRange = Range.LONG;
						EventManager.addEvent(new EventMove(LONG_RANGE * 10));
					}
				} else if (f <= Float.POSITIVE_INFINITY) {
					currentRange = Range.UNKNOWN;
				}
			}
		}
	}
}

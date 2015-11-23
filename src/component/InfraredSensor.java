package component;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3IRSensor;
import exec.RickRobot;

public class InfraredSensor extends Thread {

	private static int NUM_SAMPLES = 10;
	private static EV3IRSensor irs = null;
	private static int SHORT_RANGE = 10;
	private static int MEDIUM_RANGE = 25;
	private static int LONG_RANGE = 50;

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
		for (float f : sample) {
			if (f != 0) {

				if (f <= SHORT_RANGE) {
					LocalEV3.get().getAudio().systemSound(Sounds.BUZZ);
					break;
				} else if (f <= MEDIUM_RANGE) {
					LocalEV3.get().getAudio().systemSound(Sounds.DOUBLE_BEEP);
					break;
				} else if (f <= LONG_RANGE) {
					LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
					break;
				}
			}
		}
	}

}

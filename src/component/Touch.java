package component;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3TouchSensor;
import exec.RickRobot;

public class Touch extends Thread {

	private static int NUM_SAMPLES = 10;
	private static EV3TouchSensor touch = null;

	@Override
	public void run() {
		boolean isRunning = true;
		if (touch == null) {
			touch = new EV3TouchSensor(RickRobot.TOUCH_SENSOR_PORT);

		}
		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processTouch();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	}

	public void processTouch() {
		float[] sample = new float[NUM_SAMPLES];
		int offset = 0;

		touch.fetchSample(sample, offset);
		for (float f : sample) {
			if (f != 0) {
				LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
				break;
			}
		}
	}

}

package component;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;
import exec.RickRobot;

public class ColourDetector extends Thread {

	private static int NUM_SAMPLES = 10;
	private static EV3ColorSensor colourDetector = null;

	@Override
	public void run() {
		boolean isRunning = true;
		if (colourDetector == null) {
			colourDetector = new EV3ColorSensor(RickRobot.COLOUR_SENSOR_PORT);
		}
		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processColour();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	}

	/**
	 * Colour ID
	 * 0 = NONE, 
	 * 1 = BLACK, 
	 * 2 = BLUE, 
	 * 3 = GREEN, 
	 * 4 = YELLOW, 
	 * 5 = RED, 
	 * 6 = WHITE, 
	 * 7 = BROWN
	 */
	public void processColour() {
		float[] sample = new float[NUM_SAMPLES];
		int offset = 0;
		colourDetector.getColorIDMode().fetchSample(sample, offset);
		for (float f : sample) {
			if (f == 6) {
				LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
				break;
			}
		}
		
	}
}

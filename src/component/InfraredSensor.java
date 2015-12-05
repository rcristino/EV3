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
	private static int UNKNOWN_RANGE = 100;
    private static int currentRange;
    
	public InfraredSensor() {
		super("InfraredSensor");
	}
	
	public static int getRage(){
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
		currentRange = UNKNOWN_RANGE;
		irs.getDistanceMode().fetchSample(sample, offset);
		for (float f : sample) {
			if (f != 0) {

				if (f <= SHORT_RANGE) {
					LocalEV3.get().getAudio().systemSound(Sounds.BUZZ);
					currentRange = SHORT_RANGE;
					break;
				} else if (f <= MEDIUM_RANGE) {
					LocalEV3.get().getAudio().systemSound(Sounds.DOUBLE_BEEP);
					currentRange = MEDIUM_RANGE;
					break;
				} else if (f <= LONG_RANGE) {
					LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
					currentRange = LONG_RANGE;
					break;
				}
			}
		}
	}

}

package components;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;


public class TouchSensor extends EV3TouchSensor{
	
	public static int NO_CLICK = 0;
	public static int SINGLE_CLICK = 1;
	private static int NUM_SAMPLES = 10;
	
	public TouchSensor(Port port) {
		super(port);
		super.init();
	}

	public int isTouched(){
		int result = NO_CLICK;
		float[] sample = new float[NUM_SAMPLES];
		int offset = 0;
		
		super.fetchSample(sample, offset);
		for (float f : sample) {
			if (f != 0) {
				result = SINGLE_CLICK;
				break;
			}
		}
			
		return result;
	}

}

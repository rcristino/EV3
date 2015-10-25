package components;

import java.io.IOException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;
import behaviours.RobotStatus;

public class Publish {

	// 1 sample per second
	private final static float FREQUENCY_HIGH = 2;
	private final static float FREQUENCY_LOW = 20;
	private boolean initIrSensor = false;
	private boolean initColourSensor = false;
	private boolean initPosition = false;
	private boolean initPower = false;
	private boolean initGrabber = false;
	private boolean initStatus = false;
	private float[] sampleColour;
	private float[] sampleIr;
	private float[] samplePos;
	private float[] samplePow;	
	private float[] sampleGrab;
	private float[] sampleStatus;
	private static SampleProvider spIr;
	private static SampleProvider spColour;
	private SampleProvider spPositition;
	private SampleProvider spPower;
	private PublishFilter spGrab;
	private PublishFilter spStatus;
	private EV3IRSensor irSensor;
	private EV3ColorSensor colourSensor;
	
	public Publish(EV3IRSensor _irSensor,EV3ColorSensor _colourSensor) {
		irSensor = _irSensor;
		colourSensor = _colourSensor;
	}
	
	public void start(){
		initIrSensor();
		initColourSensor();
		initPosition();
		initPower();
		initGrabber();
		initStatus();
	}

	private void initIrSensor() {
		try {
			if (!initIrSensor) {
				spIr = new PublishFilter(irSensor.getDistanceMode(),
						"IR range readings", FREQUENCY_HIGH);
				sampleIr = new float[spIr.sampleSize()];
				initIrSensor = true;
			}
		} catch (IOException r) {

		}
	}

	private void initColourSensor() {
		try {
			if (!initColourSensor) {
//				getColorIDMode: ID of a surface. The sensor can identify 8 unique colors 
//				(NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN)
				spColour = new PublishFilter(colourSensor.getColorIDMode(),
						"Colour sensor readings", FREQUENCY_HIGH);
				sampleColour = new float[spColour.sampleSize()];
				initColourSensor = true;
			}
		} catch (IOException r) {

		}
	}

	private void initPosition(){
		try {
			if (!initPosition) {
				PositionMode posMode = new PositionMode();
				spPositition =  new PublishFilter(posMode,posMode.getName(),FREQUENCY_HIGH);
				samplePos = new float[spPositition.sampleSize()];	
				initPosition  = true;
			}
		} catch (IOException e) {

		}
	}

	private void initPower(){
		try {
			if (!initPower) {
				PowerMode powMode = new PowerMode();
				spPower = new PublishFilter(powMode,powMode.getName(),FREQUENCY_LOW);
				samplePow = new float[spPower.sampleSize()];	
				initPower = true;
			}
		} catch (IOException e) {

		}
	}
	
	private void initGrabber(){
		try {
			if (!initGrabber) {
				GrabberMode grabMode = new GrabberMode();
				spGrab = new PublishFilter(grabMode,grabMode.getName(),FREQUENCY_LOW);
				sampleGrab = new float[spGrab.sampleSize()];	
				initGrabber = true;
			}
		} catch (IOException e) {

		}
	}
	
	private void initStatus(){
		try {
			if (!initStatus) {
				StatusMode stMode = new StatusMode();
				spStatus = new PublishFilter(stMode,stMode.getName(),FREQUENCY_LOW);
				sampleStatus = new float[spStatus.sampleSize()];	
				initStatus = true;
			}
		} catch (IOException e) {

		}
	}
	
	public void fetchSample() {
		if (initIrSensor) {
			spIr.fetchSample(sampleIr, 0);
		}
		if (initColourSensor) {
			spColour.fetchSample(sampleColour, 0);
		}
		if (initPosition) {
			spPositition.fetchSample(samplePos, 0);
		}
		if (initGrabber) {
			spGrab.fetchSample(sampleGrab, 0);
		}
		if (initPower) {
			spPower.fetchSample(samplePow, 0);
		}
		if (initStatus) {
			spStatus.fetchSample(sampleStatus, 0);
		}
	}

	private class PositionMode implements SensorMode {

		@Override
		public int sampleSize() {
			return 2;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			sample[0] = (float) RobotStatus.getPosX();
			sample[1] = (float) RobotStatus.getPosY();;
		}

		@Override
		public String getName() {
			return "Position <X,Y>";
		}
	}
	
	private class PowerMode implements SensorMode {

		@Override
		public int sampleSize() {
			return 2;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			sample[0] = (float) LocalEV3.get().getPower().getBatteryCurrent();
			sample[1] = (float) LocalEV3.get().getPower().getVoltage();
		}

		@Override
		public String getName() {
			return "Power <Battery,Voltage>";
		}
	}
	
	private class GrabberMode implements SensorMode {

		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			sample[0] = (float) RobotStatus.getCurrentGrabberStatus();
		}

		@Override
		public String getName() {
			return "Grabber <OPENED=0, CLOSED=1>";
		}
	}
	
	private class StatusMode implements SensorMode {

		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			sample[0] = (float) RobotStatus.getCurrentRobotStatus();
		}

		@Override
		public String getName() {
			return "Status <STOP = 0, RUNNING = 1, EXIT = 2>";
		}
	}
}

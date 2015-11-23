package exec;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

import component.Buttons;
import component.ColourDetector;
import component.InfraredSensor;
import component.Touch;

import events.EventManager;

public class RickRobot {

	public final static Port TOUCH_SENSOR_PORT = SensorPort.S3;
	public final static Port INFRARED_SENSOR_PORT = SensorPort.S2;
	public final static Port COLOUR_SENSOR_PORT = SensorPort.S1;
	public final static Port MOTOR_LEFT_PORT = MotorPort.A;
	public final static Port MOTOR_RIGHT_PORT = MotorPort.B;
	public final static Port MOTOR_GRABBER_PORT = MotorPort.C;

	private static EventManager eventManager;
	private static Buttons buttonsManager;
	private static Touch touchManager;
	private static ColourDetector colourManager;
	private static InfraredSensor infraredManager;
	
	private static void startEventManager() {
		eventManager = new EventManager();
		eventManager.start();
	}

	private static void startButtons() {
		buttonsManager = new Buttons();
		buttonsManager.start();
	}

	private static void startTouch() {
		touchManager = new Touch();
		touchManager.start();
	}

	private static void startColourDetector() {
		colourManager = new ColourDetector();
		colourManager.start();
	}

	private static void startInfraredDetector() {
		infraredManager = new InfraredSensor();
		infraredManager.start();
	}
	
	public static void processMission() {
		synchronized (eventManager) {
			eventManager.notify();
		}
		synchronized (buttonsManager) {
			buttonsManager.notify();
		}
		synchronized (touchManager) {
			touchManager.notify();

		}
		synchronized (colourManager) {
			colourManager.notify();
		}
		synchronized (infraredManager) {
			infraredManager.notify();
		}
	}

	public static void main(String[] args) {
		boolean isRunning = true;
		int period = 1000;

		startEventManager();
		startButtons();
		startTouch();
		startColourDetector();
		startInfraredDetector();
		
		while (isRunning) {

			processMission();

			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				isRunning = false;
			}
		}
	}

}

package exec;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import behaviour.MoveManager;

import component.Buttons;
import component.ColourDetector;
import component.Display;
import component.InfraredSensor;
import component.Touch;

import events.EventManager;
import events.PublisherManager;

public class RickRobot {

	public final static Port TOUCH_SENSOR_PORT = SensorPort.S3;
	public final static Port INFRARED_SENSOR_PORT = SensorPort.S2;
	public final static Port COLOUR_SENSOR_PORT = SensorPort.S1;
	public final static Port MOTOR_LEFT_PORT = MotorPort.A;
	public final static Port MOTOR_RIGHT_PORT = MotorPort.B;
	public final static Port MOTOR_GRABBER_PORT = MotorPort.C;
	public final static double WHEEL_RADIUS = 16; /* mm */
	public final static int TRAVEL_SPEED = 40;
	public final static int ROTATE_SPEED = 40;
	public final static int TRACK_WITH = 120;
	public final static int PLAY_GROUND_SIZE = 200;
	public final static int NUMBER_PATH_POINTS = 5;
	public final static int CLOCK = 10; /* msecs */

	private static EventManager eventManager;
	private static Buttons buttonsManager;
	private static Touch touchManager;
	private static ColourDetector colourManager;
	private static InfraredSensor infraredManager;
	private static MoveManager moveManager;
	private static Display display;
	private static PublisherManager pubManager;

	private static void startThreads() {

		display = new Display();
		display.start();

		eventManager = new EventManager();
		eventManager.start();

		buttonsManager = new Buttons();
		buttonsManager.start();

		touchManager = new Touch();
		touchManager.start();

		colourManager = new ColourDetector();
		colourManager.start();

		infraredManager = new InfraredSensor();
		infraredManager.start();

		moveManager = new MoveManager();
		moveManager.start();

		pubManager = new PublisherManager();
		pubManager.start();
	}

	public static void processMission() {
		synchronized (display) {
			display.notify();
		}
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
		synchronized (moveManager) {
			moveManager.notify();
		}
		synchronized (pubManager) {
			pubManager.notify();
		}
	}

	public static void main(String[] args) {
		boolean isRunning = true;

		startThreads();

		while (isRunning) {

			processMission();

			try {
				Thread.sleep(CLOCK);
			} catch (InterruptedException e) {
				isRunning = false;
			}
		}
	}

}

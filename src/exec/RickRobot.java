package exec;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

import component.Buttons;

import events.EventManager;

public class RickRobot {

	public final static Port TOUCH_SENSOR_PORT = SensorPort.S3;
	public final static Port INFRARED_SENSOR_PORT  = SensorPort.S2;
	public final static Port COLOUR_SENSOR_PORT  = SensorPort.S1;
	public final static Port MOTOR_LEFT_PORT  = MotorPort.A;
	public final static Port MOTOR_RIGHT_PORT  = MotorPort.B;
	public final static Port MOTOR_GRABBER_PORT  = MotorPort.C;

	/*
	 * private Display display = new Display(); private TouchSensor touchSensor
	 * = new TouchSensor(TOUCH_SENSOR); private ColourSensor colourSensor = new
	 * ColourSensor(COLOUR_SENSOR); private InfraredSensor infraredSensor = new
	 * InfraredSensor(INFRARED_SENSOR); private MonitoringTouch monitoringTouch
	 * = new MonitoringTouch(touchSensor); private MotorController leftMotor =
	 * new MotorController(MOTOR_LEFT); private MotorController rightMotor = new
	 * MotorController(MOTOR_RIGHT); private Grabber grabber = new
	 * Grabber(MOTOR_GRABBER); //private static Navi navi = new Navi(leftMotor,
	 * rightMotor); private GoToGoal gotogoal = new GoToGoal(leftMotor,
	 * rightMotor); private Publish publisher = new Publish(infraredSensor,
	 * colourSensor);
	 */
	private static EventManager eventManager;
	private static Buttons buttonsManager;

	private static void startEventManager() {
		eventManager = new EventManager();
		eventManager.start();
	}

	private static void startButtons() {
		buttonsManager = new Buttons();
		buttonsManager.start();
	}
	
	public static void processMission() {
		synchronized (eventManager) {
			eventManager.notify();
		}
		synchronized (buttonsManager) {
			buttonsManager.notify();
		}
	}	
	
	public static void main(String[] args) {
		boolean isRunning = true;
		int period = 1000;
		
		startEventManager();
		startButtons();
		
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

package components;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;
import behaviours.RobotStatus;

public class Grabber extends EV3MediumRegulatedMotor{
	
	private static int DEFAULT_DEGREES = 80; 
	private static int DEFAULT_SPEED = 80;
	private MonitoringGrabber monitoring = new MonitoringGrabber();
	private boolean isToBeOpened = false;
	private static final int GRABBER_OPENED = 0;
	private static final int GRABBER_CLOSED = 1;
	private static int currentStatus = GRABBER_OPENED;

	public Grabber(Port port) {
		super(port);
		super.stop();
		super.setSpeed(DEFAULT_SPEED);
		super.resetTachoCount();
		currentStatus = GRABBER_OPENED;
		monitoring.start();
	}
	
	private void toOpen() {
		isToBeOpened = true;
	}
	
	private void toClose() {
		isToBeOpened = false;
	}	
	
	public void checkRobotStatus() {
		if((RobotStatus.getCurrentGrabberStatus() == RobotStatus.GRABBER_OPENED) 
			&& (isToBeOpened == false)){
			toOpen();
		}
		if((RobotStatus.getCurrentGrabberStatus() == RobotStatus.GRABBER_CLOSED) 
			&& (isToBeOpened == true)){
			toClose();	
		}
	}
	
	public int getCurrentStatus() {
		return currentStatus;
	}
	 
	private class MonitoringGrabber extends Thread{
		
		private int UPDATE_TIMER = 1000;
		
		@Override
		public void run() {
			/*int prevPosition = 0;
			int currentStatus = GRABBER_OPENED;
			*/
			while(true){
				checkRobotStatus();
				
				if ((isToBeOpened == true) && (currentStatus == GRABBER_CLOSED)) {
					resetTachoCount();
					rotate(DEFAULT_DEGREES);
					currentStatus = GRABBER_OPENED;
				}
				if ((isToBeOpened == false) && (currentStatus == GRABBER_OPENED)) {
					resetTachoCount();
					rotate(DEFAULT_DEGREES * -1 );
					currentStatus = GRABBER_CLOSED;
				}
				
				Delay.msDelay(UPDATE_TIMER);

			}
		}
	}
}

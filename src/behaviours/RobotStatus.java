package behaviours;

import lejos.hardware.Button;
import lejos.utility.Delay;

/**
 * 
 * LED pattern
 *0: turn off button lights
 *1/2/3: static light green/red/yellow
 *4/5/6: normal blinking light green/red/yellow
 *7/8/9: fast blinking light green/red/yellow
 *>9: same as 9.
 *
 * @author scottish
 *
 */
public class RobotStatus {

	public static final int STATE_STOP = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_EXIT = 2;
	public static final int GRABBER_OPENED = 0;
	public static final int GRABBER_CLOSED = 1;;
	private static int prevRobotStatus = -1;
	private static int currentRobotStatus = STATE_STOP;
	private static int prevGrabberStatus = GRABBER_CLOSED;
	private static int currentGrabberStatus = GRABBER_OPENED;
	private static double posX = 0;
	private static double posY = 0;

	public static int getPrevRobotStatus() {
		return prevRobotStatus;
	}

	public static int getCurrentRobotStatus() {
		return currentRobotStatus;
	}
	
	public static int getCurrentGrabberStatus() {
		return currentGrabberStatus;
	}

	public static void setCurrentRobotStatus(int currentState) {
		if (prevRobotStatus != currentState) {
			prevRobotStatus = currentState;
			RobotStatus.currentRobotStatus = currentState;
		}
		switch (RobotStatus.currentRobotStatus) {
		case STATE_STOP:
			Button.LEDPattern(1);
			break;
		case STATE_RUNNING:
			Button.LEDPattern(2);
			break;
		case STATE_EXIT:
			Button.LEDPattern(6);
			break;
		default:
			Button.LEDPattern(0);
			break;
		}

	}
	
	public static void setCurrentGrabberStatus(int currentState) {
		if (prevGrabberStatus != currentGrabberStatus) {
			prevGrabberStatus = currentGrabberStatus;
			RobotStatus.currentGrabberStatus = currentState;
		}
	}
	
	public static double getPosX() {
		return posX;
	}

	public static void setPosX(double posX) {
		RobotStatus.posX = posX;
	}

	public static double getPosY() {
		return posY;
	}

	public static void setPosY(double posY) {
		RobotStatus.posY = posY;
	}
}

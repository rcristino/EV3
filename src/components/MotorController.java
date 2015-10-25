package components;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import behaviours.RobotStatus;

public class MotorController extends EV3LargeRegulatedMotor {
	
	public static final int FORWARD = 1;
	public static final int BACKWARDS = 3;
	public static final int STOP = 4;
    private static int currentDirection = 0;
    private static int numTurns = 0;
    
	public MotorController(Port port) {
		super(port);
		super.stop();
		super.getTachoCount();;
	}
	
	public void setSpeed(int degreePerSec){
		if(degreePerSec > 0){
			super.setSpeed(degreePerSec);
		}
		
	}

	public int getSpeed() {
		return super.getSpeed();
	}
	
	public void setDirection(int direction){
		currentDirection = direction;
		switch (direction) {
		case FORWARD:
			super.forward();
			break;
		case BACKWARDS:
			super.backward();
			break;
		case STOP:
			super.stop();
			if (RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_STOP) {
				super.resetTachoCount();
			}
			break;
		default:
			super.stop();
			break;
		}
	}

	public int getDirection(){
		return currentDirection;
	}
	
	public void setWheelDegree(int degrees){
		if (degrees > 0) { /* only positive number to not put reverse, use BACKWRDS for that effect */
			super.rotate(degrees);
		}
	}
	
	public double getNumWheelTurns(){
		double result = this.getTachoCount();
		return result;
		
	}
	
}

package behaviours;

import lejos.hardware.Button;
import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;
import lejos.utility.Delay;

import components.TouchSensor;

/**
 * @author scottish
 *
 */
/**
 * @author scottish
 *
 */
public class MonitoringTouch extends Thread{
	
	private static long TIME_WAIT = 100;
	public static int GRABBER_OPENED = 0;
	public static int GRABBER_CLOSED = 1;
	public static int GRABBER_MOVING = 2;
	private TouchSensor touch;
	
	public MonitoringTouch(TouchSensor _touchSensor) {
		touch = _touchSensor;
	}

	private void soundTouched(){
		LocalEV3.get().getAudio();
		LocalEV3.get().getAudio().systemSound(Sounds.BEEP);
	}			
	
	@Override
	public void run() {
		int prevNumClicks = -1;
		int numClicks = TouchSensor.NO_CLICK;
		while (RobotStatus.getCurrentRobotStatus() != RobotStatus.STATE_EXIT) {
			
			if(Button.ESCAPE.isDown()){
				this.soundTouched();
				RobotStatus.setCurrentRobotStatus(RobotStatus.STATE_EXIT);
			}
			
			if((Button.ENTER.isDown()) && (RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_RUNNING)){
				this.soundTouched();
				if (RobotStatus.getCurrentGrabberStatus() == RobotStatus.GRABBER_OPENED) {
					RobotStatus.setCurrentGrabberStatus(RobotStatus.GRABBER_CLOSED);
				} else {
					RobotStatus.setCurrentGrabberStatus(RobotStatus.GRABBER_OPENED);
				}
			}
			
			numClicks = touch.isTouched(); 
			if(numClicks!=prevNumClicks){
				if((numClicks == TouchSensor.NO_CLICK) && (RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_STOP)){
					LocalEV3.get().getAudio();
					LocalEV3.get().getAudio().systemSound(Sounds.DOUBLE_BEEP);
					
				} else if((numClicks == TouchSensor.SINGLE_CLICK) && (RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_STOP)){
					this.soundTouched();
					RobotStatus.setCurrentRobotStatus(RobotStatus.STATE_RUNNING);
					
				} else 	if((numClicks == TouchSensor.SINGLE_CLICK) && (RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_RUNNING)){
					this.soundTouched();
					RobotStatus.setCurrentRobotStatus(RobotStatus.STATE_STOP);
				} 
			}
			prevNumClicks = numClicks;
			Delay.msDelay(TIME_WAIT);


		}
	}
	
	
}

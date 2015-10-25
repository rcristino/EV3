package components;

import java.text.DecimalFormat;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import behaviours.RobotStatus;

public class Display extends Thread{
	
	private static int UPDATE_DISPLAY_TIMER = 1000;
	private static boolean isUpdating = false;
    
	public Display() {
	}
	
	public synchronized String startingMessage(){
		long timeToUpdate = 0;
		String result = "UNKNOWN";
		
		LCD.clear();
		String helloTxt = new String("Hello!");
		String buttonTxt = new String("Press the button");
		String startTxt = new String("to start");
		
		result = new String(helloTxt + ";" + buttonTxt + ";" + startTxt);
		
		LCD.drawString(helloTxt, 0,2);
		LCD.drawString(buttonTxt, 0,4);
		LCD.drawString(startTxt, 0,5);
		LCD.refresh();
		timeToUpdate = LCD.getRefreshCompleteTime() - System.currentTimeMillis();
		if(timeToUpdate > 0)
			Delay.msDelay(timeToUpdate);
		return result;
	}
	
	private synchronized String updateDisplay(){
		DecimalFormat df = new DecimalFormat("#.##");
		double poxX = Double.valueOf(df.format(RobotStatus.getPosX()));
		double poxY = Double.valueOf(df.format(RobotStatus.getPosY()));
		long timeToUpdate = 0;
		String result = "UNKNOWN";

		String batteryTxt = new String("Battery: " + String.format("%.5g",LocalEV3.get().getPower().getBatteryCurrent()));
		String voltageTxt = new String("Voltage: " + String.format("%.5g",LocalEV3.get().getPower().getVoltage()));
		String positionTxt = new String("X: " + poxX + " Y: " + poxY);
		String grabberStatusTxt = "UNKNOWN";
		if(RobotStatus.getCurrentGrabberStatus() == RobotStatus.GRABBER_OPENED)
			grabberStatusTxt = new String("Grabber: OPENED");
		else
			grabberStatusTxt = new String("Grabber: CLOSED");
		
		result = new String(batteryTxt + ";" + voltageTxt + ";" + positionTxt + ";" + grabberStatusTxt);
		
		LCD.clear();
		LCD.drawString(batteryTxt, 0,0);
		LCD.drawString(voltageTxt, 0,1);
		LCD.drawString(positionTxt, 0,2);
		LCD.drawString(grabberStatusTxt, 0,3);
		LCD.refresh();
		timeToUpdate = LCD.getRefreshCompleteTime() - System.currentTimeMillis();
		if(timeToUpdate > 0)
			Delay.msDelay(timeToUpdate);
		
		return result;
		
	}
	
	public synchronized void stopDisplay(){
		isUpdating = false;
	}
	
	@Override
	public void run() {
		isUpdating = true;
		while(isUpdating){
			if(RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_RUNNING){
				this.updateDisplay();
			} else if(RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_STOP){
				this.startingMessage();
			}
			
			Delay.msDelay(UPDATE_DISPLAY_TIMER);

		}
		LCD.clear();
	}
}

package core;

import lejos.utility.Delay;
import behaviours.GoToGoal;
import behaviours.MonitoringTouch;
import behaviours.RobotStatus;

import components.ColourSensor;
import components.Display;
import components.Grabber;
import components.InfraredSensor;
import components.MotorController;
import components.Publish;
import components.TouchSensor;

public class RobotManager {
	
	private static int  TIME_WAIT = 1000;
	
	private Display  display = new Display();
	private TouchSensor touchSensor = new TouchSensor(RobotConfiguration.TOUCH_SENSOR);
	private ColourSensor colourSensor = new ColourSensor(RobotConfiguration.COLOUR_SENSOR);
	private InfraredSensor infraredSensor = new InfraredSensor(RobotConfiguration.INFRARED_SENSOR);
	private MonitoringTouch monitoringTouch = new MonitoringTouch(touchSensor);
	private MotorController leftMotor = new MotorController(RobotConfiguration.MOTOR_LEFT);
	private MotorController rightMotor = new MotorController(RobotConfiguration.MOTOR_RIGHT);
	private Grabber grabber = new Grabber(RobotConfiguration.MOTOR_GRABBER);
	//private static Navi navi  = new Navi(leftMotor, rightMotor);
	private GoToGoal gotogoal = new GoToGoal(leftMotor, rightMotor);
	private Publish publisher = new Publish(infraredSensor, colourSensor);
	
	public RobotManager() {
		startRobot();
		startNavi();
	}

	private void startRobot() {
		
		publisher.start();
		
		RobotStatus.setCurrentRobotStatus(RobotStatus.STATE_STOP);
		
		//TODO create internal threads and start them in constructor e.g Grabber
		monitoringTouch.start(); 
		display.start();
	}
	
	private void stopRobot() {
		RobotStatus.setCurrentGrabberStatus(RobotStatus.GRABBER_OPENED);
		while(RobotStatus.getCurrentGrabberStatus() != RobotStatus.GRABBER_OPENED)
			Delay.msDelay(TIME_WAIT); //wait until the action is finished
		
		leftMotor.setDirection(MotorController.STOP);
		rightMotor.setDirection(MotorController.STOP);
		display.stopDisplay();
		
		gotogoal.stopGoToGoal();
	}
	
	private void startNavi(){

		while(true){
			if(RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_EXIT){
				exit();
			}
			
			if (RobotStatus.getCurrentRobotStatus()== RobotStatus.STATE_RUNNING){
				if(gotogoal.isCompleted()){
					gotogoal.newWaypoint(-100,100,0);
					gotogoal.navigate();
				}
			}
			/*
			if (RobotStatus.getCurrentRobotStatus() == RobotStatus.STATE_STOP){
				stopRobot(); remove this line
			}*/
			
			//send data to computer
			publisher.fetchSample();
			
			Delay.msDelay(TIME_WAIT);
			
		}
	}
	
	private void exit(){
		stopRobot();
		touchSensor.close();
		leftMotor.close();
		rightMotor.close();
		grabber.close();
		colourSensor.close();
		Delay.msDelay(TIME_WAIT);
		System.exit(0);
	}
}

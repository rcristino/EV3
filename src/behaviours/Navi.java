package behaviours;

import lejos.utility.Delay;

import components.MotorController;

public class Navi extends Thread{

	private static double RADIUS = 0.016; /* meters */
	private static int DEFAULT_SPEED = 90;  
	private static double posX = 0;
	private static double posY = 0;
	private static double prevPosX = 0;
	private static double prevPosY = 0;
	private static double targetPosX = 0;
	private static double targetPosY = 0;
	private static double speed = 0;
	private static boolean isOnOngoing = false;
	private MotorController leftMotor;
	private MotorController rightMotor;
	private int UPDATE_NAVI_TIMER = 100;
	
	public Navi(MotorController _leftMotor, MotorController _righMotor) {
		super("Navi");
		leftMotor = _leftMotor;
		rightMotor = _righMotor;
		leftMotor.stop();
		rightMotor.stop();
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		isOnOngoing = false;
	}
	
	public void goTo(double x, double y){
		if(isOnOngoing==false){
			targetPosX = x;
			targetPosY = y;
			isOnOngoing = true;
		}
	}
	
	public boolean isOnOngoing(){
		return isOnOngoing;
	}

	public double getPosX() {
		return posX;
	}

	public  void setPosX(double posX) {
		Navi.posX = posX;
	}

	public  double getPosY() {
		return posY;
	}

	public  void setPosY(double posY) {
		Navi.posY = posY;
	}

	public double getTargetPosX() {
		return targetPosX;
	}

	public void setTargetPosX(double targetPosX) {
		Navi.targetPosX = targetPosX;
	}

	public  double getTargetPosY() {
		return targetPosY;
	}

	public void setTargetPosY(double targetPosY) {
		Navi.targetPosY = targetPosY;
	}
	
		
	private void calculate(){
		double deltaX = 0;
		double deltaY = 0;
		prevPosX = posX;
		prevPosY = posY;
		posX =  (Math.PI * 2 * RADIUS) * (leftMotor.getNumWheelTurns() / 360);
		posY = (Math.PI * 2 * RADIUS) * rightMotor.getNumWheelTurns() / 360;
		
		RobotStatus.setPosX(posX);
		RobotStatus.setPosY(posY);
		
		deltaX = targetPosX - posX; 
		deltaY = targetPosY - posY;

		if(deltaX > 0){
			leftMotor.setDirection(MotorController.FORWARD);
			leftMotor.setSpeed(DEFAULT_SPEED);
			rightMotor.setDirection(MotorController.FORWARD);
			rightMotor.setSpeed(DEFAULT_SPEED);
		}
		if(deltaX <= 0){
			//TODO turn left or right and go to Y position
			//create turn left ad right functions
			leftMotor.setDirection(MotorController.STOP);
			rightMotor.setDirection(MotorController.STOP);
			isOnOngoing = false;
			
		}
	}
		

	@Override
	public void run(){
		
		while(true){
			if(isOnOngoing == true){
				calculate();
			} else  {
				leftMotor.setDirection(MotorController.STOP);
				rightMotor.setDirection(MotorController.STOP);
			}
			
			Delay.msDelay(UPDATE_NAVI_TIMER);

		}
		
	}
}

package controllers;

public class SpeedController extends PidController {

	private int speed = 0;
	private int speedMax = 0;
	private int speedMin = 0;
	private double prevPosition = 0;
	
	public SpeedController(double _kp, double _ki, double _kd, int _period,
			int _speedMax, int _speedMin) {
		super(_kp, _ki, _kd, _period);
		speedMax = _speedMax;
		speedMin = _speedMin;
		
	}
	
	protected void initController(){
		super.initController();		
		speed = speedMax;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	@Override
	protected void calculate() {
		super.calculate();
		speed = (int)((prevPosition - super.position) / period);
    	if(speed > speedMax)
    		speed = speedMax;
    	if(speed < speedMin)
    		speed = speedMin;
    	prevPosition = position;
	}

	@Override
	public String toString() {
		return super.toString() + "\nSpeedController [speed=" + speed + ", speedMax=" + speedMax
				+ ", speedMin=" + speedMin + "]";
	}

}

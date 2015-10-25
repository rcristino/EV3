package core;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

public class RobotConfiguration {

	public final static Port TOUCH_SENSOR = SensorPort.S3;
	public final static Port INFRARED_SENSOR = SensorPort.S2;
	public final static Port COLOUR_SENSOR = SensorPort.S1;
	public final static Port MOTOR_LEFT = MotorPort.A;
	public final static Port MOTOR_RIGHT = MotorPort.B; 
	public final static Port MOTOR_GRABBER = MotorPort.C;
}

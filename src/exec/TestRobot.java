package exec;

import java.text.DecimalFormat;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import behaviour.MoveManager;

public class TestRobot {

	public final static Port TOUCH_SENSOR_PORT = SensorPort.S3;
	public final static Port INFRARED_SENSOR_PORT = SensorPort.S2;
	public final static Port COLOUR_SENSOR_PORT = SensorPort.S1;
	public final static Port MOTOR_LEFT_PORT = MotorPort.A;
	public final static Port MOTOR_RIGHT_PORT = MotorPort.B;
	public final static Port MOTOR_GRABBER_PORT = MotorPort.C;
	public final static double WHEEL_RADIUS = 16; /* mm */
	public final static int TRAVEL_SPEED = 40;
	public final static int ROTATE_SPEED = 60;
	public final static int TRACK_WITH = 120;
	public final static int PLAY_GROUND_SIZE = 200;
	public final static int NUMBER_PATH_POINTS = 1;
	public final static int CLOCK = 10; /* msecs */

	public static void main(String[] args) throws InterruptedException {
		MoveManager mv = new MoveManager();
		mv.start();
		while (true) {
			DecimalFormat df = new DecimalFormat("#.#");
			double posX = Double.valueOf(df.format(MoveManager.getPosition()
					.getX()));
			double posY = Double.valueOf(df.format(MoveManager.getPosition()
					.getY()));
			double posH = Double.valueOf(df.format(MoveManager.getPosition()
					.getHeading()));
			String positionTxt = new String(posX + ":" + posY + ":" + posH);
			LCD.drawString(positionTxt, 0, 3);

			if (Button.ENTER.isDown()) {
				MoveManager.newWaypoint(100, 100, 0);

			}
			if (Button.UP.isDown()) {
				MoveManager.goStraight(50);
			}
			if (Button.ESCAPE.isDown()) {
				System.exit(0);
			}

			synchronized (mv) {
				mv.notify();
			}
			Thread.sleep(5000);
		}

	}

}

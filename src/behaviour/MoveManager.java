package behaviour;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import exec.RickRobot;

public class MoveManager extends Thread {

	private DifferentialPilot pilot = null;
	private static PoseProvider pose = null;;
	private static Navigator navi = null;
	private Waypoint target = null;
	private EV3LargeRegulatedMotor leftMotor;
	private EV3LargeRegulatedMotor righMotor;
	private final static boolean isReverse = false;

	public MoveManager() {
		super("MoveManager");
		leftMotor = new EV3LargeRegulatedMotor(RickRobot.MOTOR_LEFT_PORT);
		righMotor = new EV3LargeRegulatedMotor(RickRobot.MOTOR_RIGHT_PORT);
		pilot = new DifferentialPilot(RickRobot.WHEEL_RADIUS * 2,
				RickRobot.WHEEL_RADIUS * 2, RickRobot.TRACK_WITH, leftMotor,
				righMotor, isReverse);
		pilot.setTravelSpeed(RickRobot.TRAVEL_SPEED);
		pilot.setRotateSpeed(RickRobot.ROTATE_SPEED);
		pose = new OdometryPoseProvider(pilot);
		navi = new Navigator(pilot);
	}

	/**
	 * Set the point to pass by as x and y. Orientation is zero degrees. x =
	 * robot heading y = left side from robot heading
	 * 
	 * @param x
	 * @param y
	 * @param heading
	 */
	public static void newWaypoint(int x, int y, float heading) {
		navi.addWaypoint(x, y, heading);
	}

	/**
	 * LED pattern 0: turn off button lights 1/2/3: static light
	 * green/red/yellow 4/5/6: normal blinking light green/red/yellow 7/8/9:
	 * fast blinking light green/red/yellow >9: same as 9.
	 * 
	 * @param pattern
	 */
	private void setLED(int pattern) {
		LocalEV3.get().getLED().setPattern(pattern);
	}

	public static void newWaypoint(Position pos) {
		navi.addWaypoint(pos.getX(), pos.getY(), pos.getHeading());
	}

	public static void goStraight(float distance) {
		Double pX = navi.getPoseProvider().getPose().pointAt(distance, 0)
				.getX();
		Double pY = navi.getPoseProvider().getPose().pointAt(distance, 0)
				.getY();
		navi.addWaypoint(Float.valueOf(pX.toString()),
				Float.valueOf(pY.toString()), navi.getPoseProvider().getPose()
						.getHeading());
	}

	public static float[] getPoint() {
		float[] result = new float[3];
		result[0] = pose.getPose().getX();
		result[1] = pose.getPose().getY();
		result[2] = pose.getPose().getHeading();
		return result;
	}

	public static Position getPosition() {
		Position pos = new Position(0, 0, 0);
		synchronized (navi) {
			pos = new Position(pose.getPose().getX(), pose.getPose().getY(),
					pose.getPose().getHeading());
		}
		return pos;
	}

	public static Position getDestinationPosition() {
		Position pos = new Position(0, 0, 0);
		synchronized (navi) {
			if (navi.getWaypoint() != null) {
				pos = new Position(navi.getWaypoint().getPose().getX(), navi
						.getWaypoint().getPose().getY(), navi.getWaypoint()
						.getPose().getHeading());
			}
		}

		return pos;
	}

	public static void resetMove() {
		navi.clearPath();
		// TODO there are EventMove in queue which need to discard
		while (navi.isMoving()) {
			navi.clearPath();
		}
	}

	public static boolean isMoving() {
		return navi.isMoving();
	}

	public static boolean isPathCompleted() {
		return navi.pathCompleted();
	}

	@Override
	public void run() {
		boolean isRunning = true;

		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processMove();

				} catch (InterruptedException e) {
					isRunning = false;
					navi.clearPath();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	private void processMove() {
		if (!navi.pathCompleted()) {
			navi.followPath();
			target = navi.getWaypoint();
			this.setLED(2);
		} else {
			this.setLED(1);
		}
	}

	@Override
	public String toString() {
		return "Move To: " + "(" + (int) target.getX() + ","
				+ (int) target.getY() + " posX: "
				+ navi.getPoseProvider().getPose().getX() + " posY: "
				+ navi.getPoseProvider().getPose().getY() + " heading: "
				+ navi.getPoseProvider().getPose().getHeading() + ")";
	}

}

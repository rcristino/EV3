package behaviour;

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
				RickRobot.WHEEL_RADIUS * 2, RickRobot.TRACK_WITH,
				leftMotor, righMotor, isReverse);
		pilot.setTravelSpeed(RickRobot.TRAVEL_SPEED);
		pilot.setRotateSpeed(RickRobot.ROTATE_SPEED);
		pose = new OdometryPoseProvider(pilot);
		navi = new Navigator(pilot);
	}

	/**
	 * Set the point to pass by as x and y. the initial orientation is 45
	 * degrees
	 * 
	 * @param x
	 * @param y
	 * @param heading
	 */
	public static void newWaypoint(int x, int y, float heading) {
		navi.addWaypoint(x, y, heading);
	}
	
	public static void newWaypoint(Position pos) {
		navi.addWaypoint(pos.getX(), pos.getY(), pos.getHeading());
	}

	public static float[] GetPoint() {
		float[] result = new float[3];
		result[0] = pose.getPose().getX();
		result[1] = pose.getPose().getY();
		result[2] = pose.getPose().getHeading();
		return result;
	}
	
	public static Position GetPosition() {
		Position pos = new Position(pose.getPose().getX(), pose.getPose().getY(), pose.getPose().getHeading());
		return pos;
	}

	public static void resetMove() {
		navi.stop();
		navi.clearPath();
	}

	public static boolean isMoving() {
		return navi.isMoving();
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
					navi.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	private void processMove() {
		if (!navi.pathCompleted()) {
			navi.followPath();
			target = navi.getWaypoint();
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

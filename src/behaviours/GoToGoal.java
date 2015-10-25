package behaviours;

import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import components.MotorController;
import computer.RobotLogger;

public class GoToGoal {
	
	private final double WHEEL_RADIUS = 16; /* mm */
	private final int TRAVEL_SPEED = 20; 
	private final int ROTATE_SPEED = 30; 
	private final int TRACK_WITH = 100; 
	private final boolean REVERSE = false;
	private DifferentialPilot pilot = null;
	private PoseProvider pose = null;;
	private Navigator navi = null;
	private Waypoint next= null;
	
	public GoToGoal(MotorController _leftMotor, MotorController _righMotor) {
		pilot = new DifferentialPilot(WHEEL_RADIUS*2, WHEEL_RADIUS*2, TRACK_WITH,_leftMotor,  _righMotor, REVERSE);
		pilot.setTravelSpeed(TRAVEL_SPEED);
		pilot.setRotateSpeed(ROTATE_SPEED);
		pose = new OdometryPoseProvider(pilot);
		navi = new Navigator(pilot);

	}
	
    /**
     * Set the point to pass by as x and y.
     * the initial orientation is 45 degrees
     * @param x
     * @param y
     * @param heading
     */
    public void newWaypoint(int x, int y,float heading){
    	navi.addWaypoint(x, y, heading);
    }
    
    public void newPath(Path path){
    	navi.setPath(path);
    }
    
    public void newPose(Pose _pose){
    	pose.setPose(_pose);
    }
    
    public Pose reportPose(){
    	return pose.getPose();
    }
    
    public void stopGoToGoal(){
    	navi.stop();
    }
    
    public boolean isCompleted(){
    	return navi.pathCompleted();
    }
    
    public void navigate(){
		while(!navi.pathCompleted()){
			navi.followPath();
		    next = navi.getWaypoint();
		    RobotStatus.setPosX(navi.getPoseProvider().getPose().getX());
		    RobotStatus.setPosY(navi.getPoseProvider().getPose().getY());
		    
		    //RobotLogger.log("Moving to: " + "(" + (int)next.getX() + "," + (int)next.getY() + " posX: " + navi.getPoseProvider().getPose().getX() + " posY: " + navi.getPoseProvider().getPose().getY() + " heading: " +navi.getPoseProvider().getPose().getHeading() +")");
		}
    }

}

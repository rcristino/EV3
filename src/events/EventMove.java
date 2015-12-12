package events;

import java.util.Random;

import behaviour.MoveManager;
import behaviour.Position;
import events.EventGrabber.GrabberStatus;
import exec.RickRobot;

public class EventMove extends Event implements IEvent {

	public static enum Action {
		STOP, MOVE_POSITION, MOVE_RANDOM, MOVE_STRAIGHT
	}

	private Action action;
	private float distance;
	private Position position;

	public EventMove(Position position) {
		super(Event.Type.MOVE);
		action = Action.MOVE_POSITION;
		this.position = position;
	}

	public EventMove(float distance) {
		super(Event.Type.MOVE);
		action = Action.MOVE_STRAIGHT;
		this.distance = distance;
	}

	public EventMove() {
		super(Event.Type.MOVE);
		action = Action.MOVE_RANDOM;
	}

	public EventMove(Action action) {
		super(Event.Type.MOVE);
		this.action = action;
	}

	public Position getPosition() {
		Position result = MoveManager.getPosition();
		return result;
	}

	public Action getAction() {
		return this.action;
	}

	public boolean isMoving() {
		return MoveManager.isMoving();
	}

	@Override
	public void execute() {
		super.execute();

		switch (this.action) {
		case MOVE_RANDOM:
			MoveManager.resetMove();
			Random radomGenerator = new Random();
			for (int i = 0; i < RickRobot.NUMBER_PATH_POINTS; i++) {
				int x = radomGenerator.nextInt(RickRobot.PLAY_GROUND_SIZE);
				int y = radomGenerator.nextInt(RickRobot.PLAY_GROUND_SIZE);
				float heading = radomGenerator.nextInt(360); // max degrees
				position = new Position(x, y, heading);
				MoveManager.newWaypoint(position);
			}
			this.setActive(false);
			break;
		case MOVE_STRAIGHT:
			MoveManager.resetMove();
			MoveManager.goStraight(distance);
			this.setActive(false);
			break;
		case MOVE_POSITION:
			MoveManager.resetMove();
			MoveManager.newWaypoint(new Position(this.position.getX(),
					this.position.getY(), this.position.getHeading()));
			this.setActive(false);
			break;
		case STOP:
			MoveManager.resetMove();
			this.setActive(false);
			break;
		default:
			EventManager.addEvent(new EventStatus(EventStatus.Status.EXIT));
			break;
		}
	}
}

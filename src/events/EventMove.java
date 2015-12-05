package events;

import java.util.ArrayList;
import java.util.Random;

import behaviour.MoveManager;
import behaviour.Position;
import exec.RickRobot;

public class EventMove extends Event implements IEvent {

	public static enum Action {
		STOP, MOVE
	}

	private Action action;
	private ArrayList<Position> posList;

	public EventMove(Position pos) {
		super(Event.Type.MOVE);
		posList = new ArrayList<>();
		posList.add(new Position(pos.getX(), pos.getY(), pos.getHeading()));
		action = Action.MOVE;
	}

	public EventMove() {
		super(Event.Type.MOVE);
		posList = new ArrayList<>();
		action = Action.MOVE;

		Random radomGenerator = new Random();
		for (int i = 0; i < RickRobot.NUMBER_PATH_POINTS; i++) {
			int x = radomGenerator.nextInt(RickRobot.PLAY_GROUND_SIZE);
			int y = radomGenerator.nextInt(RickRobot.PLAY_GROUND_SIZE);
			float heading = radomGenerator.nextInt(360); // max degrees
			posList.add(new Position(x, y, heading));
		}

	}

	public EventMove(Action action) {
		super(Event.Type.MOVE);
		this.action = action;
	}

	@Override
	public void execute() {
		
		switch (this.action) {
		case MOVE:
			for (Position pos : posList) {
				MoveManager.newWaypoint(pos);
			}
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
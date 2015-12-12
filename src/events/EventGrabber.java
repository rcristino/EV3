package events;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import behaviour.Position;
import exec.RickRobot;

public class EventGrabber extends Event implements IEvent {

	public static enum GrabberStatus {
		OPENED, CLOSED, ACTION
	}

	private static int DEFAULT_DEGREES = 80;
	private static int DEFAULT_SPEED = 80;
	private static GrabberStatus currentStatus;
	private static EV3MediumRegulatedMotor grabberMotor = null;
	private GrabberStatus status;

	public EventGrabber(GrabberStatus status) {
		super(Event.Type.GRABBER);
		this.status = status;
		if (grabberMotor == null) {
			grabberMotor = new EV3MediumRegulatedMotor(
					RickRobot.MOTOR_GRABBER_PORT);
			grabberMotor.stop();
			grabberMotor.setSpeed(DEFAULT_SPEED);
			grabberMotor.resetTachoCount();
			currentStatus = EventGrabber.GrabberStatus.OPENED;
		}

	}

	public GrabberStatus getGrabberStatus() {
		return this.status;
	}

	@Override
	public void execute() {
		super.execute();

		//TODO to be improved, probably with new Event Kind
		EventMove evtMove = (EventMove) EventManager
				.getLastEvent(Event.Type.MOVE);
		if (evtMove != null
				&& evtMove.getAction() == EventMove.Action.MOVE_STRAIGHT
				&& !evtMove.isMoving()) {
			EventManager.addEvent(new EventGrabber(GrabberStatus.CLOSED));
			EventManager.addEvent(new EventMove(new Position(0, 0, 0)));
		}

		switch (this.getGrabberStatus()) {
		case OPENED:
			if (currentStatus == GrabberStatus.CLOSED) {
				grabberMotor.rotateTo(0);
				currentStatus = GrabberStatus.OPENED;
			}
			this.setActive(false);
			break;
		case CLOSED:
			if (currentStatus == GrabberStatus.OPENED) {
				grabberMotor.rotateTo(DEFAULT_DEGREES);
				currentStatus = GrabberStatus.CLOSED;
			}
			this.setActive(false);
			break;
		case ACTION:
			if (currentStatus == GrabberStatus.OPENED) {
				EventManager.addEvent(new EventStatus(
						EventStatus.Status.GRABER_CLOSED));
			} else {
				EventManager.addEvent(new EventStatus(
						EventStatus.Status.GRABER_OPENED));
			}
			this.setActive(false);
			break;
		default:
			EventManager.addEvent(new EventStatus(EventStatus.Status.EXIT));
			break;
		}

	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
	}

	@Override
	public String toString() {
		return "EventGrabber [status=" + status + "]";
	}

}

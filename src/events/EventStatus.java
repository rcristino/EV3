package events;

import events.EventGrabber.GrabberStatus;
import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;

public class EventStatus extends Event implements IEvent {

	public static enum Status {
		START, IDLE, EXIT, MOVE, GRABER_OPENED, GRABER_CLOSED
	}

	private Status status;

	public EventStatus(Status status) {
		super(Event.Type.STATUS);
		this.status = status;
	}

	public Status getStatus() {
		return this.status;
	}

	@Override
	public void execute() {
		super.execute();
		switch (this.getStatus()) {
		case START:
			LocalEV3.get().getAudio().systemSound(Sounds.ASCENDING);
			EventManager.addEvent(new EventStatus(EventStatus.Status.IDLE));
			this.setActive(false);
			break;
		case IDLE:

			this.setActive(false);
			break;
		case EXIT:
			LocalEV3.get().getAudio().systemSound(Sounds.DESCENDING);
			this.setActive(false);
			
			int numThreads = Thread.activeCount();
			Thread[] tarray = new Thread[numThreads];
			Thread.enumerate(tarray);
			for (Thread thread : tarray) {
				thread.interrupt();
			}
			break;
		case MOVE:
			EventManager.addEvent(new EventMove());
			this.setActive(false);
			break;
		case GRABER_CLOSED:
			EventManager.addEvent(new EventGrabber(
					EventGrabber.GrabberStatus.CLOSED));
			this.setActive(false);
			break;
		case GRABER_OPENED:
			EventManager.addEvent(new EventGrabber(
					EventGrabber.GrabberStatus.OPENED));
			this.setActive(false);
			break;

		default:
			EventManager.addEvent(new EventStatus(EventStatus.Status.EXIT));
			break;
		}
	}

	@Override
	public String toString() {
		return "EventRobotStatus [status=" + status + ", isActivated()="
				+ isActivated() + ", getEventType()=" + getType()
				+ ", getEventId()=" + getId() + ", getTimeStamp()="
				+ getTimeStamp() + "]";
	}

}

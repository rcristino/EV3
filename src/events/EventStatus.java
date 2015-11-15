package events;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;

public class EventStatus extends Event implements IEvent {

	public static enum Status {
		IDLE, EXIT, RUNNING, MOVING
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
		case IDLE:
			LocalEV3.get().getAudio().systemSound(Sounds.ASCENDING);
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
		default:
			break;
		}

	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
	}

	@Override
	public String toString() {
		return "EventRobotStatus [status=" + status + ", isActivated()="
				+ isActivated() + ", getEventType()=" + getType()
				+ ", getEventId()=" + getId() + ", getTimeStamp()="
				+ getTimeStamp() + "]";
	}

}

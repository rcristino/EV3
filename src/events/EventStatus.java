package events;

import lejos.hardware.Sounds;
import lejos.hardware.ev3.LocalEV3;

public class EventStatus extends Event implements IEvent {

	public static enum Status {
		START, 
		IDLE, 
		EXIT, 
		RUNNING, 
		MOVING
	}

	private Status status;

	public EventStatus(Status status) {
		super(Event.Type.STATUS);
		this.status = status;
	}

	public Status getStatus() {
		return this.status;
	}

	/**
	 *LED pattern
	 *0: turn off button lights
	 *1/2/3: static light green/red/yellow
	 *4/5/6: normal blinking light green/red/yellow
	 *7/8/9: fast blinking light green/red/yellow
	 *>9: same as 9.
	 * @param pattern
	 */
	private void setLED(int pattern){
		LocalEV3.get().getLED().setPattern(pattern);
	}
	
	@Override
	public void execute() {
		super.execute();
		switch (this.getStatus()) {
		case START:
			LocalEV3.get().getAudio().systemSound(Sounds.ASCENDING);
			this.setLED(1);
			EventManager.addEvent(new EventStatus(EventStatus.Status.IDLE));
			this.setActive(false);
			break;
		case IDLE:
			this.setLED(1);
			this.setActive(false);
			break;
		case EXIT:
			LocalEV3.get().getAudio().systemSound(Sounds.DESCENDING);
			this.setLED(8);
			this.setActive(false);
			int numThreads = Thread.activeCount();
			Thread[] tarray = new Thread[numThreads];
			Thread.enumerate(tarray);
			for (Thread thread : tarray) {
				thread.interrupt();
			}
			break;
		case RUNNING:
			this.setLED(2);
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
		return "EventRobotStatus [status=" + status + ", isActivated()="
				+ isActivated() + ", getEventType()=" + getType()
				+ ", getEventId()=" + getId() + ", getTimeStamp()="
				+ getTimeStamp() + "]";
	}

}

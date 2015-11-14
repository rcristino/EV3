package events;

public class EventRobotStatus extends Event implements IEvent {

	public static enum Status {
		STOPPED,
		RUNNING,
		MOVING
	} 
	
	private Status status;
	
	public EventRobotStatus(Status status, String info) {
		super(Event.Type.STATUS, info);
		this.status = status;
	}

	public Status getStatus(){
		return this.status;
	}
	@Override
	public void execute() {
		super.execute();
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
				+ getTimeStamp() + ", getInfo()=" + getInfo() + "]";
	}
	

}

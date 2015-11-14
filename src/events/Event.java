package events;

import java.util.Date;


public class Event implements IEvent {

	public static enum Type {
		STATUS,
		POSITION
	}
	
	private static long totalIds = 0;
	private boolean isActive = false;
	private Type eventType;
	private long eventId = -1;
	private Date timeStamp;
	private String info = "Event";
	
	
	public Event(Type type, String info) {
		this.eventId = Event.generateEventId(); 
		this.info = info;
		this.timeStamp = new Date();
		this.eventType = type;
	}
	
	public void execute(){
		this.setActive(true);
	}
	
	public void setActive(boolean active){
		this.isActive = active;
	}
	
	public final boolean isActivated(){
		return this.isActive;
	}
	
    private final static long generateEventId(){
    	totalIds = totalIds + 1;
    	return totalIds;
    }
    
	public final Type getType() {
		return eventType;
	}
	
	public final long getId() {
		return eventId;
	}

	public final Date getTimeStamp() {
		return timeStamp;
	}
	
	public final String getInfo() {
		return info;
	}

	/* (non-Javadoc)
	 * @see events.IEvent#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (eventId ^ (eventId >>> 32));
		result = prime * result + ((info == null) ? 0 : info.hashCode());
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see events.IEvent#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eventId != other.eventId)
			return false;
		if (info == null) {
			if (other.info != null)
				return false;
		} else if (!info.equals(other.info))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see events.IEvent#toString()
	 */
	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", timeStamp=" + timeStamp
				+ ", info=" + info + ", active=" + isActive + "]";
	}

    
}

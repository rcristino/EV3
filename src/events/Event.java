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
	
	
	public Event(Type type) {
		this.eventId = Event.generateEventId(); 
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
	
	/* (non-Javadoc)
	 * @see events.IEvent#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (eventId ^ (eventId >>> 32));
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
				+ ", active=" + isActive + "]";
	}

    
}

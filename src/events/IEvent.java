package events;

import java.util.Date;

import events.Event.Type;

public interface IEvent {

	public abstract void setActive(boolean active);

	public abstract boolean isActivated();

	public abstract Type getType();

	public abstract long getId();

	public abstract Date getTimeStamp();

	public abstract int hashCode();

	public abstract boolean equals(Object obj);

	public abstract String toString();
	
	public abstract void execute();

}
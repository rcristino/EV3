package events;

import component.InfraredSensor;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import exec.RickRobot;

public class EventDistance extends Event implements IEvent {

	private InfraredSensor.Range range;

	public EventDistance(InfraredSensor.Range range) {
		super(Event.Type.DISTANCE);
		this.range = range;	}

	public InfraredSensor.Range getRange() {
		return this.range;
	}

	@Override
	public void execute() {
		super.execute();
		switch (this.range) {
		case UNKNOWN:
			
			this.setActive(false);
			break;
		case SHORT:
			EventManager.addEvent(new EventMove(EventMove.Action.STOP));
			this.setActive(false);
			break;
		case MEDIUM:
			EventManager.addEvent(new EventMove(EventMove.Action.STOP));
			this.setActive(false);
			break;
		case LONG:
			EventManager.addEvent(new EventMove(EventMove.Action.STOP));
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
		return "EventObject [range=" + range + "]";
	}

}

package events;

import java.io.IOException;

import lejos.hardware.sensor.SensorMode;
import lejos.robotics.filter.PublishFilter;

public class EventsPublisher {

	private final static float TRANSMISSION_FREQUENCY_HIGH = 2;
	private final static float TRANSMISSION_FREQUENCY_LOW = 20;

	private PublishFilter publishStatus;
	private float[] sampleStatus;
	private EventPublisherStatus evtPubStatus;

	public EventsPublisher(){
		
	}
	
	public void publishEvent(IEvent evt) {
		switch (evt.getType()) {
		case STATUS:
			publishEventStatus((EventRobotStatus)evt);
			break;

		default:
			break;
		}
	}
	
	private void publishEventStatus(EventRobotStatus evtStatus) {
		try {

			evtPubStatus = new EventPublisherStatus(evtStatus);
			publishStatus = new PublishFilter(evtPubStatus,
					evtPubStatus.getName(), TRANSMISSION_FREQUENCY_LOW);
			sampleStatus = new float[publishStatus.sampleSize()];
			publishStatus.fetchSample(sampleStatus, 0);
		} catch (IOException e) {

		}
	}

	private class EventPublisherStatus implements SensorMode {

		private EventRobotStatus evt;

		public EventPublisherStatus(EventRobotStatus evt) {
			this.evt = evt;
		}

		@Override
		public int sampleSize() {
			return 1;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			sample[0] = (float) new Float(evt.getStatus().ordinal()).floatValue();
		}

		@Override
		public String getName() {
			return "(" + evt.getId() + ") " + evt.getType().name() + ": " + evt.getStatus().name();
		}
	}
}

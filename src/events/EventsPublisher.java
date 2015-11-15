package events;

import java.io.IOException;

import lejos.hardware.sensor.SensorMode;
import lejos.robotics.filter.PublishFilter;

public class EventsPublisher {

	private final static float TRANSMISSION_FREQUENCY_HIGH = 2;
	private final static float TRANSMISSION_FREQUENCY_LOW = 20;

	private PublishFilter publishStatus;
	private float[] sampleStatus;
	private EventPublisher evtPubStatus;
	private boolean isEvtPubStatusInitialised = false;

	public EventsPublisher() {
		EventStatus evtStatus = new EventStatus(EventStatus.Status.IDLE);
		evtPubStatus = new EventPublisher(evtStatus, evtStatus.getType().name());
		try {
			publishStatus = new PublishFilter(evtPubStatus,
					evtPubStatus.getName(), TRANSMISSION_FREQUENCY_LOW);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sampleStatus = new float[publishStatus.sampleSize()];
		isEvtPubStatusInitialised = true;
	}

	public void publishEvent(IEvent evt) {
		switch (evt.getType()) {
		case STATUS:
			publishEventStatus((EventStatus) evt);
			break;

		default:
			break;
		}
	}

	private void publishEventStatus(EventStatus evtStatus) {

		if (isEvtPubStatusInitialised) {
			evtPubStatus.updateEvent(evtStatus);
			publishStatus.fetchSample(sampleStatus, 0);
		}
	}

	private class EventPublisher implements SensorMode {

		private IEvent evt;
		private int numSample = 1;
		private String name = "UNKNOWN";

		public EventPublisher(IEvent evt, String reference) {
			this.evt = evt;
			this.name = reference;
			switch (evt.getType()) {
			case STATUS:
				numSample = 1;
				break;

			default:
				numSample = 1;
				break;
			}
		}

		public void updateEvent(IEvent newEvt) {
			this.evt = newEvt;
		}

		@Override
		public int sampleSize() {
			return numSample;
		}

		@Override
		public void fetchSample(float[] sample, int offset) {
			switch (evt.getType()) {
			case STATUS:
				sample[0] = (float) new Float(((EventStatus) evt).getStatus()
						.ordinal()).floatValue();
				break;

			default:
				sample[0] = -1;
				break;
			}

		}

		@Override
		public String getName() {
			return name;
		}

	}
}

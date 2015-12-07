package events;

import java.io.IOException;
import java.util.ArrayList;

import lejos.hardware.sensor.SensorMode;
import lejos.robotics.filter.PublishFilter;

public class PublisherManager extends Thread {

	private final static float TRANSMISSION_FREQUENCY_HIGH = 2;
	private final static float TRANSMISSION_FREQUENCY_LOW = 20;
	private static Event currentEvent;
	private PublishFilter publishStatus;
	private PublishFilter publishMove;
	private PublishFilter publishGrabber;
	private PublishFilter publishDistance;
	private float[] sampleStatus;
	private float[] sampleMove;
	private float[] sampleGrabber;
	private float[] sampleDistance;
	private EventPublisher evtPubStatus;
	private EventPublisher evtPubMove;
	private EventPublisher evtPubGrabber;
	private EventPublisher evtPubDistance;
	private boolean isEvtPubStatusInitialised = false;
	private boolean isEvtPubMoveInitialised = false;
	private boolean isEvtPubGrabberInitialised = false;
	private boolean isEvtPubDistanceInitialised = false;

	public PublisherManager() {
		super("EventsPublisher");
	}

	@Override
	public void run() {
		boolean isRunning = true;

		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processPubEvent();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	private void processPubEvent() {
		
		if (currentEvent != null) {
			Event evt = currentEvent;

			try {
				switch (evt.getType()) {
				case STATUS:
					if (isEvtPubStatusInitialised == false) {
						evtPubStatus = new EventPublisher(evt, evt.getType()
								.name());
						publishStatus = new PublishFilter(evtPubStatus,
								evtPubStatus.getName(),
								TRANSMISSION_FREQUENCY_LOW);
						sampleStatus = new float[publishStatus.sampleSize()];
						isEvtPubStatusInitialised = true;
					}

					publishEventStatus((EventStatus) evt);
					break;

				case MOVE:
					if (isEvtPubMoveInitialised == false) {
						evtPubMove = new EventPublisher(evt, evt.getType()
								.name());
						publishMove = new PublishFilter(evtPubMove,
								evtPubMove.getName(),
								TRANSMISSION_FREQUENCY_LOW);
						sampleMove = new float[publishMove.sampleSize()];
						isEvtPubMoveInitialised = true;
					}
					publishEventMove((EventMove) evt);
					break;

				case GRABBER:
					if (isEvtPubGrabberInitialised == false) {
						evtPubGrabber = new EventPublisher(evt, evt.getType()
								.name());
						publishGrabber = new PublishFilter(evtPubGrabber,
								evtPubGrabber.getName(),
								TRANSMISSION_FREQUENCY_LOW);
						sampleGrabber = new float[publishGrabber.sampleSize()];
						isEvtPubGrabberInitialised = true;
					}
					publishEventGrabber((EventGrabber) evt);
					break;

				case DISTANCE:
					if (isEvtPubDistanceInitialised == false) {
						evtPubDistance = new EventPublisher(evt, evt.getType()
								.name());
						publishDistance = new PublishFilter(evtPubDistance,
								evtPubDistance.getName(),
								TRANSMISSION_FREQUENCY_LOW);
						sampleDistance = new float[publishDistance.sampleSize()];
						isEvtPubDistanceInitialised = true;
					}
					publishEventDistance((EventDistance) evt);
					break;

				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	}

	public static void publishEvent(Event evt) {
		currentEvent = (evt);
	}

	private void publishEventStatus(EventStatus evtStatus) {

		if (isEvtPubStatusInitialised) {
			evtPubStatus.updateEvent(evtStatus);
			publishStatus.fetchSample(sampleStatus, 0);
		}
	}

	private void publishEventMove(EventMove evtMove) {

		if (isEvtPubMoveInitialised) {
			evtPubMove.updateEvent(evtMove);
			publishMove.fetchSample(sampleMove, 0);
		}
	}

	private void publishEventGrabber(EventGrabber evtGrabber) {

		if (isEvtPubGrabberInitialised) {
			evtPubGrabber.updateEvent(evtGrabber);
			publishGrabber.fetchSample(sampleGrabber, 0);
		}
	}

	private void publishEventDistance(EventDistance evtDistance) {

		if (isEvtPubDistanceInitialised) {
			evtPubDistance.updateEvent(evtDistance);
			publishDistance.fetchSample(sampleDistance, 0);
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
			case MOVE:
				numSample = 3;
				break;
			case GRABBER:
				numSample = 1;
				break;
			case DISTANCE:
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
			case MOVE:
				EventMove evtMove = (EventMove) evt;
				sample[0] = (float) evtMove.getPosition().getX();
				sample[1] = (float) evtMove.getPosition().getY();
				sample[2] = (float) evtMove.getPosition().getHeading();
				break;

			case GRABBER:
				EventGrabber evtGrabber = (EventGrabber) evt;
				sample[0] = (float) new Float(evtGrabber.getGrabberStatus()
						.ordinal()).floatValue();
				break;

			case DISTANCE:
				EventDistance evtDistance = (EventDistance) evt;
				sample[0] = (float) new Float(evtDistance.getRange().ordinal())
						.floatValue();
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

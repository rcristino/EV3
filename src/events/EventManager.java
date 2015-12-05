package events;

import java.util.ArrayList;

public class EventManager extends Thread {

	private final static int MAX_ACTIVE_EVENT = 10;

	private static ArrayList<Event> eventQueueList = null;
	private static ArrayList<Event> eventActiveList = null;
	private static EventsPublisher eventsPublisher = null;

	public EventManager() {
		super("EventManager");
		eventQueueList = new ArrayList<Event>();
		eventActiveList = new ArrayList<Event>();
		EventStatus evtStatus = new EventStatus(EventStatus.Status.START);
		EventManager.addEvent(evtStatus);
		eventsPublisher = new EventsPublisher(evtStatus);
	}

	@Override
	public void run() {
		boolean isRunning = true;

		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processEvents();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	}

	private void processEvents() {
		ArrayList<Event> evtToRemove = new ArrayList<Event>();
		synchronized (eventActiveList) {
			for (Event evt : eventActiveList) {
				if (!evt.isActivated()) {
					evtToRemove.add(evt);
				}
			}
			eventActiveList.removeAll(evtToRemove);
		}
		synchronized (eventQueueList) {
			if (eventActiveList.size() < MAX_ACTIVE_EVENT
					&& !eventQueueList.isEmpty()) {
				Event nextEvt = eventQueueList.get(0);
				if (getEventActive(nextEvt.getType()) == null) {
					nextEvt.execute();
					eventActiveList.add(nextEvt);
					eventQueueList.remove(0);
					eventsPublisher.publishEvent(nextEvt);
				}
			}
		}
	}

	public static Event getEventActive(Event.Type type) {
		Event result = null;
		for (Event evtActive : eventActiveList) {
			if (evtActive.getType() == type) {
				result = evtActive;
				break;
			}
		}
		return result;
	}

	public static void addEvent(Event evt) {
		if (eventQueueList == null) {
			eventQueueList = new ArrayList<>();
			eventActiveList = new ArrayList<>();
		}
		eventQueueList.add(evt);
	}

	public void remvoveEvent(Event evt) {
		evt.setActive(false);
		if (eventQueueList != null) {
			eventQueueList.remove(evt);
			if (eventActiveList.contains(evt)) {
				eventActiveList.remove(evt);
			}
		}
	}

}

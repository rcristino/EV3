package events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class EventManager extends Thread {

	private final static int MAX_ACTIVE_EVENT = 10;

	private static ArrayList<Event> eventQueueList = null;
	private static ArrayList<Event> eventActiveList = null;
	private static HashMap<Event.Type,Event> performedEvent = null;

	public EventManager() {
		super("EventManager");
		eventQueueList = new ArrayList<Event>();
		eventActiveList = new ArrayList<Event>();
		performedEvent = new HashMap<Event.Type,Event>();
		EventStatus evtStatus = new EventStatus(EventStatus.Status.START);
		EventManager.addEvent(evtStatus);
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
		synchronized (eventActiveList) {
			for (Event evt : eventActiveList) {
				if (!evt.isActivated()) {
					performedEvent.put(evt.getType(),evt);
					eventActiveList.remove(evt);
				}
			}
		}
		synchronized (eventQueueList) {
			if (eventActiveList.size() < MAX_ACTIVE_EVENT
					&& !eventQueueList.isEmpty()) {
				Event nextEvt = eventQueueList.get(0);
				if (nextEvt != null && getEventActive(nextEvt.getType()) == null) {
					nextEvt.execute();
					eventActiveList.add(nextEvt);
					eventQueueList.remove(0);
				}
			}
		}
	}

	public static Event getEventActive(Event.Type type) {
		Event result = null;
		synchronized (eventActiveList) {
			for (Event evtActive : eventActiveList) {
				if (evtActive.getType() == type) {
					result = evtActive;
					break;
				}
			}
		}
		return result;
	}
	
	public static Event getLastEvent(Event.Type type) {
		return performedEvent.get(type);
	}
	
	public static ArrayList<Event> getLastEvents() {
		ArrayList<Event> lastEventsList = new ArrayList<Event>();
		Collection<Event> col = performedEvent.values();
		for (Event evt : col) {
			lastEventsList.add(evt);
		}
		return lastEventsList;
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

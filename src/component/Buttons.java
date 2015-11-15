package component;

import lejos.hardware.Button;
import events.EventManager;
import events.EventStatus;

public class Buttons extends Thread {

	@Override
	public void run() {
		boolean isRunning = true;
		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					processButtons();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	}

	private void processButtons() {
		if (Button.ESCAPE.isDown()) {
			EventStatus evt = new EventStatus(EventStatus.Status.EXIT);
			EventManager.addEvent(evt);
		}
		if (Button.UP.isDown()) {
			EventStatus evt = new EventStatus(EventStatus.Status.RUNNING);
			EventManager.addEvent(evt);
		}
		if (Button.DOWN.isDown()) {
			EventStatus evt = new EventStatus(EventStatus.Status.IDLE);
			EventManager.addEvent(evt);
		}
	}
}
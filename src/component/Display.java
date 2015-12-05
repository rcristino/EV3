package component;

import java.text.DecimalFormat;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import behaviour.MoveManager;
import events.Event;
import events.EventManager;
import events.EventStatus;

public class Display extends Thread {

	private static enum Row {
		BATTERY, VOLTAGE, STATUS, POSITION, SENSORS
	}

	private String status = "UNKOWN";

	public Display() {
		super("Display");
	}

	@Override
	public void run() {
		boolean isRunning = true;

		synchronized (this) {
			while (isRunning)
				try {

					this.wait();
					updateDisplay();

				} catch (InterruptedException e) {
					isRunning = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	public synchronized void updateDisplay() {
		DecimalFormat df = new DecimalFormat("#.##");
		double posX = Double.valueOf(df
				.format(MoveManager.GetPosition().getX()));
		double posY = Double.valueOf(df
				.format(MoveManager.GetPosition().getY()));
		double posH = Double.valueOf(df.format(MoveManager.GetPosition()
				.getHeading()));

		EventStatus evtStatus = (EventStatus) EventManager
				.getEventActive(Event.Type.STATUS);
		if (evtStatus != null) {
			status = evtStatus.getStatus().name();
		}

		String batteryTxt = new String("Battery: "
				+ String.format("%.5g", LocalEV3.get().getPower()
						.getBatteryCurrent()));
		String voltageTxt = new String("Voltage: "
				+ String.format("%.5g", LocalEV3.get().getPower().getVoltage()));
		String statusTxt = new String("Status: " + status);
		String positionTxt = new String(posX + ":" + posY + ":" + posH);
		String sensorsTxt = new String("RANGE: " + Integer.toString(InfraredSensor.getRage()));

		LCD.clear();
		LCD.drawString(batteryTxt, 0, Display.Row.BATTERY.ordinal());
		LCD.drawString(voltageTxt, 0, Display.Row.VOLTAGE.ordinal());
		LCD.drawString(statusTxt, 0, Display.Row.STATUS.ordinal());
		LCD.drawString(positionTxt, 0, Display.Row.POSITION.ordinal());
		LCD.drawString(sensorsTxt, 0, Display.Row.SENSORS.ordinal());
		LCD.refresh();

	}
}

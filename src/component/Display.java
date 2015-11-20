package component;

import java.text.DecimalFormat;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import events.Event;
import events.EventManager;
import events.EventStatus;

public class Display {

	private static enum Row {
		BATTERY, VOLTAGE, STATUS, POSITION
	}

	public Display() {
	}

	public synchronized void updateDisplay() {
		DecimalFormat df = new DecimalFormat("#.##");
		// double poxX = Double.valueOf(df.format(RobotStatus.getPosX()));
		// double poxY = Double.valueOf(df.format(RobotStatus.getPosY()));
		double poxX = 0;
		double poxY = 0;

		EventStatus evtStatus = (EventStatus) EventManager
				.getEventActive(Event.Type.STATUS);
		if (evtStatus != null) {
			String batteryTxt = new String("Battery: "
					+ String.format("%.5g", LocalEV3.get().getPower()
							.getBatteryCurrent()));
			String voltageTxt = new String("Voltage: "
					+ String.format("%.5g", LocalEV3.get().getPower()
							.getVoltage()));
			String statusTxt = new String("Status: "
					+ evtStatus.getStatus().name());
			String positionTxt = new String("X: " + poxX + " Y: " + poxY);

			LCD.clear();
			LCD.drawString(batteryTxt, 0, Display.Row.BATTERY.ordinal());
			LCD.drawString(voltageTxt, 0, Display.Row.VOLTAGE.ordinal());
			LCD.drawString(statusTxt, 0, Display.Row.STATUS.ordinal());
			LCD.drawString(positionTxt, 0, Display.Row.POSITION.ordinal());
			LCD.refresh();
		}
	}
}

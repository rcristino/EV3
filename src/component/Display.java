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
		BATTERY, VOLTAGE, STATUS, POSITION, NEXT_POSITION, RANGE
	}

	private String status = "UNKOWN";

	public Display() {
		super("Display");
		LCD.clear();
		LCD.drawString(" Initialising...", 0, Display.Row.STATUS.ordinal());
		LCD.refresh();
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

	private void updateDisplay() {
		DecimalFormat df = new DecimalFormat("#.#");
		double posX = Double.valueOf(df
				.format(MoveManager.getPosition().getX()));
		double posY = Double.valueOf(df
				.format(MoveManager.getPosition().getY()));
		double posH = Double.valueOf(df.format(MoveManager.getPosition()
				.getHeading()));
		double posNextX = Double.valueOf(df
				.format(MoveManager.getDestinationPosition().getX()));
		double posNextY = Double.valueOf(df
				.format(MoveManager.getDestinationPosition().getY()));
		double posNextH = Double.valueOf(df.format(MoveManager.getDestinationPosition()
				.getHeading()));

		EventStatus evtStatus = (EventStatus) EventManager
				.getLastEvent(Event.Type.STATUS);
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
		String positionNextTxt = new String(posNextX + ":" + posNextY + ":" + posNextH);
		InfraredSensor.Range range = InfraredSensor.getRage();
		String rangeTxt = new String("RANGE: ");
		switch (range) {

		case SHORT:
			rangeTxt = rangeTxt + "[|       ]";
			break;
		case MEDIUM:
			rangeTxt = rangeTxt + "[   |     ]";
			break;
		case LONG:
			rangeTxt = rangeTxt + "[      |  ]";
			break;
		case UNKNOWN:
			rangeTxt = rangeTxt + "[        |]";
			break;
		default:
			rangeTxt = rangeTxt + "[        |]";
			break;
		}

		LCD.clear();
		LCD.drawString(batteryTxt, 0, Display.Row.BATTERY.ordinal());
		LCD.drawString(voltageTxt, 0, Display.Row.VOLTAGE.ordinal());
		LCD.drawString(statusTxt, 0, Display.Row.STATUS.ordinal());
		LCD.drawString(positionTxt, 0, Display.Row.POSITION.ordinal());
		LCD.drawString(positionNextTxt, 0, Display.Row.NEXT_POSITION.ordinal());
		LCD.drawString(rangeTxt, 0, Display.Row.RANGE.ordinal());
		LCD.refresh();

	}
}

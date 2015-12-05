package behaviour;

public class PidController extends Thread {
	protected int period = 100;
	protected double target = 0;
	protected double position = 0;
	private double kp = 0;
	private double ki = 0;
	private double kd = 0;
	private double err = 0;
	private double preErr = 0;
	private boolean isEnable = false;
	private int counter = 0;
	private double integral = 0;
	private double derivation = 0;

	public PidController(double _kp, double _ki, double _kd, int _period) {
		super("PidController");
		kp = _kp;
		ki = _ki;
		kd = _kd;
		if (_period > 0) {
			period = _period;
		}
		initController();
		this.start();
	}

	protected void initController() {
		isEnable = true;
		counter = 0;
	}

	public void setEnable(boolean _isEnable) {
		isEnable = _isEnable;
	}

	public void setTarget(double _target) {
		target = _target;
	}

	public void setPosition(double _position) {
		position = _position;
	}

	public double getPosition() {
		return position;
	}

	protected void calculate() {
		err = target - position;
		integral = integral + (err * period);
		derivation = (err - preErr) / period;
		position = (int) (kp * err + ki * integral + kd * derivation);
		preErr = err;

	}

	public int getCounter() {
		return counter;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (isEnable) {
					calculate();
					counter++;
				} else {
					initController();
				}
				Thread.sleep(period);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "PidController [period=" + period + ", target=" + target
				+ ", position=" + position + ", kp=" + kp + ", ki=" + ki
				+ ", kd=" + kd + ", err=" + err + ", preErr=" + preErr
				+ ", isEnable=" + isEnable + ", counter=" + counter + "]";
	}

}

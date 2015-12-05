package behaviour;

public class Position {
	float x = 0;
	float y = 0;
	float heading = 0;

	/**
	 * Set the point to pass by as x and y. the initial orientation is 45
	 * degrees
	 * 
	 * @param x
	 * @param y
	 * @param heading
	 */
	public Position(float x, float y, float heading) {
		this.x = x;
		this.y = y;
		this.heading = heading;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

}

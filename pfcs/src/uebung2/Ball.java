package uebung2;

import javax.media.opengl.GL2;

public class Ball {
	private static final double GRAVITY = 9.81;
	private static final double AIR_DENSITY = 1.2041; // luftdichte kg/m^3
	private static final double CW = 0.4; // stroemungswiderstandskoeffizient
	private static final double deltaT = 0.02;

	private double k;
	private int framesLived = 0;
	private double mass = 0.058; // mass 58g
	private double radius = 0.034; // ball radius 34cm
	private double area = radius * radius * Math.PI; // angestroemte flaeche m^2

	private Vector2d position;
	private Vector2d velocity;
	private Vector2d acceleration;

	// private Vector3d position;
	// private Vector3d velocity;
	// private Vector3d acceleration;

	public Ball(GL2 gl, double x, double y, double velocity, double omega) {
		this.position = new Vector2d(x, y);

		double vx = velocity * Math.cos(Math.toRadians(omega));
		double vy = velocity * Math.sin(Math.toRadians(omega));
		this.velocity = new Vector2d(vx, vy);
		acceleration = new Vector2d(0, -GRAVITY);
		k = 0.5 * AIR_DENSITY * CW * area;
	}

	public void draw(GL2 gl) {
		gl.glColor3d(1, 1, 1);

		int nPkte = 40;
		double timeStep = 2.0 * Math.PI / nPkte;
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < nPkte; i++) {
			gl.glVertex2d(position.x + (radius * 10) * Math.cos(i * timeStep),
					position.y + (radius * 10) * Math.sin(i * timeStep));
		}
		gl.glEnd();
	}

	public double getFramesLived() {
		return framesLived;
	}

	public double getX() {
		return position.x;
	}

	public double getY() {
		return position.y;
	}

	public void update() {
		double vxOld = velocity.x;
		double vyOld = velocity.y;
		velocity.x = vxOld + (-k / mass)
				* Math.sqrt((vxOld * vxOld) + (vyOld * vyOld)) * vxOld * deltaT;
		velocity.y = vyOld
				+ ((-k / mass) * Math.sqrt((vxOld * vxOld) + (vyOld * vyOld))
						* vyOld + acceleration.y) * deltaT;
		position.x = position.x + ((vxOld + velocity.x) / 2) * deltaT;
		position.y = position.y + ((vyOld + velocity.y) / 2) * deltaT;
		if (position.y <= radius * 10) {
			position.y = radius * 10;
			velocity.y = -velocity.y;
		} else {
			velocity.y += acceleration.y * deltaT;
		}
		++framesLived;
	}
}

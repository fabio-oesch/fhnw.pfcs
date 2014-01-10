package uebung5;

import java.awt.Color;

import javax.media.opengl.GL2;

public class Particle {

	private static final Color color = Color.BLACK;
	private static final double RADIUS = 0.1;

	private double x = 0;
	private double y = 0;
	private double speed = 0.2;

	public Particle(double x, double y, double speed) {
		this.x = x;
		this.y = y;
		this.speed = speed;
	}

	public void draw(GL2 gl) {
		gl.glColor3d(color.getRed(), color.getGreen(), color.getBlue());
		Draw.smallCircle2d(gl, RADIUS, x, y);
	}

	public void move(Dynamics d) {
		double[] v = { x, y };
		double[] p = d.move(v, speed);
		x = p[0];
		y = p[1];
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
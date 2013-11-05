package uebung3;

import java.awt.geom.Point2D;

import javax.media.opengl.GL2;

public class Track {

	private GL2 gl;

	public Track(GL2 gl) {
		this.gl = gl;
	}

	public void draw() {
		gl.glPushMatrix();
		zeichneRennbahn(gl);
		gl.glPopMatrix();
	}

	void zeichneStrecke(GL2 gl, double s) {
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2d(0, 0);
		gl.glVertex2d(s, 0);
		gl.glEnd();
		gl.glTranslated(0, -10, 0);
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex2d(0, 0);
		gl.glVertex2d(s, 0);
		gl.glEnd();
		gl.glTranslated(0, 10, 0);
	}

	private void zeichneKurve(GL2 gl) {
		gl.glTranslated(-60, 15, 0);
		double s = 0.8;
		for (int i = 0; i < 70; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
		}
		double phi = 0, dphi = 0.1;
		for (int i = 0; i < 30; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi += dphi;
		}
		for (int i = 0; i < 30; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
		}
		for (int i = 0; i < 5; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi -= dphi;
		}
		for (int i = 0; i < 70; i++) {
			zeichneStrecke(gl, s);
			gl.glTranslated(s, 0, 0);
		}
	}

	private void zeichneRennbahn(GL2 gl) {
		gl.glColor3d(0, 0, 0);
		zeichneKurve(gl);
	}

	public Point2D.Double getStartPosition() {
		return new Point2D.Double(-41, 10);
	}

	public double getStartAngle() {
		return 0.0;
	}

}

package janfaessler.assignment_3;

import javax.media.opengl.GL2;
import javax.vecmath.Point2d;

public class RoundCourse implements Course {
	
	private GL2 gl;
	
	public RoundCourse(GL2 gl) {
		this.gl = gl;
	}
	
	public void draw() {		
		drawRound(new Point2d(0, 20), 0, 0.1, 1);
		drawRound(new Point2d(0, 15), 0, 0.1, 0.75);
	}
	
	private void drawRound(Point2d start, double phi, double dphi, double s) {
		gl.glPushMatrix();
		gl.glTranslated(start.x, start.y, 0);
		gl.glColor3d(0.75, 0.75, 0.75);

		for (int i = 0;  i < 30; i++) {
			DrawUtils.drawLine(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi += dphi;
		}
		for (int i = 0;  i < 30; i++) {
			DrawUtils.drawLine(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
		}
		for (int i = 0;  i < 30; i++) {
			DrawUtils.drawLine(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi -= dphi;
		}
		for (int i = 0;  i < 30; i++) {
			DrawUtils.drawLine(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi += dphi;
		}
		for (int i = 0;  i < 30; i++) {
			DrawUtils.drawLine(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
		}
		for (int i = 0;  i < 30; i++) {
			DrawUtils.drawLine(gl, s);
			gl.glTranslated(s, 0, 0);
			gl.glRotated(-phi, 0, 0, 1);
			phi -= dphi;
		}
		gl.glPopMatrix();
	}
	


	@Override
	public Point2d getStartPosition() {
		return new Point2d(0, 17.5);
	}

	@Override
	public double getStartAngle() {
		return 0.0;
	}

}

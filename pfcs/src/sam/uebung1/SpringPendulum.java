package sam.uebung1;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.FPSAnimator;

public class SpringPendulum implements WindowListener, KeyListener,
		GLEventListener {

	public static void main(String[] args) {
		new SpringPendulum();
	}

	private double x = 0, y = 0, z = 0; // start coordinates
	private double speedMultiplier = 1;
	private double timeStep = 0.04; // s
	private double ballRadius = 0.5;
	private double amplitude = 3;
	private double helperRadius = amplitude - ballRadius;
	private double angle = 0;
	private final double gravity = 9.81;
	private boolean showHelperCircle = false;
	private double xRot = 0.0;
	private double yRot = 0.0;

	private GLU glu = new GLU();

	// input variables
	public SpringPendulum() {
		Frame frame = new Frame("Assignment 1: Spring pendulum");
		frame.setSize(800, 600);
		frame.addWindowListener(this);
		frame.addKeyListener(this);
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		frame.add(canvas);
		frame.setVisible(true);
		FPSAnimator anim = new FPSAnimator(canvas, 60, true);
		anim.start();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslated(0.0, 0.0, -17.0);
		gl.glRotated(xRot, 0.0, 1.0, 0.0);
		gl.glRotated(yRot, 1.0, 0.0, 0.0);
		gl.glColor3f(0.5f, 0.5f, 0.5f);
		drawAxes(gl);

		// calculations
		angle += 1 * speedMultiplier;
		helperRadius = amplitude - ballRadius;
		double phi = angle * timeStep;
		y = helperRadius * Math.sin(phi);

		// draw helper circle/ball
		if (showHelperCircle) {
			gl.glColor3d(0.75, 0, 0);
			drawCircle(gl, amplitude, 0, 0, 0, false);
			gl.glColor3d(1, 0, 0);
			drawCircle(gl, ballRadius, helperRadius * Math.cos(phi), y, z, true);
		}

		// draw spring
		gl.glColor3d(1, 1, 1);
		drawSpring(gl, ballRadius, 0, y + ballRadius, z);

		// draw ball
		gl.glColor3d(0, 1, 0);
		// drawCircle(gl, ballRadius, x, y, true);
		drawShpere(gl, ballRadius, x, y, z);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	private void drawAxes(GL2 gl) {
		gl.glBegin(GL2.GL_LINES);
		{
			// x-axis
			gl.glVertex3d(-10, 0, 0);
			gl.glVertex3d(10, 0, 0);
			// y-axis
			gl.glVertex3d(0, -10, 0);
			gl.glVertex3d(0, 10, 0);
		}
		gl.glEnd();
	}

	private void drawCircle(GL2 gl, double r, double xm, double ym, double zm,
			boolean fill) {
		int nPoints = 40; // number of points
		double dt = 2.0 * Math.PI / nPoints; // time step
		int mode;
		if (fill) {
			mode = GL2.GL_POLYGON;
		} else {
			mode = GL2.GL_LINE_LOOP;
		}
		gl.glBegin(mode);
		{
			for (int i = 0; i < nPoints; i++) {
				gl.glVertex3d(xm + r * Math.cos(i * dt),
						ym + r * Math.sin(i * dt), zm);
			}
		}
		gl.glEnd();
	}

	private void drawShpere(GL2 gl, double r, double xm, double ym, double zm) {
		gl.glTranslated(xm, ym, zm);
		GLUquadric earth = glu.gluNewQuadric();
		glu.gluQuadricDrawStyle(earth, GLU.GLU_LINE);
		glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
		glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
		final double radius = r;
		final int slices = 16;
		final int stacks = 16;
		glu.gluSphere(earth, radius, slices, stacks);
		glu.gluDeleteQuadric(earth);
	}

	private void drawSpring(GL2 gl, double r, double xm, double ym, double zm) {
		int loops = 12; // number of loops
		int points = loops * 40; // number of points
		int total = loops * points;
		double anchor = 6; // spring anchor
		double stretch = 0; // spring stretch
		double dt = 2.0 * loops * Math.PI / points; // time step
		gl.glBegin(GL2.GL_LINE_STRIP);
		{
			// top vertices
			gl.glVertex3d(0, (anchor + r * Math.sin(10 * dt)) + 0.5, zm);
			gl.glVertex3d(0, anchor + (0 / total * (loops - ym)) - stretch, zm);
			// loops
			for (int i = 0; i < points; i++) {
				gl.glVertex3d(xm + r * Math.cos(i * dt), anchor
						+ (i / total * (loops - ym)) - stretch,
						r * Math.sin(i * dt));
				stretch += ((anchor - ym) / (points + 40));
			}
			// bottom vertex
			gl.glVertex3d(0,
					anchor + (points / total * (loops - ym)) - stretch, zm);
			gl.glVertex3d(0, ym, zm);
		}
		gl.glEnd();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glClearDepth(1.0);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			reset();
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			showHelperCircle = !showHelperCircle;
			System.out.println("show helper circle: " + showHelperCircle);
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			if (xRot < 360) {
				xRot += 5;
			} else {
				xRot = 0;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			if (xRot > -360) {
				xRot -= 5;
			} else {
				xRot = 0;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_W) {
			if (yRot < 360) {
				yRot += 5;
			} else {
				yRot = 0;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			if (yRot > -360) {
				yRot -= 5;
			} else {
				yRot = 0;

			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (amplitude < 5) {
				amplitude += 0.1;
			}
			System.out.println("new amplitude: " + amplitude);
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (amplitude > 1) {
				amplitude -= 0.1;
			}
			System.out.println("new amplitude: " + amplitude);
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (speedMultiplier < 3) {
				speedMultiplier += 0.05;
			}
			System.out.println("new speedMultiplier: " + speedMultiplier);
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (speedMultiplier > 0.5) {
				speedMultiplier -= 0.05;
			}
			System.out.println("new speedMultiplier: " + speedMultiplier);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void reset() {
		showHelperCircle = false;
		xRot = 0.0;
		yRot = 0.0;
		amplitude = 3;
		speedMultiplier = 1;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		final GL2 gl = drawable.getGL().getGL2();
		if (height <= 0) {
			height = 1;
		}
		final float aspect = (float) width / height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, aspect, 0.3, 100.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
package uebung1;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

public class SpringPendulum extends JFrame implements GLEventListener,
		KeyListener {

	private double timeStep = 0.04; // Zeitschritt (s)
	private double omega = 0; // winkel f??r Federbewegung
	private double ampli = 0.5; // Feder Amplitude
	private boolean showHelperRotator = false;
	private boolean showAxes = false;
	private double speedMultiplier = 0; // Schritte in der die Geschwindigkeit
										// ver??ndert werden kann

	private final int x = 0, y = 0; // Startkoordinaten Ball
	private final int nPkte = 40; // Ball/Kreis Genauigkeit
	private final int SpringPunkteCount = 20; // Feder Ring Genauigkeit
	private final int SpringRingCount = 15; // Feder Anzahl Ringe
	private final double lFederEnde = 0.25; // L??nge der Feder enden
	private final double ballRadius = 0.5; // Radius Ball
	private final double circleRadius = 3; // Radius Circle
	private final double rad = circleRadius - ballRadius; // helper radius
	private final double ampliSteps = 0.1; // Schritte in der die Amplittude
											// ver??ndert werden kann

	private TextRenderer renderer;

	private static final long serialVersionUID = -6243699006762458154L;

	public static void main(String[] args) {
		new SpringPendulum();
	}

	private SpringPendulum() {
		this.setName("Federpendel");
		this.setTitle("Federpendel");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);

		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		this.add(canvas);
		FPSAnimator anim = new FPSAnimator(canvas, 60, true);
		anim.start();
		this.setVisible(true);
	}

	private void drawCircle(GL2 gl, double r, double x, double y, boolean fill) {

		double dt = 2.0 * Math.PI / nPkte;
		gl.glBegin(fill ? gl.GL_POLYGON : gl.GL_LINE_LOOP);
		for (int i = 0; i < nPkte; i++) {
			gl.glVertex2d(x + r * Math.cos(i * dt), y + r * Math.sin(i * dt));
		}
		gl.glEnd();
	}

	private void drawSpring(GL2 gl, double x, double yt, double yb) {

		double ah = (yt - yb - 2 * lFederEnde)
				/ (SpringPunkteCount * SpringRingCount); // Abschnitt-H??he
		double dt = 2.0 * Math.PI / SpringPunkteCount; // Parameter-Schrittweite
		double h = yt - lFederEnde;

		gl.glBegin(gl.GL_LINE_STRIP);
		gl.glVertex2d(x - 1, yt + 1);
		gl.glVertex2d(x - 1, yt - lFederEnde);
		gl.glVertex2d(x + 1, yt - lFederEnde);
		gl.glVertex2d(x + 1, yt + 1);
		gl.glEnd();

		gl.glBegin(gl.GL_LINE_STRIP);

		for (int j = 0; j < SpringRingCount; j++) {
			for (int i = 0; i < SpringPunkteCount; i++) {
				gl.glVertex3d(x + ampli * Math.cos(i * dt), h,
						y + ampli * Math.sin(i * dt));
				h -= ah;
			}
		}

		gl.glVertex2d(x, yb + lFederEnde);
		gl.glVertex2d(x, yb);
		gl.glEnd();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		renderer = new TextRenderer(new Font("Arial", Font.BOLD, 10));
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glClear(gl.GL_COLOR_BUFFER_BIT); // Bild loeschen

		// calculations
		omega += 1;
		double wt = omega * timeStep;
		double yr = y + (rad + speedMultiplier) * Math.sin(wt);

		// draw helper circle/ball
		if (showHelperRotator) {
			gl.glColor3d(1, 1, 1);
			drawCircle(gl, circleRadius, 0, 0, false);
			gl.glColor3d(1, 0, 1);
			drawCircle(gl, ballRadius, rad * Math.cos(wt),
					y + rad * Math.sin(wt), true);

		}

		// draw spring
		gl.glColor3d(1, 1, 1);
		drawSpring(gl, x, 7, yr + ballRadius);

		// draw ball
		gl.glColor3d(1, 1, 1);
		drawCircle(gl, ballRadius, x, yr, true);

		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(Color.WHITE);
		renderer.draw("Amplitude: " + String.format("%.2f", speedMultiplier),
				10, 20);
		renderer.draw(
				"Help: 'r' to show radius. 'UP' to increase amplitude. 'DOWN' to decrease amplitude.",
				10, 35);
		renderer.endRendering();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glViewport(0, 0, width, height);
		double aspect = (double) height / width;
		double left = -10, right = 10;
		double bottom = aspect * left, top = aspect * right;
		double near = -100, far = 100;
		gl.glMatrixMode(gl.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(left, right, bottom, top, near, far);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		if (c == 'r') {
			showHelperRotator = !showHelperRotator;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			speedMultiplier += 0.1;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			speedMultiplier -= 0.1;
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}

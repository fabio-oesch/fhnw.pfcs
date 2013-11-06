package uebung2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

public class SchieferWurf implements WindowListener, GLEventListener,
		KeyListener {

	private static final String TITLE = "Schiefer Wurf";
	private static final int DISPLAY_WIDTH = 800;
	private static final int DISPLAY_HEIGHT = 600;
	private static final int FPS = 60;

	public static void main(String[] args) {
		new SchieferWurf();
	}

	private double windowSizeWidth = 40;
	private boolean enabled; // ball machine enabled
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private double frames; // number of time steps/frames
	private double startVelocity; // m/s
	private Vector2d startPosition;
	private double omega; // throwing angle
	private int ballsThrown; // number of balls thrown
	private double delay; // time delay (frames)

	private TextRenderer renderer;

	public SchieferWurf() {
		Frame f = new Frame(TITLE);
		f.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		f.addWindowListener(this);
		f.addKeyListener(this);
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		f.add(canvas);
		f.setVisible(true);
		FPSAnimator anim = new FPSAnimator(canvas, FPS, true);
		anim.start();
		reset();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glClearColor(0, 0, 0, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		++frames;

		gl.glPushMatrix();
		{
			gl.glTranslated(0, -15, 0);
			gl.glColor3d(0.5, 0.5, 0.5);

			drawCross(gl);
			drawFloor(gl);

			if (enabled) {
				// generate new ball and add to balls list if delay is over
				if (frames % delay == 0) {
					double angle = omega;

					Ball ball = new Ball(gl, startPosition.x, startPosition.y,
							startVelocity, angle);
					balls.add(ball);
					frames = 0;
					System.out.println("Balls thrown:" + ++ballsThrown);
				}
			}

			// update balls
			for (int i = 0; i < balls.size(); ++i) {
				Ball ball = balls.get(i);
				ball.update();
				ball.draw(gl);
				// remove ball if not in horizontal viewport
				if (ball.getX() < -windowSizeWidth
						|| ball.getX() > windowSizeWidth
						|| ball.getFramesLived() > 2000) {
					balls.remove(ball);
					ball = null;
				}
			}
		}
		gl.glPopMatrix();

		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(Color.WHITE);
		renderer.draw("Angle: " + omega, 10, 510);
		renderer.draw("Velocity: " + startVelocity, 10, 490);
		renderer.draw(
				"Help: 'SPACE' to reset. 'UP' to increase speed. 'DOWN' to decrease speed. 'LEFT' to shoot further to the left.",
				10, 550);
		renderer.draw(
				"'RIGHT' to shoot further to the right. 't' to start and stop.",
				30, 530);
		renderer.endRendering();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	private void drawCross(GL2 gl) {
		gl.glBegin(GL2.GL_LINES);
		{
			// horizontal
			gl.glVertex3d(startPosition.x - 1, startPosition.y, 0);
			gl.glVertex3d(startPosition.x + 1, startPosition.y, 0);
			// vertical
			gl.glVertex3d(startPosition.x, startPosition.y - 1, 0);
			gl.glVertex3d(startPosition.x, startPosition.y + 1, 0);
		}
		gl.glEnd();
	}

	private void drawFloor(GL2 gl) {
		gl.glBegin(GL2.GL_LINES);
		{
			gl.glVertex3d(-windowSizeWidth, 0, 0);
			gl.glVertex3d(windowSizeWidth, 0, 0);
		}
		gl.glEnd();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
		renderer = new TextRenderer(new Font("Arial", Font.BOLD, 10));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_SPACE:
			reset();
			break;
		case KeyEvent.VK_RIGHT:
			omega -= 2;
			omega = omega % 360;
			break;
		case KeyEvent.VK_LEFT:
			omega += 2;
			omega = omega % 360;
			break;
		case KeyEvent.VK_DOWN:
			startVelocity -= 1;
			break;
		case KeyEvent.VK_UP:
			startVelocity += 1;
			break;

		case KeyEvent.VK_T:
			enabled = !enabled;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void reset() {
		enabled = true;
		frames = 0;
		startVelocity = 30;
		startPosition = new Vector2d(-20, 8);
		omega = 25;
		ballsThrown = 0;
		delay = 10;
		balls.clear();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		double aspect = (double) height / width;
		double left = -windowSizeWidth;
		double right = windowSizeWidth;
		double bottom = left * aspect;
		double top = right * aspect;
		double near = -100, far = 100;
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(left, right, bottom, top, near, far);
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

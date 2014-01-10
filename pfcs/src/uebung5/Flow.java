package uebung5;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;

public class Flow implements GLEventListener, KeyListener {

	private double left = -25, right = 25;
	private double near = -100, far = 100;

	private static double r = 5.0;
	private ArrayList<Particle> particles = new ArrayList<Particle>();

	private int waveCount = 0;
	private int waveDistance = 4;

	private double startY = -4.75;
	private double startX = -25;
	private double particleCount = 20;

	private double speed = 0.2;

	Dynamics dynamic = new Dynamics(r);

	private TextRenderer renderer;

	public Flow() {
		JFrame f = new JFrame("Flow");
		GLCanvas canvas = new GLCanvas();
		f.setSize(800, 600);
		f.addKeyListener(this);
		canvas.addGLEventListener(this);
		canvas.addKeyListener(this);
		f.add(canvas);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FPSAnimator anim = new FPSAnimator(canvas, 200, true);
		anim.start();
	}

	public static void main(String[] args) {
		new Flow();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT + GL.GL_DEPTH_BUFFER_BIT);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_FILL);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();

		if (waveCount++ % waveDistance == 0) {

			for (int i = 0; i < particleCount; i++)
				particles.add(new Particle(startX, (i * 0.5 + startY), speed));
		}
		Iterator<Particle> it = particles.iterator();
		while (it.hasNext()) {
			Particle p = it.next();
			p.move(dynamic);
			p.draw(gl);
			if (p.getX() > right)
				it.remove();
		}
		gl.glColor3d(0.5, 0.5, 0.5);
		Draw.circle2d(gl, r, 0, 0);

		// draw info
		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(Color.BLACK);
		renderer.draw("Particle Count: " + particleCount, 10, 490);
		renderer.draw("Particle Speed: " + String.format("%.2f", speed), 10, 510);
		renderer.draw(
				"Help: 'r' to reset. 'UP' to increase speed. 'DOWN' to decrease speed. 'LEFT' to decrease particles. 'RIGHT' to increase particles.",
				10, 530);
		renderer.endRendering();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		renderer = new TextRenderer(new Font("Arial", Font.BOLD, 10));
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl0 = drawable.getGL();
		GL2 gl = gl0.getGL2();
		gl.glViewport(0, 0, width, height);
		double aspect = (double) height / width;
		double bottom = aspect * left, top = aspect * right;
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(left, right, bottom, top, near, far);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_R:
			particles.clear();
			break;
		case KeyEvent.VK_UP:
			if (speed < 0.5) {
				speed += 0.05;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (speed > 0.05) {
				speed -= 0.05;
			}
			break;
		case KeyEvent.VK_LEFT:
			if (particleCount > 2) {
				startY += 0.5;
				particleCount -= 2;
			}
			break;
		case KeyEvent.VK_RIGHT:
			startY -= 0.5;
			particleCount += 2;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}